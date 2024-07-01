package ru.domain.handlers;

import ru.domain.config.WorkspaceConfig;
import ru.domain.io.in.UserInput;
import ru.domain.io.out.UserOutput;
import ru.domain.managers.ConferenceRoomManager;
import ru.domain.managers.WorkspaceManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        String conferenceRoomName = userInput.readLine("Enter Conference Room Name:");
        String workspaceName = userInput.readLine("Enter Workspace Name");

        if (conferenceRoomName.isEmpty() || workspaceName.isEmpty()) {
            userOutput.println("Conference Room Name and Workspace Name cannot be empty.");
            return;
        }

        try {
            conferenceRoomManager.addWorkspaceToConferenceRoom(conferenceRoomName, workspaceName);
            userOutput.println("Workspace " + workspaceName + " added successfully to conference room: " + conferenceRoomName);
        } catch (IllegalArgumentException | IllegalStateException e) {
            userOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на бронирование рабочих мест
     */
    public void handleBookWorkspace() {
        String conferenceRoomName = userInput.readLine("Enter Conference Room Name:");
        String workspaceName = userInput.readLine("Enter Workspace Name:");
        String userName = userInput.readLine("Enter Username:");
        String bookingTimeInput = userInput.readLine("Enter Booking Time as 'yyyy-MM-dd HH:mm':");

        if (conferenceRoomName.isEmpty() || workspaceName.isEmpty() || userName.isEmpty() || bookingTimeInput.isEmpty()) {
            userOutput.println("All fields are required");
            return;
        }

        try {
            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            conferenceRoomManager.bookWorkspace(conferenceRoomName, workspaceName, userName, bookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
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
        String conferenceRoomName = userInput.readLine("Enter Conference Room Name:");
        String workspaceName = userInput.readLine("Enter Workspace Name:");

        if (conferenceRoomName.isEmpty() || workspaceName.isEmpty()) {
            userOutput.println("Conference Room Name and Workspace Name cannot be empty.");
        }

        try {
            conferenceRoomManager.cancelBookingForWorkspace(conferenceRoomName, workspaceName);
            userOutput.println("Booking cancel for workspace: " + workspaceName + " in conference room " + conferenceRoomName);
        } catch (IllegalArgumentException e) {
            userOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Отмена бронирования всех рабочих мест в конференц-зале
     */
    public void handleCancelBookingForAllWorkspaces() {
        String conferenceRoomName = userInput.readLine("Enter Conference Room Name:");

        if (conferenceRoomName.isEmpty()) {
            userOutput.println("Conference Room Name cannot be empty.");
            return;
        }

        try {
            conferenceRoomManager.cancelBookingForAllWorkspaces(conferenceRoomName);
            userOutput.println("All bookings cancelled for conference room: " + conferenceRoomName);
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