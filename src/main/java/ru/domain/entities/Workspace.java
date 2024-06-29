package ru.domain.entities;

import lombok.Getter;
import ru.domain.config.WorkspaceConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Определяем класс для определения рабочих мест в Конференц-залах
 */
@Getter
public class Workspace {
    /**
     * -- GETTER --
     *  Возвращаем ID рабочего места.
     *
     * @return the workspace ID
     */
    private String id;
    /**
     * -- GETTER --
     *  Проверяем, забронировано ли рабочее место
     *
     * @return true if workspace is booked, false otherwise
     */
    private boolean booked;
    /**
     * -- GETTER --
     *  Возвращаем ID пользователя забронировавшего рабочее место
     *
     * @return the user's ID who booked the workspace, or null if not booked
     */
    private String bookedBy;
    /**
     * -- GETTER --
     *  Возвращаем забронированное время
     *
     * @return the booking time
     */
    private LocalDateTime bookingTime;

    /**
     * Конструктор для создания нового рабочего места.
     * @param id the workspace ID;
     */
    public Workspace(String id) {
        this.id = id;
        this.booked = false;
        this.bookedBy = null; //изначально, рабочее место изначально никем не забронировано
        this.bookingTime = null; //изначально, рабочее место не забронировано ни на какое время
    }

    /**
     * Бронируем рабочее место
     *
     * @param userId the user's ID who books the workspace
     * @param bookingTime the booking time
     */
    public void book(String userId, LocalDateTime bookingTime) {
        this.booked = true;
        this.bookedBy = userId;
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
