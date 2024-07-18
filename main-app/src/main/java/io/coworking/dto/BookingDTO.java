package io.coworking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
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

    @Min(value = 1, message = "Workspace ID must be greater than 0")
    private int workspaceId;

    @NotBlank(message = "Booked by Username cannot be blank")
    private String bookedBy;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Booking time cannot be null")
    private LocalDateTime bookingTime;

    @Min(value = 1, message = "Booking duration hours must be greater than 0")
    private int bookingDurationHours;
}
