package com.diego.lime.repository;

import com.diego.lime.model.Film;
import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import com.diego.lime.tmdb.TmdbMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("""
            SELECT f FROM Film f
            WHERE f.id NOT IN (
                SELECT s.film.id FROM Swipe s
                WHERE s.room = :room AND s.user = :user
            )
            ORDER BY f.id           
""")
    List<Film> findNotSwipedByUserInRoom(@Param("room") Room room, @Param("user") User user);

    boolean existsByTmdbId(Long id);
}
