package ru.domain.managers;

import lombok.Getter;
import lombok.Setter;
import ru.domain.config.WorkspaceConfig;
import ru.domain.entities.ConferenceRoom;
import ru.domain.config.DefaultConferenceRooms;
import ru.domain.entities.Workspace;

import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;

/**
 * Определим класс для управления Конференц-залами.
 */
@Getter
public class ConferenceRoomManager {
    private Map<String, ConferenceRoom> conferenceRooms;
    @Setter
    private ConferenceRoomFilter conferenceRoomFilter;

    /**
     * Инициализируем конструктор с заранее установленными Конференц-залами
     */
    public ConferenceRoomManager() {
        this.conferenceRooms = new HashMap<>();
        this.conferenceRoomFilter = new ConferenceRoomFilter(conferenceRooms);
        initializeConferenceRooms();
    }

    /**
     * Инициализируем Конференц-залы заранее определенными именами
     */
    public void initializeConferenceRooms() {
        for (DefaultConferenceRooms room : DefaultConferenceRooms.values()) {
            this.conferenceRooms.put(room.getName(), new ConferenceRoom(room.getName(), WorkspaceConfig.WORKSPACES_CAPACITY.getValue()));
        }
    }

    /**
     * Создаем новый Конференц-зал.
     *
     * @param roomName the conference room name
     */
    public void addConferenceRoom(String roomName) {
        if (conferenceRooms.containsKey(roomName)) {
            throw new IllegalArgumentException("Conference room with name " + roomName + " already exists.");
        }

        conferenceRooms.put(roomName, new ConferenceRoom(roomName, WorkspaceConfig.WORKSPACES_CAPACITY.getValue()));
    }

    /**
     * Вносим изменение в ID уже существующего Конференц-зала
     *
     * @param oldRoomName the old conference room name
     * @param newRoomName the new conference room name
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
     * Добавляем рабочее место в Конференц-зал.
     *
     * @param conferenceRoomName the conference room name
     * @param workspaceName the workspace name
     */
    public void addWorkspaceToConferenceRoom(String conferenceRoomName, String workspaceName) {
        ConferenceRoom conferenceRoom = getConferenceRoom(conferenceRoomName);
        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with name " + conferenceRoomName + " not found.");
        }
        Optional<Workspace> existingWorkspace = conferenceRoom.getWorkspace(workspaceName);
        if (existingWorkspace.isPresent()) {
            throw new IllegalArgumentException("Workspace with name " + workspaceName + " already exists in conference room " + conferenceRoomName);
        }
        Workspace workspace = new Workspace(workspaceName);
        conferenceRoom.addWorkspace(workspace);
    }

    /**
     * Бронируем все доступные рабочие места во всех конференц-залах для пользователя.
     *
     * @param conferenceRoomName the conference room
     * @param userName the username
     * @param bookingTime the booking time
     */
    public void bookWholeConferenceRoom(String conferenceRoomName, String userName, LocalDateTime bookingTime) {
        ConferenceRoom room = getConferenceRoom(conferenceRoomName);
        if (room == null) {
            throw new IllegalArgumentException("Conference room with name " + conferenceRoomName + " not found.");
        }

        for (Workspace workspace : room.getWorkspaces()) {
            if (!workspace.isBooked()) {
                workspace.book(userName, bookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
            }
        }
    }

    /**
     * Бронируем рабочее место для пользователя в Конференц-зале.
     *
     * @param workspaceName the workspace name
     * @param userName the username
     * @param bookingTime the booking time
     */
    public void bookWorkspace(String conferenceRoomName, String workspaceName, String userName, LocalDateTime bookingTime, int bookingDurationHours) {
        ConferenceRoom room = getConferenceRoom(conferenceRoomName);
        if (room == null) {
            throw new IllegalArgumentException("Conference room with name " + conferenceRoomName + " not found.");
        }

        Workspace workspace = room.getWorkspace(workspaceName).orElseThrow(() ->
                new IllegalArgumentException("Workspace with name " + workspaceName + " not found in conference room " + conferenceRoomName));

        if (workspace.isBooked()) {
            throw new IllegalStateException("Workspace with name " + workspaceName + " is already booked.");
        }

        workspace.book(userName, bookingTime, bookingDurationHours);
    }

    /**
     * Отменяем бронирование рабочего места в Конференц-зале.
     *
     * @param workspaceName the workspace name
     */
    public void cancelBookingForWorkspace(String conferenceRoomName, String workspaceName) {
        ConferenceRoom room = getConferenceRoom(conferenceRoomName);
        if (room == null) {
            throw new IllegalArgumentException("Conference room with name " + conferenceRoomName + " not found.");
        }

        Workspace workspace = room.getWorkspace(workspaceName).orElseThrow(() ->
                new IllegalArgumentException("Workspace with name " + workspaceName + " not found in conference room " + conferenceRoomName));
        workspace.cancelBooking();
    }

    /**
     * Отменяем бронирование всех рабочих мест в Конференц-зале.
     *
     * @param conferenceRoomName the conference room name.
     */
    public void cancelBookingForAllWorkspaces(String conferenceRoomName) {
        ConferenceRoom room = getConferenceRoom(conferenceRoomName);
        if (room == null) {
            throw new IllegalArgumentException("Conference room with name " + conferenceRoomName + " not found.");
        }

        for (Workspace workspace : room.getWorkspaces()) {
            workspace.cancelBooking();
        }
    }

    /**
     * Возвращаем доступные слоты рабочих мест в Конференц-зале
     *
     * @param conferenceRoomName the conference room ID
     * @param date the date
     * @return the list of available slots in conference room
     */
    public List<String> getAvailableSlots(String conferenceRoomName, LocalDate date) {
        ConferenceRoom room = getConferenceRoom(conferenceRoomName);
        if (room == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomName + " not found.");
        }
        return room.getBookingWorkspaceManager().getAvailableSlots(date.atStartOfDay());
    }

    /**
     * Метод фильтрует бронирования Конференц-залов по дате
     * @param date the date
     * @return the list of conference rooms that has booked workspaces on current date
     */
    public List<String> filterByDate(LocalDate date) {
        return conferenceRoomFilter.filterByDate(date);
    }

    /**
     * Метод фильтрует бронирования Конференц-залов по пользователю
     * @param userId the user ID
     * @return the list of conference rooms that has booked by current user
     */
    public List<String> filterByUser(String userId) {
        return conferenceRoomFilter.filterByUser(userId);
    }

    /**
     * Метод фильтрующий Конференц-залы с доступными рабочими местами для бронирования
     * @return the list of conference rooms available for book
     */
    public List<String> filterByAvailableWorkspaces() {
        return conferenceRoomFilter.filterByAvailableWorkspaces();
    }
}
