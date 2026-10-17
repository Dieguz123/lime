package com.diego.lime.controller;


import com.diego.lime.dto.CreateRoomRequest;
import com.diego.lime.dto.JoinRoomRequest;
import com.diego.lime.model.Room;
import com.diego.lime.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Room created = roomService.createRoom(request.creatorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Room> getRoomByCode(@PathVariable String code) {
        return roomService.getRoomByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @PostMapping("/{code}/join")
    public ResponseEntity<Room> joinRoom (@PathVariable String code, @Valid @RequestBody JoinRoomRequest request){
        Room room = roomService.joinRoom(code, request.userId());
        return ResponseEntity.ok(room);
    }


}
