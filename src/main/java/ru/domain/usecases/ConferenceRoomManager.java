package ru.domain.usecases;

import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.DefaultConferenceRooms;
import ru.domain.entities.Workspace;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Определим класс для управления Конференц-залами и рабочими местами.
 */
public class ConferenceRoomManager {
    private Map<String, ConferenceRoom> conferenceRoomRepository;

    /**
     * Инициализируем конструктор с заранее установленными Конференц-залами
     */
    public ConferenceRoomManager() {
        this.conferenceRoomRepository = new HashMap<>();
        initializeConferenceRooms();
    }

    /**
     * Инициализируем Конференц-залы заранее определенными именами
     */
    public void initializeConferenceRooms() {
        for (DefaultConferenceRooms room : DefaultConferenceRooms.values()) {
            createConferenceRoom(room.getName());
        }
    }

    /**
     * Создаем новый Конференц-зал.
     *
     * @param id the conference room ID
     */
    public void createConferenceRoom(String id) {
        if (conferenceRoomRepository.containsKey(id)) {
            throw new IllegalArgumentException("Conference room with id " + id + " already exists.");
        }

        conferenceRoomRepository.put(id, new ConferenceRoom(id));
    }

    /**
     * Добавляем рабочее место в Конференц-зал
     *
     * @param conferenceRoomId the conference room ID
     * @param workspaceId the workspace ID
     */
    public void addWorkspaceToConferenceRoom(String conferenceRoomId, String workspaceId) {
        ConferenceRoom conferenceRoom = conferenceRoomRepository.get(conferenceRoomId);

        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }

        conferenceRoom.addWorkspace(new Workspace(workspaceId));
    }

    /**
     * Бронируем рабочее место для пользователя в Конференц-зале/
     *
     * @param conferenceRoomId the conference room ID
     * @param workspaceId the workspace ID
     * @param userId the user ID
     */
    public void bookWorkspace(String conferenceRoomId, String workspaceId, String userId, LocalDateTime bookingTime) {
        ConferenceRoom conferenceRoom = conferenceRoomRepository.get(conferenceRoomId);
        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }

        if (!conferenceRoom.isBookingTimeAvailable(bookingTime)) {
            throw new IllegalArgumentException("Time slot is not available for conference room with id " + conferenceRoomId);
        }

        Workspace workspace = conferenceRoom.getWorkspace(workspaceId);

        if (workspace == null) {
            throw new IllegalArgumentException("Workspace with id " + workspaceId + " not found.");
        }

        workspace.book(userId, bookingTime);
    }

    /**
     * Бронируем все рабочие места в Конференц-зале
     *
     * @param conferenceRoomId the conference room ID
     * @param userId the user ID
     * @param bookingTime the booking time
     */
    public void bookAllWorkspaces(String conferenceRoomId, String userId, LocalDateTime bookingTime) {
        ConferenceRoom room = conferenceRoomRepository.get(conferenceRoomId);
        if (room == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        room.bookAllWorkspaces(userId, bookingTime);
    }

    /**
     * Вносим изменение в ID уже существующего Конференц-зала
     *
     * @param oldId the old conference room ID
     * @param newId the new conference room ID
     */
    public void updateConferenceRoom(String oldId, String newId) {
        if (!conferenceRoomRepository.containsKey(oldId)) {
            throw new IllegalArgumentException("Conference room with id " + oldId + " not found.");
        }
        if (conferenceRoomRepository.containsKey(newId)) {
            throw new IllegalArgumentException("Conference room with id " + newId + " already exists.");
        }

        ConferenceRoom conferenceRoom = conferenceRoomRepository.remove(oldId);
        conferenceRoom.setId(newId);
        conferenceRoomRepository.put(newId, conferenceRoom);
    }

    /**
     * Удаляем уже существующий Конференц-зал
     *
     * @param id the conference room ID
     */
    public void deleteConferenceRoom(String id) {
        if (!conferenceRoomRepository.containsKey(id)) {
            throw new IllegalArgumentException("Conference room with id " + id + " not found.");
        }
        conferenceRoomRepository.remove(id);
    }

    /**
     * Берем самый свободный КОнференц-зал
     *
     * @return the conference room with the most available workspaces
     */
    public ConferenceRoom getConferenceRoomWithMostAvailableWorkspaces() {
        ConferenceRoom availableRoom = null;
        int maxAvailable = 0;

        for (ConferenceRoom conferenceRoom : conferenceRoomRepository.values()) {
            int available = conferenceRoom.getAvailableWorkspaceCount();

            if (available > maxAvailable) {
                maxAvailable = available;
                availableRoom = conferenceRoom;
            }
        }
        return availableRoom;
    }

    /**
     * Возвращаем репозиторий Конференц-зал'ов
     *
     * @return the conference room repository
     */
    public Map<String, ConferenceRoom> getConferenceRoomRepository() {
        return conferenceRoomRepository;
    }

    /**
     * Возвращаем доступные слоты рабочих мест в Конференц-зале
     *
     * @param conferenceRoomId the conference room ID
     * @param date the date
     * @return the list of available slots in conference room
     */
    public List<String> getAvailableSlots(String conferenceRoomId, LocalDate date) {
        ConferenceRoom conferenceRoom = conferenceRoomRepository.get(conferenceRoomId);
        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        return conferenceRoom.getAvailableSlots(date.atStartOfDay());
    }

    /**
     * Отмена бронирования рабочих мест в конференц-залах
     *
     * @param conferenceRoomId
     * @param workspaceId
     */
    public void cancelBookingForWorkspace(String conferenceRoomId, String workspaceId) {
        ConferenceRoom conferenceRoom = conferenceRoomRepository.get(conferenceRoomId);
        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        conferenceRoom.cancelBookingForWorkspace(workspaceId);
    }

    /**
     * Отмена бронирования Конференц-залов
     *
     * @param conferenceRoomId
     */
    public void cancelBookingForAllWorkspaces(String conferenceRoomId) {
        ConferenceRoom conferenceRoom = conferenceRoomRepository.get(conferenceRoomId);
        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        conferenceRoom.cancelBookingForAllWorkspaces();
    }
}
