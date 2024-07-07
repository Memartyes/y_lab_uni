package ru.domain.handlers;

import ru.domain.config.WorkspaceConfig;
import ru.domain.entities.Booking;
import ru.domain.entities.Workspace;
import ru.domain.io.in.UserInput;
import ru.domain.io.out.UserOutput;
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
    private final UserInput userInput;
    private final UserOutput userOutput;
    private final WorkspaceManager workspaceManager;
    private final ConferenceRoomManager conferenceRoomManager;

    public WorkspaceHandler(UserInput userInput, UserOutput userOutput, WorkspaceManager workspaceManager, ConferenceRoomManager conferenceRoomManager) {
        this.userInput = userInput;
        this.userOutput = userOutput;
        this.workspaceManager = workspaceManager;
        this.conferenceRoomManager = conferenceRoomManager;
    }

    /**
     * Обработка добавления рабочих мест в Конференц-зал.
     */
    public void handleAddWorkspace() {
        int conferenceRoomId = Integer.parseInt(userInput.readLine("Enter Conference Room ID:"));
        String workspaceName = userInput.readLine("Enter Workspace Name:");

        if (workspaceName.isEmpty()) {
            userOutput.println("Workspace Name cannot be empty.");
            return;
        }

        try {
            Workspace workspace = new Workspace(workspaceName);
            conferenceRoomManager.addWorkspaceToConferenceRoom(conferenceRoomId, workspace);
            userOutput.println("Workspace " + workspaceName + " added successfully to conference room ID: " + conferenceRoomId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            userOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на бронирование рабочих мест
     */
    public void handleBookWorkspace() {
        int workspaceId = Integer.parseInt(userInput.readLine("Enter Workspace ID:"));
        String userName = userInput.readLine("Enter Username:");
        String bookingTimeInput = userInput.readLine("Enter Booking Time as 'yyyy-MM-dd HH:mm':");

        if (userName.isEmpty() || bookingTimeInput.isEmpty()) {
            userOutput.println("All fields are required");
            return;
        }

        try {
            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            workspaceManager.bookWorkspace(workspaceId, userName, bookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
            userOutput.println("Workspace booked successfully.");
        } catch (DateTimeParseException e) {
            userOutput.println("Error: invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            userOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Отмена бронирования рабочего места
     */
    public void handleCancelBookingWorkspace() {
        int bookingId = Integer.parseInt(userInput.readLine("Enter Booking ID:"));

        try {
            workspaceManager.cancelBooking(bookingId);
            userOutput.println("Booking cancelled for booking ID: " + bookingId);
        } catch (IllegalArgumentException e) {
            userOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Отмена бронирования всех рабочих мест в конференц-зале
     */
    public void handleCancelBookingForAllWorkspaces() {
        int conferenceRoomId = Integer.parseInt(userInput.readLine("Enter Conference Room ID:"));

        try {
            List<Workspace> workspaces = conferenceRoomManager.findWorkspacesByConferenceRoomId(conferenceRoomId);
            for (Workspace workspace : workspaces) {
                List<Booking> bookings = workspaceManager.findBookingsByWorkspaceId(workspace.getId());
                for (Booking booking : bookings) {
                    workspaceManager.cancelBooking(booking.getId());
                }
            }
            userOutput.println("All bookings cancelled for conference room ID: " + conferenceRoomId);
        } catch (IllegalArgumentException e) {
            userOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Чтение даты и времени бронирования с консоли
     *
     * @return LocalDateTime объект, представляющий дату и время бронирования
     */
    public LocalDateTime handleReadBookingDateTime() {
        String dateTimeString = userInput.readLine("Enter Booking Time 'yyyy-MM-dd HH:mm':");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            userOutput.println("Error: Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
            return null;
        }
    }
}