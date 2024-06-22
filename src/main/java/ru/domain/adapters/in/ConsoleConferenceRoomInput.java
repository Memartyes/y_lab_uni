package ru.domain.adapters.in;

import ru.domain.adapters.out.ConsoleOutput;
import ru.domain.usecases.ConferenceRoomManager;
import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.Workspace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Определячем класс для обработки запросов с консоли,
 * связанных с Конференц-залами.
 */
public class ConsoleConferenceRoomInput {
    private ConsoleInput input;
    private ConsoleOutput output;
    private ConferenceRoomManager conferenceRoomManager;

    /**
     * Конструктор для создания класса обработки запросов Конференц-залов при работе с консолью.
     *
     * @param input the console input
     * @param output the console output
     * @param conferenceRoomManager the conference room manager
     */
    public ConsoleConferenceRoomInput(ConsoleInput input, ConsoleOutput output, ConferenceRoomManager conferenceRoomManager) {
        this.input = input;
        this.output = output;
        this.conferenceRoomManager = conferenceRoomManager;
    }

    /**
     * Обработка запроса на создание нового Конференц-зала.
     */
    public void handleCreateConferenceRoom() {
        output.println("Enter Conference Room ID:");
        String id = input.readLine();

        try {
            conferenceRoomManager.createConferenceRoom(id);
            output.println("Conference Room created successfully.");
        } catch (IllegalArgumentException e) {
            output.println("Error creating conference room: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на смотр всех Конференц-зал'ов и доступных рабочих мест.
     */
    public void handleViewConferenceRooms() {
        output.println("Available Conference Rooms:");
        for (ConferenceRoom room : conferenceRoomManager.getConferenceRoomRepository().values()) {
            output.println("Conference Room ID: " + room.getId() + "\nAvailable Workspaces: " + room.getAvailableWorkspaceCount());

            for (Workspace workspace : room.getWorkspaces()) {
                if (workspace.isBooked()) {
                    output.println(" ID: " + workspace.getId() + " - Booked by User ID: " + workspace.getBookedBy()  + " from " + workspace.getBookingTime() + " to " + workspace.getBookingEndTime());
                } else {
                    output.println(" ID: " + workspace.getId() + " - Available to book");
                }
            }
        }
    }

    //TODO: manage that if the 'IllegalStateException' throws, offer the user other alternative workspace with the most available slots
    /**
     * Обработка добавления рабочих мест в Конференц-зал.
     */
    public void handleAddWorkspace() {
        output.println("Enter Conference Room ID:");
        String conferenceRoomId = input.readLine();
        output.println("Enter Workspace ID:");
        String workspaceId = input.readLine();

        try {
            conferenceRoomManager.addWorkspaceToConferenceRoom(conferenceRoomId, workspaceId);
            output.println("Workspace added successfully.");
        } catch (IllegalArgumentException e) {
            output.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            output.println("Error: " + e.getMessage()); //FIXME: manage the exception logic here
            ConferenceRoom alternativeRoom = conferenceRoomManager.getConferenceRoomWithMostAvailableWorkspaces();

            if (alternativeRoom != null) {
                int availableWorkspaces = alternativeRoom.getAvailableWorkspaceCount();
                output.println("Alternative Conference Room: " + alternativeRoom.getId() + " with " + availableWorkspaces + " available workspaces");
            } else {
                output.println("No available conference rooms found");
            }
        }
    }

    /**
     * Обрабатываем запрос на бронирование рабочих мест
     */
    public void handleBookWorkspace() {
        output.println("Enter Conference Room ID:");
        String conferenceRoomId = input.readLine();
        output.println("Enter Workspace ID:");
        String workspaceId = input.readLine();
        output.println("Enter User ID:");
        String userId = input.readLine();
        output.println("Enter Booking Time (yyyy-MM-dd HH:mm):");
        String bookingTimeInput = input.readLine();

        try {
            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            conferenceRoomManager.bookWorkspace(conferenceRoomId, workspaceId, userId, bookingTime);
            output.println("Workspace booked successfully.");
        } catch (DateTimeParseException e) {
            output.println("Error: Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            output.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на изменение ID Конференц-зала
     */
    public void handleUpdateConferenceRoom() {
        output.println("Enter the old Conference Room ID:");
        String oldId = input.readLine();
        output.println("Enter the new Conference Room ID:");
        String newId = input.readLine();

        try {
            conferenceRoomManager.updateConferenceRoom(oldId, newId);
            output.println("Conference Room updated successfully.");
        } catch (IllegalArgumentException e) {
            output.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на удаление Конференц-зала
     */
    public void handleDeleteConferenceRoom() {
        output.println("Enter Conference Room ID:");
        String id = input.readLine();

        try {
            conferenceRoomManager.deleteConferenceRoom(id);
            output.println("Conference Room deleted successfully.");
        } catch (IllegalArgumentException e) {
            output.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на показ доступных слотов в Конференц-залах
     */
    public void handleViewAvailableSlots() {
        output.println("Enter Conference Room ID:");
        String conferenceRoomId = input.readLine();
        output.println("Enter Date (yyyy-MM-dd)");
        String dateInput = input.readLine();

        try {
            LocalDate date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<String> availableSlots = conferenceRoomManager.getAvailableSlots(conferenceRoomId, date);
            output.println("Available slots on " + date + " are:");
            for (String slot : availableSlots) {
                output.println("  " + slot);
            }
        } catch (DateTimeParseException e) {
            output.println("Error: Invalid date format. Please use 'yyyy-MM-dd'.");
        } catch (IllegalArgumentException e) {
            output.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для обработки запроса на бронирование всего Конференц-зала
     */
    public void handleBookConferenceRoom() {
        output.println("Enter Conference Room ID:");
        String conferenceRoomId = input.readLine();
        output.println("Enter User ID:");
        String userId = input.readLine();
        output.println("Enter Booking Time (yyyy-MM-dd HH:mm):");
        String bookingTimeInput = input.readLine();

        try {
            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            conferenceRoomManager.bookAllWorkspaces(conferenceRoomId, userId, bookingTime);
            output.println("Conference Room booked successfully.");
        } catch (DateTimeParseException e) {
            output.println("Error: Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            output.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обработка запроса на отмену бронирования рабочих мест
     */
    public void handleCancelWorkspaceBooking() {
        output.println("Enter Conference Room ID:");
        String conferenceRoomId = input.readLine();
        output.println("Enter Workspace ID:");
        String workspaceId = input.readLine();

        try {
            conferenceRoomManager.cancelBookingForWorkspace(conferenceRoomId, workspaceId);
            output.println("Workspace canceled successfully.");
        } catch (IllegalArgumentException e) {
            output.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обработка запроса на отмену бронирования Конференц-зала
     */
    public void handleCancelConferenceRoomBooking() {
        output.println("Enter Conference Room ID:");
        String conferenceRoomId = input.readLine();

        try {
            conferenceRoomManager.cancelBookingForAllWorkspaces(conferenceRoomId);
            output.println("Conference Room canceled successfully.");
        } catch (IllegalArgumentException e) {
            output.println("Error: " + e.getMessage());
        }
    }
}
