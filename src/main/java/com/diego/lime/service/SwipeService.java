package com.diego.lime.service;

import com.diego.lime.exception.NotFoundException;
import com.diego.lime.model.Film;
import com.diego.lime.model.Room;
import com.diego.lime.model.Swipe;
import com.diego.lime.model.User;
import com.diego.lime.repository.FilmRepository;
import com.diego.lime.repository.RoomRepository;
import com.diego.lime.repository.SwipeRepository;
import com.diego.lime.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SwipeService {

    private final SwipeRepository swipeRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    public SwipeService(SwipeRepository swipeRepository,
                        RoomRepository roomRepository,
                        UserRepository userRepository,
                        FilmRepository filmRepository){
        this.swipeRepository = swipeRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
    }

    public boolean swipe(String inviteCode, Long userId, Long filmId, boolean liked){
        Room room = roomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("Room not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found"));

        if (!room.isMember(user)){
            throw new IllegalStateException("User is not member of this room");
        }
        if (swipeRepository.existsByRoomAndUserAndFilm(room, user, film)){
            throw new IllegalStateException("Film already voted");
        }

        swipeRepository.save(new Swipe(room, user, film , liked));

        if (!liked || !room.isFull()){
            return false;
        }

        User otherMember = room.getOtherMember(user);
        return swipeRepository.existsByRoomAndUserAndFilmAndLikedTrue(room, otherMember, film);
    }

    public Optional<Film> getNextFilm(String inviteCode, Long userId) {
        Room room = roomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("Room not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!room.isMember(user)){
            throw new IllegalStateException(("User is not a member of this room"));
        }

        return filmRepository.findNotSwipedByUserInRoom(room, user)
                .stream()
                .findFirst();
    }

    public List<Film> getMatches(String inviteCode){
        Room room = roomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("Room not found"));
        return swipeRepository.findMatchedFilms(room);
    }
}
