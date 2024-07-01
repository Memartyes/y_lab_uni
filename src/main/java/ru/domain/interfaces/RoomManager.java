package ru.domain.interfaces;

import ru.domain.entities.ConferenceRoom;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс для управления Конференц-залами.
 */
public interface RoomManager {

    /**
     * Инициализируем конференц-залы заранее определенными именами.
     */
    void initializeConferenceRoom();

    /**
     * Создает новый конференц-зал.
     *
     * @param roomName the conference room name
     */
    void addConferenceRoom(String roomName);

    /**
     * Изменяет название уже существующего конференц-зала.
     *
     * @param conferenceRoomName the old conference room name
     * @param newConferenceRoomName the new conference room name
     */
    void updateConferenceRoomName(String conferenceRoomName, String newConferenceRoomName);

    /**
     * Удаляет уже существующий конференц-зал по его названию.
     *
     * @param roomName the conference room name
     */
    void deleteConferenceRoom(String roomName);

    /**
     * Возвращает конференц-зал по запрошенному названию.
     *
     * @param roomName  the conference room name
     * @return the conference room by name
     */
    ConferenceRoom getConferenceRoom(String roomName);

    /**
     * Возвращает доступные слоты рабочих мест в конференц-зале.
     *
     * @param conferenceRoomName the conference room name
     * @param date the date
     * @return the list of available slots in conference room
     */
    List<String> getAvailableSlots(String conferenceRoomName, LocalDate date);
}
