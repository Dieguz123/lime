package com.diego.lime.service;

import com.diego.lime.exception.ForbiddenException;
import com.diego.lime.exception.NotFoundException;
import com.diego.lime.model.Provider;
import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import com.diego.lime.repository.ProviderRepository;
import com.diego.lime.repository.RoomRepository;
import com.diego.lime.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final InviteCodeGenerator codeGenerator;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository, ProviderRepository providerRepository, InviteCodeGenerator codeGenerator) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.providerRepository = providerRepository;
        this.codeGenerator = codeGenerator;
    }

    public Room createRoom(Long creatorId, Set<Long> providerIds){
        User creator = findUser(creatorId);
        Set<Provider> providers = findProviders(providerIds);
        Room room = new Room(generateUniqueCode(), creator);
        room.setProviders(providers);
        return roomRepository.save(room);
    }

    public Room getRoomByCode(String inviteCode, Long userId){

        Room room = roomRepository.findWithProvidersByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("Room not found"));
        User user = findUser(userId);

        if (!room.isMember(user)){
            throw new ForbiddenException("You are not part of this room");
        }

        return room;
    }

    public List<Room> getMyRooms(Long userId){
        User user = findUser(userId);
        return roomRepository.findByCreatorOrPartner(user, user);
    }


    public Room joinRoom(String inviteCode, Long userId) {
        Room room = roomRepository.findWithProvidersByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        User user = findUser(userId);

        if (room.getCreator().getId().equals(user.getId())){
            throw new IllegalStateException("You are already in this room");
        }

        room.join(user);
        return roomRepository.save(room);
    }

    public Room updateProviders(String inviteCode, Long userId, Set<Long> providerIds) {
        Room room = getRoomByCode(inviteCode, userId);
        room.setProviders(findProviders(providerIds));
        return roomRepository.save(room);
    }

    private User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Set<Provider> findProviders(Set<Long> providerIds) {
        List<Provider> providers = providerRepository.findAllById(providerIds);
        if (providers.size() != providerIds.size()) {
            throw new NotFoundException("Provider not found");
        }
        return new HashSet<>(providers);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = codeGenerator.generate();
        } while (roomRepository.existsByInviteCode(code));
        return code;
    }
}
