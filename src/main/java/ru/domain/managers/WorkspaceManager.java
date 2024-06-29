package ru.domain.managers;

import ru.domain.entities.Workspace;
import ru.domain.entities.ConferenceRoom;

import java.time.LocalDateTime;

/**
 * Определяем класс для управления раб
 */
public class WorkspaceManager {
    private ConferenceRoomManager conferenceRoomManager;;

    public WorkspaceManager(ConferenceRoomManager conferenceRoomManager) {
        this.conferenceRoomManager = conferenceRoomManager;
    }

    /**
     * Добавляем рабочее место в Конференц-зал
     *
     * @param conferenceRoomId the conference room ID
     * @param workspaceId the workspace ID
     */
    public void addWorkspace(String conferenceRoomId, String workspaceId) {
        ConferenceRoom conferenceRoom = conferenceRoomManager.getConferenceRoom(conferenceRoomId);

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
        ConferenceRoom conferenceRoom = conferenceRoomManager.getConferenceRoom(conferenceRoomId);
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
        ConferenceRoom room = conferenceRoomManager.getConferenceRoom(conferenceRoomId);
        if (room == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        room.bookAllWorkspaces(userId, bookingTime);
    }

    /**
     * Отмена бронирования рабочих мест в конференц-залах
     *
     * @param conferenceRoomId the conference room ID
     * @param workspaceId the workspace ID
     */
    public void cancelBookingWorkspace(String conferenceRoomId, String workspaceId) {
        ConferenceRoom conferenceRoom = conferenceRoomManager.getConferenceRoom(conferenceRoomId);
        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        conferenceRoom.cancelBookingForWorkspace(workspaceId);
    }

    /**
     * Отмена бронирования Конференц-залов
     *
     * @param conferenceRoomId the conference room ID
     */
    public void cancelBookingForAllWorkspaces(String conferenceRoomId) {
        ConferenceRoom conferenceRoom = conferenceRoomManager.getConferenceRoom(conferenceRoomId);
        if (conferenceRoom == null) {
            throw new IllegalArgumentException("Conference room with id " + conferenceRoomId + " not found.");
        }
        conferenceRoom.cancelBookingForAllWorkspaces();
    }
}
