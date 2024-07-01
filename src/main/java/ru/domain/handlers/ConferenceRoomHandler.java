package ru.domain.handlers;

import ru.domain.config.WorkspaceConfig;
import ru.domain.io.ConsoleConferenceRoomInput;
import ru.domain.io.ConsoleConferenceRoomOutput;
import ru.domain.managers.ConferenceRoomManager;
import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.Workspace;
import ru.domain.managers.WorkspaceManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Определячем класс для обработки логики Конференц-зала.
 */
public class ConferenceRoomHandler {
    private ConsoleConferenceRoomInput conferenceRoomInput;
    private ConsoleConferenceRoomOutput conferenceRoomOutput;
    private ConferenceRoomManager conferenceRoomManager;
    private WorkspaceManager workspaceManager;

    /**
     *  Конструктор для создания класса обработки запросов Конференц-залов при работе с консолью.
     *
     * @param conferenceRoomInput the conference room input
     * @param conferenceRoomOutput the conference room output
     * @param conferenceRoomManager the conference room manager
     * @param workspaceManager the workspace manager
     */
    public ConferenceRoomHandler(ConsoleConferenceRoomInput conferenceRoomInput, ConsoleConferenceRoomOutput conferenceRoomOutput, ConferenceRoomManager conferenceRoomManager, WorkspaceManager workspaceManager) {
        this.conferenceRoomInput = conferenceRoomInput;
        this.conferenceRoomOutput = conferenceRoomOutput;
        this.conferenceRoomManager = conferenceRoomManager;
        this.workspaceManager = workspaceManager;
    }

