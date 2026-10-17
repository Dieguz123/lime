package com.diego.lime.repository;

import com.diego.lime.model.Film;
import com.diego.lime.model.Room;
import com.diego.lime.model.Swipe;
import com.diego.lime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    boolean existsByRoomAndUserAndFilm(Room room, User user, Film film);

    boolean existsByRoomAndUserAndFilmAndLikedTrue(Room room, User user, Film film);

    @Query("""
        SELECT f FROM Film f
        WHERE f.id IN (
            SELECT s.film.id FROM Swipe s
            WHERE s.room = :room AND s.liked = true
            GROUP BY s.film.id
            HAVING COUNT(s) = 2
        )
        ORDER BY f.id
        """)
    List<Film> findMatchedFilms(@Param("room") Room room);
}
