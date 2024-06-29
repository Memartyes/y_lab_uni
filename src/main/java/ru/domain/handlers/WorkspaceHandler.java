package ru.domain.handlers;

import ru.domain.io.ConsoleConferenceRoomInput;
import ru.domain.io.ConsoleOutput;
import ru.domain.managers.WorkspaceManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс для обработки операций с рабочими местами
 */
public class WorkspaceHandler {
    private final ConsoleConferenceRoomInput conferenceRoomInput;
    private final ConsoleOutput output;
    private final WorkspaceManager workspaceManager;

    public WorkspaceHandler(ConsoleConferenceRoomInput conferenceRoomInput, ConsoleOutput output, WorkspaceManager workspaceManager) {
        this.conferenceRoomInput = conferenceRoomInput;
        this.output = output;
        this.workspaceManager = workspaceManager;
    }

    /**
     * Обработка добавления рабочих мест в Конференц-зал.
     */
    public void handleAddWorkspace() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        String workspaceId = conferenceRoomInput.readWorkspaceId();

        try {
            workspaceManager.addWorkspace(conferenceRoomId, workspaceId);
            output.println("Workspace added successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            output.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на бронирование рабочих мест
     */
    public void handleBookWorkspace() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        String workspaceId = conferenceRoomInput.readWorkspaceId();
        String userId = conferenceRoomInput.readUserId();
        String bookingTimeInput = conferenceRoomInput.readBookingDateTime();

        try {
            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            workspaceManager.bookWorkspace(conferenceRoomId, workspaceId, userId, bookingTime);
            output.println("Workspace booked successfully.");
        } catch (DateTimeParseException e) {
            output.println("Error: Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            output.println("Error: " + e.getMessage());
        }
    }

    /**
     * Бронирование всех рабочих мест
     */
    public void handleBookAllWorkspaces() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        String userId = conferenceRoomInput.readUserId();
        LocalDateTime bookingTime = handleReadBookingDateTime();
        try {
            workspaceManager.bookAllWorkspaces(conferenceRoomId, userId, bookingTime);
            output.println("All workspaces booked by user " + userId + " in conference room " + conferenceRoomId + " at " + bookingTime);
        } catch (IllegalArgumentException e) {
            output.println(e.getMessage());
        }
    }

    /**
     * Отмена бронирования рабочего места
     */
    public void handleCancelBookingWorkspace() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        String workspaceId = conferenceRoomInput.readWorkspaceId();
        try {
            workspaceManager.cancelBookingWorkspace(conferenceRoomId, workspaceId);
            output.println("Booking canceled for workspace: " + workspaceId + " in conference room " + conferenceRoomId);
        } catch (IllegalArgumentException e) {
            output.println(e.getMessage());
        }
    }

    /**
     * Отмена бронирования всех рабочих мест в конференц-зале
     */
    public void handleCancelBookingForAllWorkspaces() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        try {
            workspaceManager.cancelBookingForAllWorkspaces(conferenceRoomId);
            output.println("All bookings canceled for conference room: " + conferenceRoomId);
        } catch (IllegalArgumentException e) {
            output.println(e.getMessage());
        }
    }

    /**
     * Чтение даты и времени бронирования с консоли
     *
     * @return LocalDateTime объект, представляющий дату и время бронирования
     */
    private LocalDateTime handleReadBookingDateTime() {
        String dateTimeString = conferenceRoomInput.readBookingDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}