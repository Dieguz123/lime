package com.diego.lime.repository;

import com.diego.lime.model.Film;
import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import com.diego.lime.tmdb.TmdbMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Long> {

    //EXISTS instead of a JOIN: a film on two of the room's providers must not be returned twice.
    //A room without providers has no filter, which is what the NOT EXISTS branch is for.
    @Query("""
            SELECT f FROM Film f
            WHERE f.id NOT IN (
                SELECT s.film.id FROM Swipe s
                WHERE s.room = :room AND s.user = :user
            )
            AND (
                NOT EXISTS (
                    SELECT 1 FROM Room r JOIN r.providers p
                    WHERE r = :room
                )
                OR EXISTS (
                    SELECT 1 FROM Room r JOIN r.providers p
                    WHERE r = :room AND p MEMBER OF f.providers
                )
            )
            ORDER BY f.id
            LIMIT 1
""")
    Optional<Film> findNextForUserInRoom(@Param("room") Room room, @Param("user") User user);

    boolean existsByTmdbId(Long id);

    Optional<Film> findByTmdbId(Long id);
}
