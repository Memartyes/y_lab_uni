package ru.domain.entities;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Определяем класс для определения рабочих мест в Конференц-залах
 */
public class Workspace {
    private String id;
    private String bookedBy;
    private LocalDateTime bookingTime;

    /**
     * Конструктор для создания нового рабочего места.
     * @param id the workspace ID;
     */
    public Workspace(String id) {
        this.id = id;
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
     * Бронируем рабочее место
     *
     * @param userId the user's ID who books the workspace
     * @param time the booking time
     */
    public void book(String userId, LocalDateTime time) {
        if(bookedBy != null && !isBookingExpired()) {
            throw new IllegalStateException("Workspace is already booked");
        }

        if (!isBookingTimeValid(time)) {
            throw new IllegalArgumentException("Booking time is not valid");
        }

        this.bookedBy = userId;
        this.bookingTime = time;
    }

    /**
     * Проверяем, забронировано ли рабочее место
     *
     * @return true if workspace is booked, false otherwise
     */
    public boolean isBooked() {
        return bookedBy != null && !isBookingExpired();
    }

    /**
     * Возвращаем ID пользователя забронировавшего рабочее место
     *
     * @return the user's ID who booked the workspace, or null if not booked
     */
    public String getBookedBy() {
        return isBookingExpired() ? null : bookedBy;
    }

    /**
     * Проверяем доступность бронирования на подходящее время и рабочий день.
     *
     * @param time the booking time
     * @return true if the booking time is valid, false otherwise
     */
    public boolean isBookingTimeValid(LocalDateTime time) {
        int hour = time.getHour();
        String dayOfWeek = time.getDayOfWeek().name();

        for (DayOfWeek validDay : WorkspaceConfig.WORK_DAYS.getDays()) {
            if (validDay.toString().equalsIgnoreCase(dayOfWeek)) {
                return hour >= WorkspaceConfig.START_HOUR.getValue() && hour < WorkspaceConfig.END_HOUR.getValue();
            }
        }
        return false;
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
     * Возвращаем забронированное время
     *
     * @return the booking time, or null if not booked
     */
    public String getBookingTime() {
        if (bookingTime != null && !isBookingExpired()) {
            return bookingTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        return null;
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
