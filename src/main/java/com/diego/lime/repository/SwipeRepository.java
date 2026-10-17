package com.diego.lime.repository;

import com.diego.lime.model.Film;
import com.diego.lime.model.Room;
import com.diego.lime.model.Swipe;
import com.diego.lime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    boolean existsByRoomAndUserAndFilm(Room room, User user, Film film);

    boolean existsByRoomAndUserAndFilmAndLikedTrue(Room room, User user, Film film);
}
