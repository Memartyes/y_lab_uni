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
            this.conferenceRooms.put(room.getName(), new ConferenceRoom(room.getName()));
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
     * @param oldRoomName the old conference room ID
     * @param newRoomName the new conference room ID
     */
    public void updateConferenceRoomName(String oldRoomName, String newRoomName) {
        if (!conferenceRooms.containsKey(oldRoomName)) {
            throw new IllegalArgumentException("Conference room with name " + oldRoomName + " not found.");
        }
        if (conferenceRooms.containsKey(newRoomName)) {
            throw new IllegalArgumentException("Conference room with name " + newRoomName + " already exists.");
        }

        ConferenceRoom conferenceRoom = conferenceRooms.remove(oldRoomName);
        conferenceRoom.setName(newRoomName);
        conferenceRooms.put(newRoomName, conferenceRoom);
    }

    /**
     * Удаляем уже существующий Конференц-зал по его имени
     *
     * @param roomName the conference room roomName
     */
    public void deleteConferenceRoom(String roomName) {
        if (!conferenceRooms.containsKey(roomName)) {
            throw new IllegalArgumentException("Conference room with roomName " + roomName + " not found.");
        }
        conferenceRooms.remove(roomName);
    }

    /**
     * Возвращаем Конференц-зал по запрошенному ID
     *
     * @param roomName the conference room ID
     * @return conference room by ID
     */
    public ConferenceRoom getConferenceRoom(String roomName) {
        if (!conferenceRooms.containsKey(roomName)) {
            throw new IllegalArgumentException("Conference room with name " + roomName + " not found.");
        }
        return conferenceRooms.get(roomName);
    }

    /**
     * Возвращаем доступные слоты рабочих мест в Конференц-зале
     *
     * @param conferenceRoomId the conference room ID
     * @param date the date
     * @return the list of available slots in conference room
     */
    public List<String> getAvailableSlots(String conferenceRoomId, LocalDate date) {
        ConferenceRoom room = getConferenceRoom(conferenceRoomId);
        if (room == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        return room.getBookingManager().getAvailableSlots(date.atStartOfDay());
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
