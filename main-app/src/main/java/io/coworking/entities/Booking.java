package io.coworking.entities;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс представляет бронирование рабочего места.
 */
@Data
public class Booking {
    private int id;
    private int workspaceId;
    private String bookedBy;
    private LocalDateTime bookingTime;
    private int bookingDurationHours; //WorkspaceConfig.BOOKING_DURATION_HOURS.getValue()

    public Booking(String bookedBy, LocalDateTime bookingTime, int bookingDurationHours) {
        if (bookedBy == null || bookingTime == null) {
            throw new IllegalArgumentException("Booked by User and Booking time cannot be null");
        }
        this.bookedBy = bookedBy;
        this.bookingTime = bookingTime;
        this.bookingDurationHours = bookingDurationHours;
    }

    public Booking() {}
}
