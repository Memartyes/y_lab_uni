package ru.domain.interfaces;

import ru.domain.entities.Workspace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface Room {

    /**
     * Добавляем рабочее место.
     *
     * @param workspace the workspace to add
     */
    void addWorkspace(Workspace workspace);

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
