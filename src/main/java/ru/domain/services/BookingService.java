package ru.domain.services;

import ru.domain.interfaces.Bookable;

import java.time.LocalDateTime;

/**
 * Сервис для управления логикой бронирования.
 */
public class BookingService {

    /**
     * Бронируем обьект, реализующий интерфейс Bookable.
     *
     * @param Bookable the object that we need to book
     * @param userName the username
     * @param bookingTime the booking time
     */
    public void book(Bookable Bookable, String userName, LocalDateTime bookingTime, int bookingDurationHours) {
        Bookable.book(userName, bookingTime, bookingDurationHours);
    }

    /**
     * Отменяем бронирование обьекта, реализующего интерфейс Bookable.
     *
     * @param Bookable the object that we need to cancel book
     */
    public void cancelBooking(Bookable Bookable) {
        Bookable.cancelBooking();
    }

    /**
     * Проверяем забронирован ли обьект, реализующий интерфейс Bookable.
     *
     * @param Bookable the object to check
     * @return true of booked, false otherwise
     */
    public boolean isBooked(Bookable Bookable) {
        return Bookable.isBooked();
    }
}
