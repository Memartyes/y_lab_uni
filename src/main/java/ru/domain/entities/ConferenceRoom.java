package ru.domain.entities;

import java.time.DayOfWeek;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Определяем класс Конференц-зала который содержит рабочие места (Workspace).
 */
public class ConferenceRoom {
    private String id;
    private List<Workspace> workspaces;

    /**
     * Создаем новый Конференц-зал.
     *
     * @param id the conference room ID
     */
    public ConferenceRoom(String id) {
        this.id = id;
        this.workspaces = new ArrayList<>();
        initializeWorkspace();
    }

    /**
     * Возвращаем ID Конференц-зала.
     *
     * @return the conference room ID
     */
    public String getId() {
        return id;
    }

    /**
     * Устанавливаем новый ID в Конференц-зал
     * @param id the new conference room ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Возвращаем список рабочих мест.
     *
     * @return the list of workspaces
     */
    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    /**
     * Добавляем рабочее место в Конференц-зал.
     *
     * @param workspace the workspace to add
     * @throws IllegalStateException if the workspace limit is reached
     */
    public void addWorkspace(Workspace workspace) {
        if (workspaces.size() >= WorkspaceConfig.WORKSPACES_CAPACITY.getValue()) {
            throw new IllegalStateException("Workspace limit reached");
        }
        workspaces.add(workspace);
    }

    /**
     * Получаем рабочее место по ID.
     *
     * @param workspaceId the workspace ID
     * @return the workspace with the workspaceId, or null if not found
     */
    public Workspace getWorkspace(String workspaceId) {
        for (Workspace workspace : workspaces) {
            if (workspace.getId().equals(workspaceId)) {
                return workspace;
            }
        }
        return null;
    }

    /**
     * Определим рабочие места по умолчанию с лимитом
     * указанным в WorkspaceConfig.WORKSPACES_CAPACITY.
     */
    public void initializeWorkspace() {
        for (int i = 1; i <= WorkspaceConfig.WORKSPACES_CAPACITY.getValue(); i++) {
            this.workspaces.add(new Workspace(String.valueOf(i)));
        }
    }

    /**
     * Возвращаем количество доступных рабочих мест
     *
     * @return the count of available workspaces
     */
    public int getAvailableWorkspaceCount() {
        int count = 0;
        for (Workspace workspace : workspaces) {
            if (!workspace.isBooked()) {
                count++;
            }
        }
        return count;
    }

    //FIXME: this one needs to observe
    /**
     * Проверячем если указанное время на бронирование недоступно
     *
     * @param bookingTime the time slot to check
     * @return true if the ti,e slot is available, false otherwise
     */
    public boolean isBookingTimeAvailable(LocalDateTime bookingTime) {
        int startHour = WorkspaceConfig.START_HOUR.getValue();
        int endHour = WorkspaceConfig.END_HOUR.getValue();
        DayOfWeek[] workDays = WorkspaceConfig.WORK_DAYS.getDays();

        boolean isWorkingDay = false;
        for (DayOfWeek day : workDays) {
            if (day == bookingTime.getDayOfWeek()) {
                isWorkingDay = true;
                break;
            }
        }

        LocalTime bookingLocalTime = bookingTime.toLocalTime();
        return isWorkingDay &&
                !bookingLocalTime.isBefore(LocalTime.of(startHour, 0)) &&
                !bookingLocalTime.isAfter(LocalTime.of(endHour - 1, 59));
    }

    //FIXME: don't forget to manage this one
    /**
     * Возвращаем лист доступных слотов бронирования по дате
     *
     * @param date the date to check for available slots
     * @return the list of available workspace slots
     */
    public List<String> getAvailableSlots(LocalDateTime date) {
        List<String> availableSlots = new ArrayList<>();

        for (int hour = WorkspaceConfig.START_HOUR.getValue(); hour < WorkspaceConfig.END_HOUR.getValue(); hour++) {
            LocalDateTime slotTime = date.withHour(hour).withMinute(0);
            boolean slotAvailable = true;

            for (Workspace workspace : workspaces) {
                if (workspace.isBooked() && workspace.getBookingTime().equals(slotTime)) {
                    slotAvailable = false;
                    break;
                }
            }
            if (slotAvailable) {
                availableSlots.add(slotTime.toLocalTime().toString());
            }
        }
        return availableSlots;
    }

    /**
     * Метод для бронирования всего Конференц-зала
     *
     * @param userId the user ID
     * @param bookingTime the booking time
     */
    public void bookAllWorkspaces(String userId, LocalDateTime bookingTime) {
        if (!isBookingTimeAvailable(bookingTime)) {
            throw new IllegalStateException("Booking time not available");
        }
        for (Workspace workspace : workspaces) {
            if (!workspace.isBooked()) {
                workspace.book(userId, bookingTime);
            }
        }
    }

    /**
     * Отмена бронирования рабочего места
     *
     * @param workspaceId the workspace ID
     */
    public void cancelBookingForWorkspace(String workspaceId) {
        Workspace workspace = getWorkspace(workspaceId);
        if (workspace == null) {
            throw new IllegalArgumentException("Workspace not found");
        }
        workspace.cancelBooking();
    }

    /**
     * Отмена бронирования всего Конференц-зала
     */
    public void cancelBookingForAllWorkspaces() {
        for (Workspace workspace : workspaces) {
            if (workspace.isBooked()) {
                workspace.cancelBooking();
            }
        }
    }
}
