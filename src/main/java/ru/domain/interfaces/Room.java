package ru.domain.interfaces;

import ru.domain.entities.Workspace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface Room {

    /**
     * Получаем рабочее место по его названию.
     *
     * @param workspaceName the workspace name
     * @return the workspace by name, or null otherwise
     */
    Optional<Workspace> getWorkspace(String workspaceName);

    /**
     * Добавляем рабочее место.
     *
     * @param workspace the workspace to add
     */
    void addWorkspace(Workspace workspace);

    /**
     * Возвращаем количество доступных рабочих мест.
     *
     * @return the count of available workspaces
     */
    int getAvailableWorkspaceCount();

    /**
     * Бронируем все рабочие места для пользователя
     *
     * @param userName the username
     * @param bookingTime the booking time
     */
    void bookAllWorkspaces(String userName, LocalDateTime bookingTime);

    /**
     * Отменяем бронирование рабочего места
     *
     * @param workspaceName the workspace name
     */
    void cancelBookingForWorkspace(String workspaceName);

    /**
     * Проверяем доступность рабочего места по времени.
     *
     * @param dateTime the date time
     * @return true if the date time is available for book, false otherwise
     */
    boolean isBookingTimeAvailable(LocalDateTime dateTime);

    /**
     * Отменяем бронирования всех рабочих мест.
     */
    void cancelBookingForAllWorkspaces();

    /**
     * Проверяем наличие бронирования на указанную дату.
     *
     * @param date the date to check the booking
     * @return true if booked, false otherwise
     */
    boolean hasBookingOnDate(LocalDate date);

    /**
     * Проверяем наличие бронирований пользователя.
     *
     * @param userName the username
     * @return true if the user has books
     */
    boolean hasBookingByUser(String userName);

    /**
     * Проверяем наличие свободных рабочих мест.
     *
     * @return true if available at least one available workspace slot, false otherwise
     */
    boolean hasAvailableWorkspaces();
}
