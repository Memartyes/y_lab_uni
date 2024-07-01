package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;
import ru.domain.config.WorkspaceConfig;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Класс представляет бронирование рабочего места.
 */
@Getter
@Setter
public class Booking {
    private String bookedBy;
    private LocalDateTime bookingTime;
    private ZoneId zoneId = ZoneId.systemDefault();

    public Booking(String bookedBy, LocalDateTime bookingTime) {
        this.bookedBy = bookedBy;
        this.bookingTime = bookingTime;
    }

    /**
     * Проверяет, истекло ли время бронирования.
     *
     * @return true if the booking time is expired, false otherwise
     */
    public boolean isExpired() {
        LocalDateTime expirationTime = bookingTime.plusHours(WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
        return LocalDateTime.now(zoneId).isAfter(expirationTime);
    }

    /**
     * Возвращает строку, представляющую время окончания бронирования.
     *
     * @return the booked time as string, or null if not booked
     */
    public String getFormattedEndTime() {
        if (bookingTime == null) {
            return null;
        }
        return bookingTime.plusHours(WorkspaceConfig.BOOKING_DURATION_HOURS.getValue()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
