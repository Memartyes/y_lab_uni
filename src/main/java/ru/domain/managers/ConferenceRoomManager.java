package ru.domain.managers;

import lombok.Getter;
import ru.domain.entities.ConferenceRoom;
import ru.domain.config.DefaultConferenceRooms;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Определим класс для управления Конференц-залами.
 */
@Getter
public class ConferenceRoomManager {
    /**
     * -- GETTER --
     *  Возвращаем репозиторий Конференц-зал'ов
     */
    private Map<String, ConferenceRoom> conferenceRooms;

    /**
     * Инициализируем конструктор с заранее установленными Конференц-залами
     */
    public ConferenceRoomManager() {
        this.conferenceRooms = new HashMap<>();
        initializeConferenceRooms();
    }

    /**
     * Инициализируем Конференц-залы заранее определенными именами
     */
    public void initializeConferenceRooms() {
        for (DefaultConferenceRooms room : DefaultConferenceRooms.values()) {
            addConferenceRoom(room.getName());
        }
    }

    /**
     * Создаем новый Конференц-зал.
     *
     * @param roomId the conference room ID
     */
    public void addConferenceRoom(String roomId) {
        if (conferenceRooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Conference room with id " + roomId + " already exists.");
        }

        conferenceRooms.put(roomId, new ConferenceRoom(roomId));
    }

    /**
     * Вносим изменение в ID уже существующего Конференц-зала
     *
     * @param oldId the old conference room ID
     * @param newId the new conference room ID
     */
    public void updateConferenceRoom(String oldId, String newId) {
        if (!conferenceRooms.containsKey(oldId)) {
            throw new IllegalArgumentException("Conference room with id " + oldId + " not found.");
        }
        if (conferenceRooms.containsKey(newId)) {
            throw new IllegalArgumentException("Conference room with id " + newId + " already exists.");
        }

        ConferenceRoom conferenceRoom = conferenceRooms.remove(oldId);
        conferenceRoom.setName(newId);
        conferenceRooms.put(newId, conferenceRoom);
    }

    /**
     * Удаляем уже существующий Конференц-зал
     *
     * @param id the conference room ID
     */
    public void deleteConferenceRoom(String id) {
        if (!conferenceRooms.containsKey(id)) {
            throw new IllegalArgumentException("Conference room with id " + id + " not found.");
        }
        conferenceRooms.remove(id);
    }

    /**
     * Возвращаем Конференц-зал по запрошенному ID
     *
     * @param roomId the conference room ID
     * @return conference room by ID
     */
    public ConferenceRoom getConferenceRoom(String roomId) {
        return conferenceRooms.get(roomId);
    }

    /**
     * Возвращаем доступные слоты рабочих мест в Конференц-зале
     *
     * @param conferenceRoomId the conference room ID
     * @param date the date
     * @return the list of available slots in conference room
     */
    public List<String> getAvailableSlots(String conferenceRoomId, LocalDate date) {
        ConferenceRoom conferenceRoom = conferenceRooms.get(conferenceRoomId);
        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        return conferenceRoom.getAvailableSlots(date.atStartOfDay());
    }

    /**
     * Метод фильтрует бронирования Конференц-залов по дате
     * @param date the date
     * @return the list of conference rooms that has booked workspaces on current date
     */
    public List<String> filterByDate(LocalDate date) {
        List<String> results = new ArrayList<>();
        for (ConferenceRoom room : conferenceRooms.values()) {
            if (room.hasBookingOnDate(date)) {
                results.add(room.getName() + " has booking on " + date);
            }
        }
        return results;
    }

    /**
     * Метод фильтрует бронирования Конференц-залов по пользователю
     * @param userId the user ID
     * @return the list of conference rooms that has booked by current user
     */
    public List<String> filterByUser(String userId) {
        List<String> results = new ArrayList<>();
        for (ConferenceRoom room : conferenceRooms.values()) {
            if (room.hasBookingByUser(userId)) {
                results.add(room.getName() + " has bookings by user " + userId);
            }
        }
        return results;
    }

    /**
     * Метод фильтрующий Конференц-залы с доступными рабочими местами для бронирования
     * @return the list of conference rooms available for book
     */
    public List<String> filterByAvailableWorkspaces() {
        return conferenceRooms.values().stream()
                .filter(ConferenceRoom::hasAvailableWorkspaces)
                .map(room -> room.getName() + " has available workspaces")
                .collect(Collectors.toList());
    }
}
