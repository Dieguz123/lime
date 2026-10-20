package com.diego.lime.service;

import com.diego.lime.model.Film;
import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import com.diego.lime.repository.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private User diego;
    private User anna;
    private String code;
    private Film inception;

    @BeforeEach
    void setUp() {
        diego = userService.createUser("Diego");
        anna = userService.createUser("Anna");

        Room room = roomService.createRoom(diego.getId());
        code = room.getInviteCode();
        roomService.joinRoom(code, anna.getId());

        inception = filmRepository.save(new Film("Inception", 2010, "Prime", "Sci-Fi", 1L, null, null));
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
        User marco = userService.createUser("Marco");
        assertThrows(IllegalStateException.class,
                () -> swipeService.swipe(code, marco.getId(), inception.getId(), true));
    }

    @Test
    void getMatches_afterMatch_containsFilm() {
        swipeService.swipe(code, diego.getId(), inception.getId(), true);
        swipeService.swipe(code, anna.getId(), inception.getId(), true);

        List<Film> matches = swipeService.getMatches(code);

        assertEquals(1, matches.size());
        assertEquals("Inception", matches.get(0).getName());
    }
}


