package ru.domain.dao;

import ru.domain.entities.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для дальнейшей имплементации Booking Data Access Object
 */
public interface BookingDAO {

    /**
     * Добавляем бронирование.
     *
     * @param booking the booking
     */
    void addBooking(Booking booking);

    /**
     * Находим бронирование по ID
     *
     * @param id the booking ID
     * @return the Optional object of Booking
     */
    Optional<Booking> findBookingById(int id);

    /**
     * Находим все бронирования.
     *
     * @return the list of all bookings
     */
    List<Booking> findAllBookings();

    /**
     * Обновляем бронирование.
     *
     * @param booking the booking to update
     */
    void updateBooking(Booking booking);

    /**
     * Удаляем бронирование по ID.
     *
     * @param id the booking ID to delete
     */
    void deleteBooking(int id);

    /**
     * Находим бронирования по ID рабочего места.
     *
     * @param workspaceId the workspace ID
     * @return the list of bookings by workspace ID
     */
    List<Booking> findBookingsByWorkspaceId(int workspaceId);

    /**
     * Находим бронирования по имени пользователя.
     *
     * @param bookedBy the user's name
     * @return the list of bookings by user's name
     */
    List<Booking> findBookingsByUser(String bookedBy);

    /**
     * Находим бронирования на определенную дату.
     *
     * @param date the date of bookings
     * @return the list of bookings on the specified date
     */
    List<Booking> findBookingsByDate(LocalDateTime date);

    /**
     * Отменяем бронирования по имени пользователя.
     *
     * @param bookedBy the user's name
     */
    void cancelBookingsByUser(String bookedBy);

    /**
     * Проверяем доступность бронирования на определенное время.
     *
     * @param workspaceId the workspace ID
     * @param bookingTime the booking time to check
     * @return true if the booking is available, false otherwise
     */
    boolean isBookingAvailable(int workspaceId, LocalDateTime bookingTime);
}
