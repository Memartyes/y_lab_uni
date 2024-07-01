package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;
import ru.domain.config.WorkspaceConfig;
import ru.domain.interfaces.Room;
import ru.domain.managers.BookingWorkspaceManager;
import ru.domain.managers.WorkspaceManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private WorkspaceManager workspaceManager;
    private BookingWorkspaceManager bookingWorkspaceManager;
    private List<Workspace> workspaces;

    /**
     * Создаем новый Конференц-зал.
     *
     * @param name the conference room name
     * @param capacity the conference room capacity
     */
    public ConferenceRoom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.workspaceManager = new WorkspaceManager();
        this.workspaces = workspaceManager.initializeWorkspaces(capacity);
        this.bookingWorkspaceManager = new BookingWorkspaceManager(workspaces);
    }

    public ConferenceRoom() {
        this.capacity = WorkspaceConfig.WORKSPACES_CAPACITY.getValue();
        this.workspaceManager = new WorkspaceManager();
        this.workspaces = workspaceManager.initializeWorkspaces(this.capacity);
        this.bookingWorkspaceManager = new BookingWorkspaceManager(workspaces);
    }

    /**
     * Получаем рабочее место по его названию.
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
     * Добавляем рабочее место в Конференц-зал.
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
     * Возвращаем количество доступных рабочих мест
     *
     * @return the count of available workspaces
     */
    public int getAvailableWorkspaceCount() {
        return (int) workspaces.stream().filter(workspace -> !workspace.isBooked()).count();
    }

    /**
     * Бронируем все доступные рабочие места в Конференц-зале для указанного пользователя.
     *
     * @param userName the username
     * @param bookingTime the booking time
     */
    public void bookAllWorkspaces(String userName, LocalDateTime bookingTime) {
        bookingWorkspaceManager.bookAllWorkspaces(userName, bookingTime);
    }

    /**
     * Отменяем бронирование конкретного рабочего места.
     *
     * @param workspaceName the workspace name
     */
    public void cancelBookingForWorkspace(String workspaceName) {
        bookingWorkspaceManager.cancelBookingForWorkspace(workspaceName);
    }

    /**
     * Проверяем доступно ли время для бронирования в Конференц-зале.
     *
     * @param dateTime the date time
     * @return true if the date time is available for book, false otherwise
     */
    public boolean isBookingTimeAvailable(LocalDateTime dateTime) {
        return bookingWorkspaceManager.isBookingTimeAvailable(dateTime);
    }

    /**
     * Отменяем бронирование всех рабочих мест в конференц-зале
     */
    public void cancelBookingForAllWorkspaces() {
        bookingWorkspaceManager.cancelBookingForAllWorkspaces();
    }

    /**
     * Проверяет наличие бронирований на указанную дату.
     *
     * @param date the date to check the booking
     * @return true if booked, false otherwise
     */
    public boolean hasBookingOnDate(LocalDate date) {
        return bookingWorkspaceManager.hasBookingOnDate(date);
    }

    /**
     * Проверяет наличие бронирований, сделанных указанным пользователем.
     *
     * @param userId the username
     * @return true if the user has books
     */
    public boolean hasBookingByUser(String userId) {
        return bookingWorkspaceManager.hasBookingByUser(userId);
    }

    /**
     * Проверяет наличие свободных рабочих мест в конференц-зале.
     *
     * @return true if available at least one available workspace slot, false otherwise
     */
    public boolean hasAvailableWorkspaces() {
        return workspaces.stream().anyMatch(workspace -> !workspace.isBooked());
    }
}
