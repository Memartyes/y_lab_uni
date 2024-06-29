package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;
import ru.domain.config.WorkspaceConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Определяем класс для определения рабочих мест в Конференц-залах
 */
public class Workspace {
    @Getter
    @Setter
    private String name;
    @Setter
    @Getter
    private boolean booked;
    @Setter
    @Getter
    private String bookedBy;
    @Setter
    @Getter
    private LocalDateTime bookingTime;
    @Getter
    @Setter
    private int id;

    /**
     * Конструктор для создания нового рабочего места.
     * @param name the workspace name;
     */
    public Workspace(String name) {
        this.name = name;
        this.booked = false;
        this.bookedBy = null; //изначально, рабочее место изначально никем не забронировано
        this.bookingTime = null; //изначально, рабочее место не забронировано ни на какое время
    }

    public Workspace() {}

    /**
     * Бронируем рабочее место
     *
     * @param userName the user's name who books the workspace
     * @param bookingTime the booking time
     */
    public void book(String userName, LocalDateTime bookingTime) {
        this.booked = true;
        this.bookedBy = userName;
        this.bookingTime = bookingTime;
    }

    /**
     * Отменяем бронирование рабочих мест
     */
    public void cancelBooking() {
        this.booked = false;
        this.bookedBy = null;
        this.bookingTime = null;
    }

    /**
     * Проверяем, если бронирование истокло
     *
     * @return true if the booking has expired, false otherwise
     */
    public boolean isBookingExpired() {
        return bookingTime != null && LocalDateTime.now().isAfter(bookingTime.plusHours(WorkspaceConfig.BOOKING_DURATION_HOURS.getValue()));
    }

    /**
     * Возвращаем время окончания забронированного рабочего места
     *
     * @return the booking end time, or null if not booked
     */
    public String getBookingEndTime() {
        if (bookingTime != null && !isBookingExpired()) {
            return bookingTime.plusHours(WorkspaceConfig.BOOKING_DURATION_HOURS.getValue()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        return null;
    }
}
