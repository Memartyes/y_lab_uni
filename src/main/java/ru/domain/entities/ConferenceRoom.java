package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;
import ru.domain.config.WorkspaceConfig;
import ru.domain.managers.BookingManager;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Определяем класс Конференц-зала который содержит рабочие места (Workspace).
 */
@Getter
@Setter
public class ConferenceRoom {
    private int id;
    private String name;
    private int capacity;
    private List<Workspace> workspaces;
    private BookingManager bookingManager;

    /**
     * Создаем новый Конференц-зал.
     *
     * @param name the conference room name
     */
    public ConferenceRoom(String name) {
        this.name = name;
        this.workspaces = new ArrayList<>();
        initializeWorkspace();
        this.capacity = WorkspaceConfig.WORKSPACES_CAPACITY.getValue();
        this.bookingManager = new BookingManager(workspaces);
    }

    public ConferenceRoom() {
        this.capacity = WorkspaceConfig.WORKSPACES_CAPACITY.getValue();
        this.workspaces = new ArrayList<>();
        this.bookingManager = new BookingManager(workspaces);
    }

    /**
     * Определим рабочие места по умолчанию
     */
    public void initializeWorkspace() {
        for (int i = 1; i <= WorkspaceConfig.WORKSPACES_CAPACITY.getValue(); i++) {
            this.workspaces.add(new Workspace(String.valueOf(i)));
        }
    }

    /**
     * Получаем рабочее место по ID.
     *
     * @param workspaceId the workspace ID
     * @return the workspace with the workspaceId, or null if not found
     */
    public Workspace getWorkspace(String workspaceId) {
        return workspaces.stream()
                .filter(workspace -> workspace.getName().equals(workspaceId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Добавляем рабочее место в Конференц-зал.
     *
     * @param workspace the workspace to add
     * @throws IllegalStateException if the workspace limit is reached
     */
    public void addWorkspace(Workspace workspace) {
        for (Workspace ws : workspaces) {
            if (ws.getName().equals(workspace.getName())) {
                throw new IllegalArgumentException("Workspace already exists");
            }
        }

        this.workspaces.add(workspace);
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
     * @param userId the user ID
     * @param bookingTime the booking time
     */
    public void bookAllWorkspaces(String userId, LocalDateTime bookingTime) {
        bookingManager.bookAllWorkspaces(userId, bookingTime);
    }

    /**
     * Отменяем бронирование конкретного рабочего места.
     *
     * @param workspaceId the workspace ID
     */
    public void cancelBookingForWorkspace(String workspaceId) {
        bookingManager.cancelBookingForWorkspace(workspaceId);
    }

    /**
     * Проверяем доступно ли время для бронирования в Конференц-зале.
     *
     * @param dateTime the date time
     * @return true if the date time is available for book, false otherwise
     */
    public boolean isBookingTimeAvailable(LocalDateTime dateTime) {
        return bookingManager.isBookingTimeAvailable(dateTime);
    }

    /**
     * Отменяем бронирование всех рабочих мест в конференц-зале
     */
    public void cancelBookingForAllWorkspaces() {
        bookingManager.cancelBookingForAllWorkspaces();
    }

    /**
     * Проверяет наличие бронирований на указанную дату.
     *
     * @param date the date to check the booking
     * @return true if booked, false otherwise
     */
    public boolean hasBookingOnDate(LocalDate date) {
        return bookingManager.hasBookingOnDate(date);
    }

    /**
     * Проверяет наличие бронирований, сделанных указанным пользователем.
     *
     * @param userId the user ID
     * @return true if the user has books
     */
    public boolean hasBookingByUser(String userId) {
        return bookingManager.hasBookingByUser(userId);
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
