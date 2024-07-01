package ru.domain.interfaces;

import ru.domain.entities.Workspace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс для управления бронирования рабочих мест в Конференц-зале.
 */
public interface BookingManager {

    /**
     * Бронируем все доступные рабочие места для пользователя.
     *
     * @param userName the username
     * @param dateTime the date time
     */
    void bookAllWorkspaces(String userName, LocalDateTime dateTime);

    /**
     * Проверяем доступность временя для бронирования.
     *
     * @param dateTime the date time
     * @return true if available, false otherwise
     */
    boolean isBookingTimeAvailable(LocalDateTime dateTime);

    /**
     * Отменяем бронирование рабочего места по его названию.
     *
     * @param workspaceName the workspace name
     */
    void cancelBookingForWorkspace(String workspaceName);

    /**
     * Отменяем бронирование всех рабочих мест.
     */
    void cancelBookingForAllWorkspaces();

    /**
     * Проверяем наличие бронирования на указанную дату.
     *
     * @param date the date
     * @return return true if the date has books, false otherwise
     */
    boolean hasBookingOnDate(LocalDate date);

    /**
     * Проверяем наличие бронирований пользователем.
     *
     * @param userName the username
     * @return true if the user has books, false otherwise
     */
    boolean hasBookingByUser(String userName);

    /**
     * Возвращаем список доступных слотов для бронирования на указанную дату.
     * @param dateTime the date time
     * @return the list of available slots for booking
     */
    List<String> getAvailableSlots(LocalDateTime dateTime);

    /**
     * Возвращаем список доступных рабочих мест.
     *
     * @return the workspace available slots
     */
    List<Workspace> getAvailableWorkspaces();

    /**
     * Бронируем конкретное рабочее место
     *
     * @param workspaceName the workspace name
     * @param userName the username
     * @param bookingTime the booking time
     */
    void bookSpecificWorkspace(String workspaceName, String userName, LocalDateTime bookingTime, int bookingDurationHours);

    /**
     * Обновляем бронирование для конкретного рабочего места.
     *
     * @param workspaceName the workspace name
     * @param userName the username
     * @param newBookingTime the new booking time
     */
    void updateSpecificBooking(String workspaceName, String userName, LocalDateTime newBookingTime, int bookingDurationHours);
}
