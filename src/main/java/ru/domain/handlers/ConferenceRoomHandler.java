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
    private final UserInput userInput;
    private final UserOutput userOutput;
    private final ConferenceRoomManager conferenceRoomManager;
    private final WorkspaceManager workspaceManager;

    public ConferenceRoomHandler(UserInput userInput, UserOutput userOutput, ConferenceRoomManager conferenceRoomManager, WorkspaceManager workspaceManager) {
        this.userInput = userInput;
        this.userOutput = userOutput;
        this.conferenceRoomManager = conferenceRoomManager;
        this.workspaceManager = workspaceManager;
    }

    /**
     * Создаем новый конференц-зал.
     */
    public void createConferenceRoom() {
        userOutput.println("Enter conference room name:");
        String name = userInput.readLine();
        userOutput.println("Enter conference room capacity:");
        int capacity = Integer.parseInt(userInput.readLine());

        ConferenceRoom conferenceRoom = new ConferenceRoom(name, capacity);
        conferenceRoomManager.addConferenceRoom(conferenceRoom);

        userOutput.println("Conference room " + name + " created with capacity " + capacity);
    }

    /**
     * Отображаем все конференц-залы.
     */
    public void viewConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = conferenceRoomManager.findAllConferenceRooms();
        if (conferenceRooms.isEmpty()) {
            userOutput.println("No conference rooms available.");
        } else {
            userOutput.println("Available conference rooms:");
            for (ConferenceRoom conferenceRoom : conferenceRooms) {
                userOutput.println("ID: " + conferenceRoom.getId() + ", Name: " + conferenceRoom.getName() + ", Capacity: " + conferenceRoom.getCapacity());
                List<Workspace> workspaces = conferenceRoomManager.findWorkspacesByConferenceRoomId(conferenceRoom.getId());
                for (Workspace workspace : workspaces) {
                    userOutput.println("  Workspace ID: " + workspace.getId() + ", Name: " + workspace.getName());
                }
            }
        }
    }

    /**
     * Обновляем информацию о конференц-зале.
     */
    public void updateConferenceRoom() {
        userOutput.println("Enter conference room ID to update:");
        int id = Integer.parseInt(userInput.readLine());
        userOutput.println("Enter new conference room name:");
        String name = userInput.readLine();
        userOutput.println("Enter new conference room capacity:");
        int capacity = Integer.parseInt(userInput.readLine());

        ConferenceRoom conferenceRoom = new ConferenceRoom(name, capacity);
        conferenceRoom.setId(id);
        conferenceRoomManager.updateConferenceRoom(conferenceRoom);

        userOutput.println("Conference room " + id + " updated.");
    }

    /**
     * Удаляем конференц-зал по его ID.
     */
    public void deleteConferenceRoom() {
        userOutput.println("Enter conference room ID to delete:");
        int id = Integer.parseInt(userInput.readLine());
        conferenceRoomManager.deleteConferenceRoom(id);
        userOutput.println("Conference room " + id + " deleted.");
    }

    /**
     * Добавляем рабочее место в конференц-зал.
     */
    public void addWorkspaceToConferenceRoom() {
        int conferenceRoomId = Integer.parseInt(userInput.readLine("Enter Conference Room ID:"));
        String workspaceName = userInput.readLine("Enter Workspace Name:");

        Workspace workspace = new Workspace(workspaceName);
        conferenceRoomManager.addWorkspaceToConferenceRoom(conferenceRoomId, workspace);

        userOutput.println("Workspace " + workspaceName + " added to conference room ID: " + conferenceRoomId);
    }

    /**
     * Находим конференц-залы с бронированиями на указанную дату.
     */
    public void findRoomsByDate() {
        userOutput.println("Enter date 'yyyy-MM-dd':");
        String dateInput = userInput.readLine();
        try {
            LocalDate date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<ConferenceRoom> conferenceRooms = conferenceRoomManager.findRoomsByDate(date);
            if (conferenceRooms.isEmpty()) {
                userOutput.println("No conference rooms with bookings on this date.");
            } else {
                for (ConferenceRoom conferenceRoom : conferenceRooms) {
                    userOutput.println("ID: " + conferenceRoom.getId() + ", Name: " + conferenceRoom.getName() + ", Capacity: " + conferenceRoom.getCapacity());
                }
            }
        } catch (DateTimeParseException e) {
            userOutput.println("Error: Invalid date format. Please use 'yyyy-MM-dd'.");
        }
    }

    /**
     * Находим конференц-залы с бронированиями пользователем.
     */
    public void findRoomsByUser() {
        userOutput.println("Enter Username:");
        String userName = userInput.readLine();

        List<ConferenceRoom> conferenceRooms = conferenceRoomManager.findRoomsByUser(userName);
        if (conferenceRooms.isEmpty()) {
            userOutput.println("No conference rooms with bookings by this user.");
        } else {
            for (ConferenceRoom conferenceRoom : conferenceRooms) {
                userOutput.println("ID: " + conferenceRoom.getId() + ", Name: " + conferenceRoom.getName() + ", Capacity: " + conferenceRoom.getCapacity());
            }
        }
    }

    /**
     * Находим конференц-залы с доступными рабочими местами.
     */
    public void findRoomsWithAvailableWorkspaces() {
        List<ConferenceRoom> conferenceRooms = conferenceRoomManager.findRoomsWithAvailableWorkspaces();
        if (conferenceRooms.isEmpty()) {
            userOutput.println("No conference rooms with available workspaces.");
        } else {
            for (ConferenceRoom conferenceRoom : conferenceRooms) {
                userOutput.println("ID: " + conferenceRoom.getId() + ", Name: " + conferenceRoom.getName() + ", Capacity: " + conferenceRoom.getCapacity());
            }
        }
    }
}
