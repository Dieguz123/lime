package com.diego.lime.controller;


import com.diego.lime.config.CurrentUser;
import com.diego.lime.dto.CreateRoomRequest;
import com.diego.lime.dto.JoinRoomRequest;
import com.diego.lime.model.Room;
import com.diego.lime.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@AuthenticationPrincipal Jwt jwt) {
        Room created = roomService.createRoom(CurrentUser.id(jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Room> getRoomByCode(@PathVariable String code, @AuthenticationPrincipal Jwt jwt) {
        Room room =  roomService.getRoomByCode(code, CurrentUser.id(jwt));
        return ResponseEntity.ok(room);
    }

    @PostMapping("/{code}/join")
    public ResponseEntity<Room> joinRoom (@PathVariable String code,@AuthenticationPrincipal Jwt jwt){
        Room room = roomService.joinRoom(code, CurrentUser.id(jwt));
        return ResponseEntity.ok(room);
    }


}
