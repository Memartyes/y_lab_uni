package ru.domain.interfaces;

import java.time.LocalDateTime;

/**
 * Интерфейс для обьектов, которые могут быть забронированы.
 */
public interface Bookable {

    /**
     * Бронируем обьект.
     *
     * @param userName the username
     * @param bookingTime the booking time
     */
    void book(String userName, LocalDateTime bookingTime, int bookingDurationHours);

    /**
     * Отменяем бронирование
     */
    void cancelBooking();

    /**
     * Отменяем бронирование по времени.
     *
     * @param bookingTime the booking time to remove the booking
     */
    void removeBooking(LocalDateTime bookingTime);

    /**
     * Проверяем, забронирован ли обьект.
     *
     * @return true if booked, false otherwise
     */
    boolean isBooked();
}
