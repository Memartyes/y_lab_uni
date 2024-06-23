package ru.domain.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Определяем класс для определения рабочих мест в Конференц-залах
 */
public class Workspace {
    private String id;
    private boolean booked;
    private String bookedBy;
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
     * Возвращаем ID рабочего места.
     *
     * @return the workspace ID
     */
    public String getId() {
        return id;
    }

    /**
     * Проверяем, забронировано ли рабочее место
     *
     * @return true if workspace is booked, false otherwise
     */
    public boolean isBooked() {
        return booked;
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
     * Возвращаем ID пользователя забронировавшего рабочее место
     *
     * @return the user's ID who booked the workspace, or null if not booked
     */
    public String getBookedBy() {
        return bookedBy;
    }

    /**
     * Возвращаем забронированное время
     *
     * @return the booking time
     */
    public LocalDateTime getBookingTime() {
        return bookingTime;
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
