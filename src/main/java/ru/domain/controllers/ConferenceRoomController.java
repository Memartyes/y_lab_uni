package ru.domain.controllers;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.domain.dto.ConferenceRoomDTO;
import ru.domain.entities.ConferenceRoom;
import ru.domain.managers.ConferenceRoomManager;
import ru.domain.mapper.ConferenceRoomMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conference_rooms")
//@Tag(name = "Conference Room Controller Management", description = "APIs for managing conference rooms")
public class ConferenceRoomController {

    private final ConferenceRoomManager conferenceRoomManager;

    @Autowired
    public ConferenceRoomController(ConferenceRoomManager conferenceRoomManager) {
        this.conferenceRoomManager = conferenceRoomManager;
    }

    /**
     * Получаем список конференц-залов.
     *
     * @return the conference rooms list
     */
    @GetMapping
//    @Operation(summary = "Get all conference rooms", description = "Retrieve a list of all conference rooms")
    public ResponseEntity<List<ConferenceRoomDTO>> getAllConferenceRooms() {
        List<ConferenceRoom> conferenceRoomList = conferenceRoomManager.findAllConferenceRooms();
        List<ConferenceRoomDTO> conferenceRoomDTOList = ConferenceRoomMapper.INSTANCE.toDTOList(conferenceRoomList);
        return ResponseEntity.ok(conferenceRoomDTOList);
    }

    /**
     * Получаем конференц-зал по ID.
     *
     * @param id the conference room ID
     * @return the conference room by id
     */
    @GetMapping("/{id}")
//    @Operation(summary = "Get conference room by ID", description = "Retrieve conference room by ID")
    public ResponseEntity<ConferenceRoomDTO> getConferenceRoomById(@PathVariable int id) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomManager.findConferenceRoomById(id);
        return conferenceRoom.map(room -> ResponseEntity.ok(ConferenceRoomMapper.INSTANCE.toDTO(room)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Создаем новый конференц-зал.
     *
     * @param conferenceRoomDTO
     * @return the HTTP-status
     */
    @PostMapping
//    @Operation(summary = "Create a new conference room", description = "Add a new conference room")
    public ResponseEntity<String> createConferenceRoom(@Valid @RequestBody ConferenceRoomDTO conferenceRoomDTO) {
        try {
            ConferenceRoom conferenceRoom = ConferenceRoomMapper.INSTANCE.toEntity(conferenceRoomDTO);
            conferenceRoomManager.addConferenceRoom(conferenceRoom);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Обновляем информацию о конференц-зале.
     * @param id the conference room ID
     * @param conferenceRoomDTO
     * @return the HTTP-status
     */
    @PutMapping("/{id}")
//    @Operation(summary = "Update existing conference room", description = "Update conference room by ID")
    public ResponseEntity<String> updateConferenceRoom(@PathVariable int id, @Valid @RequestBody ConferenceRoomDTO conferenceRoomDTO) {
        try {
            ConferenceRoom conferenceRoom = ConferenceRoomMapper.INSTANCE.toEntity(conferenceRoomDTO);
            conferenceRoom.setId(id);
            conferenceRoomManager.updateConferenceRoom(conferenceRoom);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Удаляем конференц-зал.
     *
     * @param id the conference room ID
     * @return the HTTP-status
     */
    @DeleteMapping("/{id}")
//    @Operation(summary = "Delete conference room", description = "Remove a conference room by ID")
    public ResponseEntity<String> deleteConferenceRoom(@PathVariable int id) {
        try {
            conferenceRoomManager.deleteConferenceRoom(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
