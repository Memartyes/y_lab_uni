package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;
import ru.domain.config.WorkspaceConfig;
import ru.domain.interfaces.Room;
import ru.domain.managers.WorkspaceManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Определяем класс Конференц-зала который содержит рабочие места (Workspace).
 */
@Getter
@Setter
public class ConferenceRoom implements Room {
    private int id;
    private String name;
    private int capacity;
    private List<Workspace> workspaces;

    public ConferenceRoom() {
        this.workspaces = new ArrayList<>();
    }

    public ConferenceRoom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.workspaces = new ArrayList<>();
    }

    /**
     * Добавляем рабочее место в конференц-зал.
     *
     * @param workspace the workspace to add
     */
    public void addWorkspace(Workspace workspace) {
        this.workspaces.add(workspace);
    }

    /**
     * Проверяем, есть ли бронирования в конференц-зале на указанную дату.
     *
     * @param date the date to check
     * @return true if there are bookings on the specified date, false otherwise
     */
    public boolean hasBookingOnDate(LocalDate date) {
        return workspaces.stream().anyMatch(workspace -> workspace.hasBookingOnDate(date));
    }

    /**
     * Проверяем, есть ли бронирования в конференц-зале пользователем.
     *
     * @param userName the user's name
     * @return true if the user has bookings, false otherwise
     */
    public boolean hasBookingByUser(String userName) {
        return workspaces.stream().anyMatch(workspace -> workspace.hasBookingByUser(userName));
    }

    /**
     * Проверяем, есть ли доступные рабочие места для бронирования.
     *
     * @return true if there are available workspaces, false otherwise
     */
    public boolean hasAvailableWorkspaces() {
        return workspaces.stream().anyMatch(Workspace::isAvailable);
    }

    /**
     * Находим рабочее место по имени.
     *
     * @param workspaceName the workspace name
     * @return Optional containing the workspace if found, empty otherwise
     */
    public Optional<Workspace> findWorkspaceByName(String workspaceName) {
        return workspaces.stream().filter(workspace -> workspace.getName().equals(workspaceName)).findFirst();
    }
}
