package com.diego.lime.repository;

import com.diego.lime.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);
}
