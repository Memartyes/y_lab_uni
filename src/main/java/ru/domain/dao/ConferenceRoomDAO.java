package ru.domain.dao;

import ru.domain.entities.Booking;
import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.Workspace;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для дальнейшей имплементации ConferenceRoom Data Access Object
 */
public interface ConferenceRoomDAO {
    /**
     * Добавляем новый конференц-зал в базу данных.
     *
     * @param conferenceRoom the conference room
     */
    void addConferenceRoom(ConferenceRoom conferenceRoom);

    /**
     * Находим конференц-зал по его ID
     *
     * @param id the conference room ID
     * @return Optional with conference room
     */
    Optional<ConferenceRoom> findConferenceRoomById(int id);

    /**
     * Возвращаем список всех конференц-залов.
     *
     * @return the list of conference rooms
     */
    List<ConferenceRoom> findAllConferenceRooms();

    /**
     * Обновляем информацию о конференц-зале.
     *
     * @param conferenceRoom the conference room to update
     */
    void updateConferenceRoom(ConferenceRoom conferenceRoom);

    /**
     * Удаляем конференц-зал по его ID.
     *
     * @param id the conference room id
     */
    void deleteConferenceRoom(int id);

    /**
     * Добавляем рабочее место в конференц-зал.
     *
     * @param conferenceRoomId the conference room ID
     * @param workspace the workspace
     */
    void addWorkspaceToConferenceRoom(int conferenceRoomId, Workspace workspace);

    /**
     * Находим все рабочие места в конференц-зале.
     *
     * @param conferenceRoomId the conference room ID
     * @return the list of workspaces in conference room
     */
    List<Workspace> findWorkspacesByConferenceRoomId(int conferenceRoomId);

    /**
     * Находим бронирование по ID рабочего места.
     *
     * @param workspaceId the workspace ID
     * @return the list of bookings in workspace
     */
    List<Booking> findBookingsByWorkspaceId(int workspaceId);

    /**
     * Находим конференц-залы с бронированиями на указанную дату.
     *
     * @param date the date
     * @return the list of conference rooms
     */
    List<ConferenceRoom> findRoomsByDate(LocalDate date);

    /**
     * Находим конференц-залы с бронированиями пользователем.
     *
     * @param userName the user's name
     * @return the list of conference rooms
     */
    List<ConferenceRoom> findRoomsByUser(String userName);

    /**
     * Находим конференц-залы с доступными рабочими местами.
     *
     * @return the list of conference rooms
     */
    List<ConferenceRoom> findRoomsWithAvailableWorkspaces();
}
