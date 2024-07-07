package ru.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для бронирований рабочих мест.
 */
@Data
public class BookingDTO {

    private int id;

    @NotBlank(message = "Workspace ID cannot be blank")
    private int workspaceId;

    @NotBlank(message = "Booked by Username cannot be blank")
    private String bookedBy;

    @NotNull(message = "Booking time cannot be null")
    private LocalDateTime bookingTime;

    @NotBlank(message = "Booking duration hours cannot be blank")
    private int bookingDurationHours;
}
