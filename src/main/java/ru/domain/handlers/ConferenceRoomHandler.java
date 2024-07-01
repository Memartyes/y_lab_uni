package ru.domain.handlers;

import ru.domain.config.WorkspaceConfig;
import ru.domain.io.in.UserInput;
import ru.domain.io.out.UserOutput;
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
    private UserInput conferenceRoomInput;
    private UserOutput conferenceRoomOutput;
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
    public ConferenceRoomHandler(UserInput conferenceRoomInput, UserOutput conferenceRoomOutput, ConferenceRoomManager conferenceRoomManager, WorkspaceManager workspaceManager) {
        this.conferenceRoomInput = conferenceRoomInput;
        this.conferenceRoomOutput = conferenceRoomOutput;
        this.conferenceRoomManager = conferenceRoomManager;
        this.workspaceManager = workspaceManager;
    }

    /**
     * Парсинг даты из строки.
     *
     * @param dateStr the date string
     * @return LocalDate the local date time
     */
    private LocalDate parseDate(String dateStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * Парсинг даты и времени из строки.
     *
     * @param dateTimeStr the date time string
     * @return LocalDateTime the local date time
     */
    private LocalDateTime parseDateTime(String dateTimeStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    /**
     * Обработка запроса на создание нового Конференц-зала.
     */
    public void handleCreateConferenceRoom() {
        String roomName = conferenceRoomInput.readLine("Enter Conference Room Name:");

        try {
            conferenceRoomManager.addConferenceRoom(roomName);
            conferenceRoomOutput.println("Conference Room created successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.println("Error creating conference room: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на смотр всех Конференц-зал'ов и доступных рабочих мест.
     */
    public void handleViewConferenceRooms() {
        conferenceRoomOutput.println("Available Conference Rooms:");

        for (ConferenceRoom room : conferenceRoomManager.getConferenceRooms().values()) {
            conferenceRoomOutput.println("Conference Room name: " + room.getName() + "\nAvailable Workspaces: " + room.getAvailableWorkspaceCount());

            for (Workspace workspace : room.getWorkspaces()) {
                if (workspace.isBooked()) {
                    conferenceRoomOutput.println(" Name: " + workspace.getName() + " - Booked by Username: " + workspace.getBookedBy()  + " from " + workspace.getBookingTime() + " to " + workspace.getBookingEndTime());
                } else {
                    conferenceRoomOutput.println(" Name: " + workspace.getName() + " - Available to book");
                }
            }
        }
    }

    /**
     * Обрабатываем запрос на изменение ID Конференц-зала
     */
    public void handleUpdateConferenceRoom() {
        String oldRoomName = conferenceRoomInput.readLine("Enter Current Conference Room Name:");
        String newRoomName = conferenceRoomInput.readLine("Enter New Conference Room Name:");

        try {
            conferenceRoomManager.updateConferenceRoomName(oldRoomName, newRoomName);
            conferenceRoomOutput.println("Conference Room updated successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на удаление Конференц-зала
     */
    public void handleDeleteConferenceRoom() {
        String conferenceRoomName = conferenceRoomInput.readLine("Enter Conference Room Name:");

        try {
            conferenceRoomManager.deleteConferenceRoom(conferenceRoomName);
            conferenceRoomOutput.println("Conference Room " + conferenceRoomName + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на показ доступных слотов в Конференц-залах
     */
    public void handleViewAvailableSlots() {
        String conferenceRoomName = conferenceRoomInput.readLine("Enter Conference Room Name:");
        String dateInput = conferenceRoomInput.readLine("Enter Booking Date (yyyy-MM-dd):");

        try {
            LocalDate date = parseDate(dateInput);
            List<String> availableSlots = conferenceRoomManager.getAvailableSlots(conferenceRoomName, date);
            conferenceRoomOutput.println("Available slots on " + date + " are:");
            for (String slot : availableSlots) {
                conferenceRoomOutput.println("  " + slot);
            }
        } catch (DateTimeParseException e) {
            conferenceRoomOutput.println("Error: Invalid date format. Please use 'yyyy-MM-dd'.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для обработки запроса на бронирование всего Конференц-зала
     */
    public void handleBookConferenceRoom() {
        String conferenceRoomName = conferenceRoomInput.readLine("Enter Conference Room Name:");
        String userName = conferenceRoomInput.readLine("Enter User Name:");
        String bookingTimeInput = conferenceRoomInput.readLine("Enter Booking Date and Time (yyyy-MM-dd HH:mm):");

        try {
            LocalDateTime bookingTime = parseDateTime(bookingTimeInput);
            conferenceRoomManager.bookWholeConferenceRoom(conferenceRoomName, userName, bookingTime);
            conferenceRoomOutput.println("Conference room booked successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для обработки запрооса на бронирования Конференц-зала на весь рабочий день.
     */
    public void handleBookConferenceRoomForWholeDay() {
        String conferenceRoomName = conferenceRoomInput.readLine("Enter Conference Room Name:");
        String userName = conferenceRoomInput.readLine("Enter User Name:");
        String bookingDate = conferenceRoomInput.readLine("Enter Booking Date (yyyy-MM-dd):");

        LocalDate date;
        try {
            date = parseDate(bookingDate);
        } catch (Exception e) {
            conferenceRoomOutput.println("Invalid date format.");
            return;
        }

        int startHour = WorkspaceConfig.START_HOUR.getValue();
        int endHour = WorkspaceConfig.END_HOUR.getValue();
        int bookingDuration = WorkspaceConfig.BOOKING_DURATION_HOURS.getValue();

        LocalDateTime bookingTime = date.atTime(startHour, 0);
        while (bookingTime.getHour() < endHour) {
            try {
                conferenceRoomManager.bookWholeConferenceRoom(conferenceRoomName, userName, bookingTime);
                bookingTime = bookingTime.plusHours(bookingDuration);
            } catch (IllegalArgumentException e) {
                conferenceRoomOutput.println("Error: " + e.getMessage());
                return;
            }
        }
        conferenceRoomOutput.println("Conference room booked for the whole day successfully.");
    }

    /**
     * Обработка запроса на отмену бронирования рабочих мест
     */
    public void handleCancelWorkspaceBooking() {
        String conferenceRoomName = conferenceRoomInput.readLine("Enter Conference Room Name:");
        String workspaceName = conferenceRoomInput.readLine("Enter Workspace Name:");

        try {
            conferenceRoomManager.cancelBookingForWorkspace(conferenceRoomName, workspaceName);
            conferenceRoomOutput.println("Workspace canceled successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обработка запроса на отмену бронирования Конференц-зала
     */
    public void handleCancelConferenceRoomBooking() {
        String conferenceRoomName = conferenceRoomInput.readLine("Enter Conference Room Name:");

        try {
            conferenceRoomManager.cancelBookingForAllWorkspaces(conferenceRoomName);
            conferenceRoomOutput.println("Conference Room booking canceled successfully.");
        } catch (IllegalArgumentException e) {
            conferenceRoomOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Фильтруем конференц залы по выбору пользователя.
     * (1 - Date, 2 - User, 3 - Available workspaces).
     */
    public void handleFilterBooking() {
        String choice = conferenceRoomInput.readLine("Filter conference rooms by:\n'1' - Date\n'2' - User\n'3' - Available Workspaces\nChoose your option:");

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
                conferenceRoomOutput.println("Invalid option.");
        }
    }

    /**
     * Фильтруем Конференц-залы по дате.
     */
    private void filterByDate() {
        String dateInput = conferenceRoomInput.readLine("Enter Booking Date (yyyy-MM-dd):");
        try {
            LocalDate date = parseDate(dateInput);
            List<String> results = conferenceRoomManager.filterByDate(date);
            conferenceRoomOutput.printList(results);
        } catch (DateTimeParseException e) {
            conferenceRoomOutput.println("Error: Invalid date format. Please use 'yyyy-MM-dd'.");
        }
    }

    /**
     * Фильтруем Конференц-залы по пользователю.
     */
    private void filterByUser() {
        String userId = conferenceRoomInput.readLine("Enter User Name:");
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
