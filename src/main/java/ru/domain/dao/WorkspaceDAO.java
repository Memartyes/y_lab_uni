package ru.domain.dao;

import ru.domain.entities.Booking;
import ru.domain.entities.Workspace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для дальнейшей имплементации Workspace Data Access Object
 */
public interface WorkspaceDAO {

    /**
     * Добавляем рабочее место в базу данных.
     *
     * @param workspace the workspace to add
     */
    void addWorkspace(Workspace workspace);

    /**
     * Находим рабочее место по ID.
     *
     * @param id the workspace ID
     * @return the workspace
     */
    Optional<Workspace> findWorkspaceById(int id);

    /**
     * Находим рабочее место по его названию.
     *
     * @param name the workspace name
     * @return the Optional with Workspace if the workspace found, empty Optional otherwise
     */
    Optional<Workspace> findWorkspaceByName(String name);

    /**
     * Находим все рабочие места.
     *
     * @return the list of workspaces
     */
    List<Workspace> findAllWorkspaces();

    /**
     * Обновляем рабочее место
     *
     * @param workspace the workspace object to update
     */
    void updateWorkspace(Workspace workspace);

    /**
     * Удаляем рабочее место по ID
     * @param id the workspace ID
     */
    void deleteWorkspace(int id);

    /**
     * Находим рабочие места, которые доступны для бронирования.
     *
     * @return the list of available workspaces
     */
    List<Workspace> findAvailableWorkspaces();

    /**
     * Находим все рабочие места, связанные с определенным конференц-залом.
     *
     * @param conferenceRoomId the conference room ID
     * @return the list of workspaces by conference room ID
     */
    List<Workspace> findWorkspacesByConferenceRoomId(int conferenceRoomId);

    /**
     * Находим все забронированные рабочие места.
     *
     * @return the list of booked workspaces
     */
    List<Workspace> findBookedWorkspaces();

    /**
     * Проверяем доступность рабочего места на определенное время.
     *
     * @param workspaceId the workspace ID
     * @param bookingTime the booking time
     * @return true if the workspace is available at the given time, false otherwise
     */
    boolean isWorkspaceAvailable(int workspaceId, LocalDateTime bookingTime);

    /**
     * Находим рабочее место и его бронирования по идентификатору.
     *
     * @param id the workspace ID
     * @return the workspace with its bookings, or empty Optional if not found
     */
    Optional<Workspace> findWorkspaceWithBookingsById(int id);

    /**
     * Находим бронирования по ID рабочего места.
     *
     * @param workspaceId the workspace ID
     * @return the list of bookings by workspace ID
     */
    List<Booking> findBookingsByWorkspaceId(int workspaceId);
}
