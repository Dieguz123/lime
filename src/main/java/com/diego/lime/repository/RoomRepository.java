package com.diego.lime.repository;

import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);

    List<Room> findByCreatorOrPartner(User creator, User partner);
}
