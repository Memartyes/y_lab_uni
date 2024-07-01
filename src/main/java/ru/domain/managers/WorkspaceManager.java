package ru.domain.managers;

import lombok.Getter;
import ru.domain.config.WorkspaceConfig;
import ru.domain.entities.Workspace;
import ru.domain.entities.ConferenceRoom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Определяем класс для управления рабочими местами
 */
public class WorkspaceManager {
    @Getter
    private List<Workspace> workspaces;

    public WorkspaceManager() {
        this.workspaces = new ArrayList<>();
    }

    /**
     * Определяем рабочие места по умолчанию.
     *
     * @param capacity the conference room capacity
     */
    public List<Workspace> initializeWorkspaces(int capacity) {
        List<Workspace> workspaces = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            Workspace workspace = new Workspace(String.valueOf(i));
            workspaces.add(workspace);
        }
        return workspaces;
    }

    /**
     * Получаем рабочее место по его названию
     *
     * @param workspaceName the workspace name
     * @return the workspace by name, or null otherwise
     */
    public Optional<Workspace> getWorkspace(String workspaceName) {
        return workspaces.stream()
                .filter(workspace -> workspace.getName().equals(workspaceName))
                .findFirst();
    }

    /**
     * Добавляем рабочее место.
     *
     * @param workspace the workspace to add
     */
    public void addWorkspace(Workspace workspace) {
        if (getWorkspace(workspace.getName()).isPresent()) {
            throw new IllegalArgumentException("Workspace with name " + workspace.getName() + " already exists.");
        }
        workspaces.add(workspace);
    }

    /**
     * Отменяем бронирование рабочего места.
     *
     * @param workspaceName the workspace name
     */
    public void cancelBookingWorkspace(String workspaceName) {
        Workspace workspace = getWorkspace(workspaceName).orElse(null);
        if (workspace == null) {
            throw new IllegalArgumentException("Workspace with name " + workspaceName + " not found.");
        }
        workspace.cancelBooking();
    }

    /**
     * Отменяем бронирование всех рабочих мест.
     */
    public void cancelBookingForAllWorkspaces() {
        for (Workspace workspace : workspaces) {
            workspace.cancelBooking();
        }
    }

    /**
     * Возвращаем количество доступных рабочих мест.
     *
     * @return the available workspaces count
     */
    public int getAvailableWorkspaceCount() {
        return (int) workspaces.stream().filter(workspace -> !workspace.isBooked()).count();
    }

    /**
     * Проверяем Конференц-зал на наличие свободных рабочих мест.
     *
     * @return true if available at least one workspace, false otherwise
     */
    public boolean hasAvailableWorkspaces() {
        return workspaces.stream().anyMatch(workspace -> !workspace.isBooked());
    }
}
