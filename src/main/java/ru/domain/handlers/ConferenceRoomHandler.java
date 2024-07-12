package ru.domain.handlers;

import ru.domain.managers.ConferenceRoomManager;
import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.Workspace;
import ru.domain.managers.WorkspaceManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Определячем класс для обработки логики Конференц-зала.
 */
public class ConferenceRoomHandler {
    private final ConferenceRoomManager conferenceRoomManager;
    private final WorkspaceManager workspaceManager;

    public ConferenceRoomHandler(ConferenceRoomManager conferenceRoomManager, WorkspaceManager workspaceManager) {
        this.conferenceRoomManager = conferenceRoomManager;
        this.workspaceManager = workspaceManager;
    }

    /**
     * Создаем новый конференц-зал.
     */
    public void createConferenceRoom(String name, int capacity) {
        ConferenceRoom conferenceRoom = new ConferenceRoom(name, capacity);
        conferenceRoomManager.addConferenceRoom(conferenceRoom);

        System.out.println("Conference room " + name + " created with capacity " + capacity);
    }

    /**
     * Отображаем все конференц-залы.
     */
    public void viewConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = conferenceRoomManager.findAllConferenceRooms();
        if (conferenceRooms.isEmpty()) {
            System.out.println("No conference rooms available.");
        } else {
            System.out.println("Available conference rooms:");
            for (ConferenceRoom conferenceRoom : conferenceRooms) {
                System.out.println("ID: " + conferenceRoom.getId() + ", Name: " + conferenceRoom.getName() + ", Capacity: " + conferenceRoom.getCapacity());
                List<Workspace> workspaces = conferenceRoomManager.findWorkspacesByConferenceRoomId(conferenceRoom.getId());
                for (Workspace workspace : workspaces) {
                    System.out.println("  Workspace ID: " + workspace.getId() + ", Name: " + workspace.getName());
                }
            }
        }
    }

    /**
     * Добавляем рабочее место в конференц-зал.
     */
    public void addWorkspaceToConferenceRoom(int conferenceRoomId, String workspaceName) {
        Workspace workspace = new Workspace(workspaceName);
        conferenceRoomManager.addWorkspaceToConferenceRoom(conferenceRoomId, workspace);

        System.out.println("Workspace " + workspaceName + " added to conference room ID: " + conferenceRoomId);
    }

    /**
     * Находим конференц-залы с бронированиями на указанную дату.
     */
    public void findRoomsByDate(String dateInput) {
        try {
            LocalDate date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<ConferenceRoom> conferenceRooms = conferenceRoomManager.findRoomsByDate(date);
            if (conferenceRooms.isEmpty()) {
                System.out.println("No conference rooms with bookings on this date.");
            } else {
                for (ConferenceRoom conferenceRoom : conferenceRooms) {
                    System.out.println("ID: " + conferenceRoom.getId() + ", Name: " + conferenceRoom.getName() + ", Capacity: " + conferenceRoom.getCapacity());
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use 'yyyy-MM-dd'.");
        }
    }

    /**
     * Находим конференц-залы с бронированиями пользователем.
     */
    public void findRoomsByUser(String userName) {
        List<ConferenceRoom> conferenceRooms = conferenceRoomManager.findRoomsByUser(userName);
        if (conferenceRooms.isEmpty()) {
            System.out.println("No conference rooms with bookings by this user.");
        } else {
            for (ConferenceRoom conferenceRoom : conferenceRooms) {
                System.out.println("ID: " + conferenceRoom.getId() + ", Name: " + conferenceRoom.getName() + ", Capacity: " + conferenceRoom.getCapacity());
            }
        }
    }

    /**
     * Находим конференц-залы с доступными рабочими местами.
     */
    public void findRoomsWithAvailableWorkspaces() {
        List<ConferenceRoom> conferenceRooms = conferenceRoomManager.findRoomsWithAvailableWorkspaces();
        if (conferenceRooms.isEmpty()) {
            System.out.println("No conference rooms with available workspaces.");
        } else {
            for (ConferenceRoom conferenceRoom : conferenceRooms) {
                System.out.println("ID: " + conferenceRoom.getId() + ", Name: " + conferenceRoom.getName() + ", Capacity: " + conferenceRoom.getCapacity());
            }
        }
    }
}
