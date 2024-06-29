package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;
import ru.domain.config.WorkspaceConfig;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Определяем класс Конференц-зала который содержит рабочие места (Workspace).
 */
public class ConferenceRoom {
    @Setter
    @Getter
    private String name;
    private List<Workspace> workspaces;
    @Setter
    @Getter
    private int id;
    @Getter
    @Setter
    private int capacity;

    /**
     * Создаем новый Конференц-зал.
     *
     * @param name the conference room name
     */
    public ConferenceRoom(String name) {
        this.name = name;
        this.workspaces = new ArrayList<>();
        initializeWorkspace();
        capacity = WorkspaceConfig.WORKSPACES_CAPACITY.getValue();
    }

    public ConferenceRoom() {
        capacity = WorkspaceConfig.WORKSPACES_CAPACITY.getValue();
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
     * Возвращаем название Конференц-зала.
     *
     * @return the conference room name
     */
    public String getName() {
        return name;
    }

    /**
     * Получаем рабочее место по ID.
     *
     * @param workspaceId the workspace ID
     * @return the workspace with the workspaceId, or null if not found
     */
    public Workspace getWorkspace(String workspaceId) {
        return workspaces.stream().filter(workspace -> workspace.getName().equals(workspaceId)).findFirst().orElse(null);
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
//        if (workspaces.size() >= WorkspaceConfig.WORKSPACES_CAPACITY.getValue()) {
//            throw new IllegalStateException("Workspace limit reached");
//        }

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
        int count = 0;
        for (Workspace workspace : workspaces) {
            if (!workspace.isBooked()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Проверка Конференц-зала на доступность бронирования по времени
     *
     * @param dateTime the date time to check
     * @return true if the booking time is available
     */
    public boolean isBookingTimeAvailable(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        //Проверяем, совпадает ли дата с рабочими днями
        String dayOfWeek = date.getDayOfWeek().name();
        boolean isValidDay = false;

        for (String validDay : WorkspaceConfig.WORK_DAYS.getDays()) {
            if (validDay.equalsIgnoreCase(dayOfWeek)) {
                isValidDay = true;
                break;
            }
        }

        if (!isValidDay) {
            return false;
        }

        //Проверяем, совпадает ли время с рабочим временем
        int hour = time.getHour();
        if (hour < WorkspaceConfig.START_HOUR.getValue() || hour >= WorkspaceConfig.END_HOUR.getValue()) {
            return false;
        }

        //Проверяем, забронированы ли рабочие места в указанное время
        for (Workspace workspace : workspaces) {
            if (workspace.isBooked() && workspace.getBookingTime().equals(dateTime)) {
                return false;
            }
        }

        return true;
    }

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
     * Проверяем есть ли забронированные места на указанную дату
     * @param date the date
     * @return true if workspaces are booked on current date, false otherwise
     */
    public boolean hasBookingOnDate(LocalDate date) {
        return workspaces.stream()
                .anyMatch(ws -> ws.isBooked() && ws.getBookingTime().toLocalDate().isEqual(date));
    }

    /**
     * Проверяем бронировал ли пользователь рабочие места
     * @param userId the user ID
     * @return true if the user booked a workspace, false otherwise
     */
    public boolean hasBookingByUser(String userId) {
        return workspaces.stream()
                .anyMatch(ws -> ws.isBooked() && ws.getBookedBy().equals(userId));
    }

    public boolean hasAvailableWorkspaces() {
        return workspaces.stream().anyMatch(ws -> !ws.isBooked());
    }

    /**
     * Метод для бронирования всего Конференц-зала
     *
     * @param userId the user ID
     * @param bookingTime the booking time
     */
    public void bookAllWorkspaces(String userId, LocalDateTime bookingTime) {
        for (Workspace workspace : workspaces) {
            if (!workspace.isBooked() && isBookingTimeAvailable(bookingTime)) {
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
