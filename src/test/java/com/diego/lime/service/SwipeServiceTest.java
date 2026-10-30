package com.diego.lime.service;

import com.diego.lime.exception.ForbiddenException;
import com.diego.lime.model.Film;
import com.diego.lime.model.Provider;
import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import com.diego.lime.repository.FilmRepository;
import com.diego.lime.repository.ProviderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SwipeServiceTest {

    @Autowired
    private SwipeService swipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User diego;
    private User anna;
    private String code;
    private Film inception;
    private Provider netflix;
    private Provider disney;

    @BeforeEach
    void setUp() {
        diego = userService.register("Diego", "password123");
        anna = userService.register("Anna", "Anna");

        Room room = roomService.createRoom(diego.getId(), "Movie night", Set.of());
        code = room.getInviteCode();
        roomService.joinRoom(code, anna.getId());

        inception = filmRepository.save(new Film("Inception", 2010, 1L, null, null));

        netflix = providerRepository.save(new Provider(8L, "Netflix", "/netflix.jpg"));
        disney = providerRepository.save(new Provider(337L, "Disney Plus", "/disney.jpg"));
    }

    @Test
    void swipe_bothLike_returnsMatch() {
        swipeService.swipe(code, diego.getId(), inception.getId(), true);

        boolean match = swipeService.swipe(code, anna.getId(), inception.getId(), true);

        assertTrue(match);
    }

    @Test
    void swipe_sameFilmTwice_throws() {
        swipeService.swipe(code, diego.getId(), inception.getId(), true);

        assertThrows(IllegalStateException.class,
                () -> swipeService.swipe(code, diego.getId(), inception.getId(), false));
    }

    @Test
    void swipe_firstLikeOnly_noMatch(){
        boolean match = swipeService.swipe(code, diego.getId(), inception.getId(), true);

        assertFalse(match);
    }

    @Test
    void swipe_likeThenDislike_noMatch() {
        swipeService.swipe(code, diego.getId(), inception.getId(), true);

        boolean match = swipeService.swipe(code, anna.getId(), inception.getId(), false);

        assertFalse(match);
    }

    @Test
    void swipe_dislikeThenLike_noMatch() {
        swipeService.swipe(code, diego.getId(), inception.getId(), false);

        boolean match = swipeService.swipe(code, anna.getId(), inception.getId(), true);

        assertFalse(match);
    }

    @Test
    void swipe_userNotInRoom_throws() {
        User marco = userService.register("Marco", "1234567890");
        assertThrows(ForbiddenException.class,
                () -> swipeService.swipe(code, marco.getId(), inception.getId(), true));
    }

    @Test
    void getMatches_afterMatch_containsFilm() {
        swipeService.swipe(code, diego.getId(), inception.getId(), true);
        swipeService.swipe(code, anna.getId(), inception.getId(), true);

        List<Film> matches = swipeService.getMatches(code, diego.getId());

        assertEquals(1, matches.size());
        assertEquals("Inception", matches.get(0).getName());
    }

    @Test
    void getMatches_userNotInRoom_throws() {
        swipeService.swipe(code, diego.getId(), inception.getId(), true);
        swipeService.swipe(code, anna.getId(), inception.getId(), true);

        User marco = userService.register("marco", "12345");

        assertThrows(ForbiddenException.class,
                () -> swipeService.getMatches(code, marco.getId()));

    }

    @Test
    void getNextFilm_UserNotInRoom_throws(){
        User marco = userService.register("marco", "12345");

        assertThrows(ForbiddenException.class,
                () -> swipeService.getNextFilm(code, marco.getId()));
    }

    @Test
    void getNextFilm_roomWithoutProviders_returnsFilmWithNoProviders() {
        Optional<Film> next = swipeService.getNextFilm(code, diego.getId());

        assertEquals(Optional.of(inception.getId()), next.map(Film::getId));
    }

    @Test
    void getNextFilm_roomWithProviders_skipsFilmsOnOtherPlatforms() {
        Film onDisney = saveFilm("Frozen", 2L, disney);
        Film onNetflix = saveFilm("Roma", 3L, netflix);
        roomService.updateProviders(code, diego.getId(), Set.of(netflix.getId()));

        Optional<Film> next = swipeService.getNextFilm(code, diego.getId());

        assertEquals(Optional.of(onNetflix.getId()), next.map(Film::getId));
        assertTrue(onDisney.getId() < onNetflix.getId());
    }

    @Test
    void getNextFilm_roomWithProviders_skipsFilmsWithNoProviders() {
        roomService.updateProviders(code, diego.getId(), Set.of(netflix.getId()));

        assertTrue(swipeService.getNextFilm(code, diego.getId()).isEmpty());
    }

    @Test
    void getNextFilm_filmOnSeveralRoomProviders_isReturnedOnce() {
        Film onBoth = saveFilm("Roma", 2L, netflix, disney);
        roomService.updateProviders(code, diego.getId(), Set.of(netflix.getId(), disney.getId()));

        assertEquals(Optional.of(onBoth.getId()), swipeService.getNextFilm(code, diego.getId()).map(Film::getId));

        swipeService.swipe(code, diego.getId(), onBoth.getId(), true);

        assertTrue(swipeService.getNextFilm(code, diego.getId()).isEmpty());
    }

    @Test
    void getNextFilm_filmOnOneOfTheRoomProviders_isReturned() {
        Film onDisney = saveFilm("Frozen", 2L, disney);
        roomService.updateProviders(code, diego.getId(), Set.of(netflix.getId(), disney.getId()));

        assertEquals(Optional.of(onDisney.getId()), swipeService.getNextFilm(code, diego.getId()).map(Film::getId));
    }

    @Test
    void getNextFilm_providersOfAnotherRoom_doNotApply() {
        Film onNetflix = saveFilm("Roma", 2L, netflix);
        roomService.createRoom(anna.getId(), "Other room", Set.of(disney.getId()));
        roomService.updateProviders(code, diego.getId(), Set.of(netflix.getId()));

        assertEquals(Optional.of(onNetflix.getId()), swipeService.getNextFilm(code, diego.getId()).map(Film::getId));
    }

    @Test
    void getNextFilm_providersRemoved_filterIsOff() {
        roomService.updateProviders(code, diego.getId(), Set.of(netflix.getId()));
        roomService.updateProviders(code, diego.getId(), Set.of());

        assertEquals(Optional.of(inception.getId()), swipeService.getNextFilm(code, diego.getId()).map(Film::getId));
    }

    @Test
    void getNextFilm_loadsASingleFilm() {
        saveFilm("Roma", 2L, netflix);
        saveFilm("Frozen", 3L, disney);
        entityManager.flush();
        entityManager.clear();

        Statistics statistics = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        swipeService.getNextFilm(code, diego.getId());

        assertEquals(1, statistics.getEntityStatistics(Film.class.getName()).getLoadCount());
    }

    private Film saveFilm(String name, Long tmdbId, Provider... providers) {
        Film film = new Film(name, 2010, tmdbId, null, null);
        film.setProviders(Set.of(providers));
        return filmRepository.save(film);
    }
}