    /**
     * Обработка запроса на создание нового Конференц-зала.
     */
    public void handleCreateConferenceRoom() {
        String id = conferenceRoomInput.readRoomId();

        try {
            conferenceRoomManager.addConferenceRoom(id);
            conferenceRoomOutput.printMessage("Conference Room created successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.printMessage("Error creating conference room: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на смотр всех Конференц-зал'ов и доступных рабочих мест.
     */
    public void handleViewConferenceRooms() {
        conferenceRoomOutput.printMessage("Available Conference Rooms:");

        for (ConferenceRoom room : conferenceRoomManager.getConferenceRooms().values()) {
            conferenceRoomOutput.printMessage("Conference Room ID: " + room.getName() + "\nAvailable Workspaces: " + room.getAvailableWorkspaceCount());

            for (Workspace workspace : room.getWorkspaces()) {
                if (workspace.isBooked()) {
                    conferenceRoomOutput.printMessage(" ID: " + workspace.getName() + " - Booked by User ID: " + workspace.getBookedBy()  + " from " + workspace.getBookingTime() + " to " + workspace.getBookingEndTime());
                } else {
                    conferenceRoomOutput.printMessage(" ID: " + workspace.getName() + " - Available to book");
                }
            }
        }
    }

    /**
     * Обрабатываем запрос на изменение ID Конференц-зала
     */
    public void handleUpdateConferenceRoom() {
        String oldRoomId = conferenceRoomInput.readRoomId();
        conferenceRoomOutput.printMessage("Enter the new Conference Room ID:");
        String newRoomId = conferenceRoomInput.readRoomId();

        try {
            conferenceRoomManager.updateConferenceRoomName(oldRoomId, newRoomId);
            conferenceRoomOutput.printMessage("Conference Room updated successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.printMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на удаление Конференц-зала
     */
    public void handleDeleteConferenceRoom() {
        String id = conferenceRoomInput.readRoomId();

        try {
            conferenceRoomManager.deleteConferenceRoom(id);
            conferenceRoomOutput.printMessage("Conference Room deleted successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.printMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на показ доступных слотов в Конференц-залах
     */
    public void handleViewAvailableSlots() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        String dateInput = conferenceRoomInput.readBookingDate();

        try {
            LocalDate date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<String> availableSlots = conferenceRoomManager.getAvailableSlots(conferenceRoomId, date);
            conferenceRoomOutput.printMessage("Available slots on " + date + " are:");
            for (String slot : availableSlots) {
                conferenceRoomOutput.printMessage("  " + slot);
            }
        } catch (DateTimeParseException e) {
            conferenceRoomOutput.printMessage("Error: Invalid date format. Please use 'yyyy-MM-dd'.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.printMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для обработки запроса на бронирование всего Конференц-зала
     */
    public void handleBookConferenceRoom() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        String userId = conferenceRoomInput.readUserId();
        String bookingTimeInput = conferenceRoomInput.readBookingDateTime();

        LocalDateTime bookingTime;
        try {
            bookingTime = LocalDateTime.parse(bookingTimeInput);
        } catch (Exception e) {
            conferenceRoomOutput.printMessage("Invalid date time format");
            return;
        }
        try {
            workspaceManager.bookAllWorkspaces(conferenceRoomId, userId, bookingTime);
            conferenceRoomOutput.printMessage("Conference room booked successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.printMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для обработки запрооса на бронирования Конференц-зала на весь рабочий день.
     */
    public void handleBookConferenceRoomForWholeDay() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        String userId = conferenceRoomInput.readUserId();
        String bookingDate = conferenceRoomInput.readBookingDate();

        LocalDate date;
        try {
            date = LocalDate.parse(bookingDate);
        } catch (Exception e) {
            conferenceRoomOutput.printMessage("Invalid date format.");
            return;
        }

        int startHour = WorkspaceConfig.START_HOUR.getValue();
        int endHour = WorkspaceConfig.END_HOUR.getValue();
        int bookingDuration = WorkspaceConfig.BOOKING_DURATION_HOURS.getValue();

        LocalDateTime bookingTime = date.atTime(startHour, 0);
        while (bookingTime.getHour() < endHour) {
            try {
                workspaceManager.bookAllWorkspaces(conferenceRoomId, userId, bookingTime);
                bookingTime = bookingTime.plusHours(bookingDuration);
            } catch (IllegalArgumentException e) {
                conferenceRoomOutput.printMessage("Error: " + e.getMessage());
                return;
            }
        }
        conferenceRoomOutput.printMessage("Conference room booked for the whole day successfully.");
    }

    /**
     * Обработка запроса на отмену бронирования рабочих мест
     */
    public void handleCancelWorkspaceBooking() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();
        String workspaceId = conferenceRoomInput.readWorkspaceId();

        try {
            workspaceManager.cancelBookingWorkspace(conferenceRoomId, workspaceId);
            conferenceRoomOutput.printMessage("Workspace canceled successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.printMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Обработка запроса на отмену бронирования Конференц-зала
     */
    public void handleCancelConferenceRoomBooking() {
        String conferenceRoomId = conferenceRoomInput.readRoomId();

        try {
            workspaceManager.cancelBookingForAllWorkspaces(conferenceRoomId);
            conferenceRoomOutput.printMessage("Conference Room canceled successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.printMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Фильтруем конференц залы по выбору пользователя.
     * (1 - Date, 2 - User, 3 - Available workspaces).
     */
    public void handleFilterBooking() {
        String choice = conferenceRoomInput.readBookingFilterChoice();

        switch (choice) {
            case "1":
                filterByDate();
                break;
            case "2":
                filterByUser();
                break;
            case "3":
                filterByAvailableWorkspaces();
            default:
                conferenceRoomOutput.printMessage("Invalid option.");
        }
    }

    /**
     * Фильтруем Конференц-залы по дате.
     */
    private void filterByDate() {
        String dateInput = conferenceRoomInput.readBookingDate();
        LocalDate date = LocalDate.parse(dateInput);
        List<String> results = conferenceRoomManager.filterByDate(date);
        conferenceRoomOutput.printList(results);
    }

    /**
     * Фильтруем Конференц-залы по пользователю.
     */
    private void filterByUser() {
        String userId = conferenceRoomInput.readUserId();
        List<String> results = conferenceRoomManager.filterByUser(userId);
        conferenceRoomOutput.printList(results);
    }

    /**
     * Фильтруем Конференц-залы по доступным рабочим местам.
     */
    private void filterByAvailableWorkspaces() {
        List<String> results = conferenceRoomManager.filterByAvailableWorkspaces();
        conferenceRoomOutput.printList(results);
    }
}
