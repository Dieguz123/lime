package com.diego.lime.repository;

import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByInviteCode(String inviteCode);

    //RoomResponse reads these outside of any session. The graph makes everything it doesn't list lazy,
    //even the eager @ManyToOne relations, so creator and partner have to be listed too.
    @EntityGraph(attributePaths = {"creator", "partner", "providers"})
    Optional<Room> findWithProvidersByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);

    @EntityGraph(attributePaths = {"creator", "partner", "providers"})
    List<Room> findByCreatorOrPartner(User creator, User partner);
}
