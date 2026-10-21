package com.diego.lime.service;

import com.diego.lime.exception.ForbiddenException;
import com.diego.lime.exception.NotFoundException;
import com.diego.lime.model.Room;
import com.diego.lime.model.User;
import com.diego.lime.repository.RoomRepository;
import com.diego.lime.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final InviteCodeGenerator codeGenerator;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository, InviteCodeGenerator codeGenerator) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.codeGenerator = codeGenerator;
    }

    public Room createRoom(Long creatorId){
        User creator = findUser(creatorId);
        Room room = new Room(generateUniqueCode(), creator);
        return roomRepository.save(room);
    }

    public Room getRoomByCode(String inviteCode, Long userId){

        Room room = roomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("Room not found"));
        User user = findUser(userId);

        if (!room.isMember(user)){
            throw new ForbiddenException("You are not part of this room");
        }

        return room;
    }




    public Room joinRoom(String inviteCode, Long userId) {
        Room room = roomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        User user = findUser(userId);

        if (room.getCreator().getId().equals(user.getId())){
            throw new IllegalStateException("You are already in this room");
        }

        room.join(user);
        return roomRepository.save(room);
    }

    private User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = codeGenerator.generate();
        } while (roomRepository.existsByInviteCode(code));
        return code;
    }
}
