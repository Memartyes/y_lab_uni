package ru.domain.handlers;

import ru.domain.config.WorkspaceConfig;
import ru.domain.entities.Booking;
import ru.domain.entities.Workspace;
import ru.domain.managers.ConferenceRoomManager;
import ru.domain.managers.WorkspaceManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Класс для обработки операций с рабочими местами
 */
public class WorkspaceHandler {
    private final WorkspaceManager workspaceManager;
    private final ConferenceRoomManager conferenceRoomManager;

    public WorkspaceHandler(WorkspaceManager workspaceManager, ConferenceRoomManager conferenceRoomManager) {
        this.workspaceManager = workspaceManager;
        this.conferenceRoomManager = conferenceRoomManager;
    }

    /**
     * Обработка добавления рабочих мест в Конференц-зал.
     */
    public void handleAddWorkspace(int conferenceRoomId, String workspaceName) {
        if (workspaceName.isEmpty()) {
            System.out.println("Workspace Name cannot be empty.");
            return;
        }

        try {
            Workspace workspace = new Workspace(workspaceName);
            conferenceRoomManager.addWorkspaceToConferenceRoom(conferenceRoomId, workspace);
            System.out.println("Workspace " + workspaceName + " added successfully to conference room ID: " + conferenceRoomId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на бронирование рабочих мест
     */
    public void handleBookWorkspace(int workspaceId, String userName, String bookingTimeInput) {
        if (userName.isEmpty() || bookingTimeInput.isEmpty()) {
            System.out.println("All fields are required");
            return;
        }

        try {
            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            workspaceManager.bookWorkspace(workspaceId, userName, bookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
            System.out.println("Workspace booked successfully.");
        } catch (DateTimeParseException e) {
            System.out.println("Error: invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Отмена бронирования рабочего места
     */
    public void handleCancelBookingWorkspace(int bookingId) {
        try {
            workspaceManager.cancelBooking(bookingId);
            System.out.println("Booking cancelled for booking ID: " + bookingId);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Отмена бронирования всех рабочих мест в конференц-зале
     */
    public void handleCancelBookingForAllWorkspaces(int conferenceRoomId) {
        try {
            List<Workspace> workspaces = conferenceRoomManager.findWorkspacesByConferenceRoomId(conferenceRoomId);
            for (Workspace workspace : workspaces) {
                List<Booking> bookings = workspaceManager.findBookingsByWorkspaceId(workspace.getId());
                for (Booking booking : bookings) {
                    workspaceManager.cancelBooking(booking.getId());
                }
            }
            System.out.println("All bookings cancelled for conference room ID: " + conferenceRoomId);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Чтение даты и времени бронирования с консоли
     *
     * @return LocalDateTime объект, представляющий дату и время бронирования
     */
    public LocalDateTime handleReadBookingDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
            return null;
        }
    }
}