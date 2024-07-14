package ru.domain.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.domain.dto.BookingDTO;
import ru.domain.entities.Booking;
import ru.domain.managers.WorkspaceManager;
import ru.domain.mapper.BookingMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Booking Controller Management", description = "APIs for managing bookings")
public class BookingController {

    private final WorkspaceManager workspaceManager;

    @Autowired
    public BookingController(WorkspaceManager workspaceManager) {
        this.workspaceManager = workspaceManager;
    }

    /**
     * Получаем список всех бронирований.
     * @return the booking list
     */
    @GetMapping
    @Operation(summary = "Get all bookings", description = "Retrieve a list of all users")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<Booking> bookings = workspaceManager.findAllBookings();
        List<BookingDTO> bookingDTOS = BookingMapper.INSTANCE.toDTOList(bookings);
        return ResponseEntity.ok(bookingDTOS);
    }

    /**
     * Получаем бронирование по ID
     * @param id the booking id
     * @return the booking by id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID", description = "Retrieve booking by ID")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable int id) {
        Optional<Booking> booking = workspaceManager.findBookingById(id);
        return booking.map(value -> ResponseEntity.ok(BookingMapper.INSTANCE.toDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Бронируем место в конференц-зале.
     * @param bookingDTO the booking to create
     * @return the HTTP-status
     */
    @PostMapping
    @Operation(summary = "Create a new booking", description = "Add a new booking")
    public ResponseEntity<String> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        try {
            Booking booking = BookingMapper.INSTANCE.toEntity(bookingDTO);
            workspaceManager.bookWorkspace(booking.getWorkspaceId(), booking.getBookedBy(), booking.getBookingTime(), booking.getBookingDurationHours());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Обновляем информацию о бронировании
     * @param id the booking ID
     * @param bookingDTO the booking to update
     * @return
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update existing booking", description = "Update booking by ID")
    public ResponseEntity<String> updateBooking(@PathVariable int id, @Valid @RequestBody BookingDTO bookingDTO) {
        try {
            Booking booking = BookingMapper.INSTANCE.toEntity(bookingDTO);
            booking.setId(id);
            workspaceManager.updateBooking(booking);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Удаляем бронирование по ID
     *
     * @param id the booking ID
     * @return the HTTP-status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete booking", description = "Remove a booking by ID")
    public ResponseEntity<String> deleteBooking(@PathVariable int id) {
        try {
            workspaceManager.cancelBooking(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
