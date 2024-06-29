package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Клас представляет рабочие места.
 */
@Getter
@Setter
public class Workspace {
    private int id;
    private String name;
    private Booking currentBooking;

    private boolean booked; //TODO: delete after database refactoring.
    private String bookedBy; //TODO: delete after database refactoring.
    private LocalDateTime bookingTime; //TODO: delete after database refactoring.

    /**
     * Конструктор для создания нового рабочего места.
     * @param name the workspace name;
     */
    public Workspace(String name) {
        this.name = name;
    }

    public Workspace() {}

    /**
     * Бронируем рабочее место
     *
     * @param userName the user's name who books the workspace
     * @param bookingTime the booking time
     */
    public void book(String userName, LocalDateTime bookingTime) {
        this.currentBooking = new Booking(userName, bookingTime);
    }

    /**
     * Отменяем бронирование рабочих мест
     */
    public void cancelBooking() {
        this.currentBooking = null;
    }

    /**
     * Проверяем, забронировано ли рабочее место.
     *
     * @return true if the workspace is booked, false otherwise
     */
    public boolean isBooked() {
        return currentBooking != null && !currentBooking.isExpired();
    }

    /**
     * Возвращаем время окончания забронированного рабочего места
     *
     * @return the booking end time as string
     */
    public String getBookingEndTime() {
        return currentBooking == null ? null : currentBooking.getFormattedEndTime();
    }
}
