package ru.domain.managers;

import ru.domain.entities.Workspace;
import ru.domain.config.WorkspaceConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для управления бронированием рабочих мест в Конференц-зале.
 */
public class BookingManager {
    private List<Workspace> workspaceList;

    public BookingManager(List<Workspace> workspaceList) {
        this.workspaceList = workspaceList;
    }

    /**
     * Бронируем все доступные рабочие места для пользователя.
     *
     * @param userId the user ID
     * @param datetime the booking time
     */
    public void bookAllWorkspaces(String userId, LocalDateTime datetime) {
        if (!isBookingTimeAvailable(datetime)) {
            throw new IllegalStateException("Booking time is not available");
        }
        workspaceList.stream()
                .filter(workspace -> !workspace.isBooked())
                .forEach(workspace -> workspace.book(userId, datetime));
    }

    /**
     * Проверяем, входит ли время в рабочие часы.
     *
     * @param dateTime the booking time to check for availability
     * @return true if booking time is available
     */
    public boolean isBookingTimeAvailable(LocalDateTime dateTime) {
        return isWithinWorkingHours(dateTime) && isWorkingDay(dateTime) && workspaceList.stream()
                .noneMatch(workspace -> workspace.isBooked() && workspace.getBookingTime().equals(dateTime));
    }

    /**
     * Проверяем, является ли указанная дата рабочим днем.
     *
     * @param dateTime the date time
     * @return true if the day is working day, false otherwise
     */
    private boolean isWorkingDay(LocalDateTime dateTime) {
        String dayOfWeek = dateTime.toLocalDate().getDayOfWeek().name();
        for (String validDay : WorkspaceConfig.WORK_DAYS.getDays()) {
            if (validDay.equalsIgnoreCase(dayOfWeek)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяем, входит ли время в рабочие часы.
     *
     * @param dateTime the date time
     * @return true if the hour is working hour, false otherwise
     */
    private boolean isWithinWorkingHours(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        return hour >= WorkspaceConfig.START_HOUR.getValue() && hour < WorkspaceConfig.END_HOUR.getValue();
    }

    /**
     * Отменяем бронирование рабочего места по workspace ID
     *
     * @param workspaceId
     */
    public void cancelBookingForWorkspace(String workspaceId) {
        Workspace workspace = getWorkspace(workspaceId);
        if (workspace == null) {
            throw new IllegalArgumentException("Workspace not found");
        }
        workspace.cancelBooking();
    }

    /**
     * Получаем рабочее место по ID.
     *
     * @param workspaceId the workspace ID
     * @return workspace by ID
     */
    private Workspace getWorkspace(String workspaceId) {
        return workspaceList.stream()
                .filter(workspace -> workspace.getName().equals(workspaceId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Берем доступные рабочие места для бронирования на дату.
     *
     * @param date the date
     * @return the list of available places on the specified date
     */
    public List<String> getAvailableSlots(LocalDateTime date) {
        List<String> availableSlots = new ArrayList<>();

        // Определите начало и конец рабочего дня на основе конфигурации
        int startHour = WorkspaceConfig.START_HOUR.getValue();
        int endHour = WorkspaceConfig.END_HOUR.getValue();
        LocalDateTime startDateTime = date.withHour(startHour).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDateTime = date.withHour(endHour).withMinute(0).withSecond(0).withNano(0);

        // Перебираем каждый час в рабочем диапазоне времени
        for (LocalDateTime time = startDateTime; time.isBefore(endDateTime); time = time.plusHours(1)) {
            final LocalDateTime slot = time;

            // Проверяем, не забронировано ли все рабочие места на этот час
            boolean isAvailable = workspaceList.stream()
                    .allMatch(workspace -> !workspace.isBooked() ||
                            !workspace.getBookingTime().equals(slot));

            if (isAvailable) {
                availableSlots.add(slot.toLocalTime().toString());
            }
        }
        return availableSlots;
    }

    /**
     * отменяет бронирование всех рабочих мест.
     */
    public void cancelBookingForAllWorkspaces() {
        workspaceList.forEach(Workspace::cancelBooking);
    }

    /**
     * Обновляем бронирование для конкретного рабочего места.
     *
     * @param workspaceId the workspace ID
     * @param userId the user ID
     * @param newBookingTime the new booking time
     */
    public void updateBooking(String workspaceId, String userId, LocalDateTime newBookingTime) {
        Workspace workspace = getWorkspace(workspaceId);
        if (workspace != null && isBookingTimeAvailable(newBookingTime)) {
            workspace.book(userId, newBookingTime);
        } else {
            throw new IllegalStateException("Workspace is not available or booking time is not valid");
        }
    }

    /**
     * Возвращаем список всех незабронированных рабочих мест.
     *
     * @return the list of available workspaces to book
     */
    public List<Workspace> getAvailableWorkspaces() {
        return workspaceList.stream()
                .filter(workspace -> !workspace.isBooked())
                .collect(Collectors.toList());
    }

    /**
     * Проверяет, есть ли забронированные места пользователем.
     *
     * @param userId the user ID
     * @return true if user has booked workspaces, false otherwise
     */
    public boolean hasBookingByUser(String userId) {
        return workspaceList.stream().anyMatch(workspace ->
                workspace.isBooked() && workspace.getBookedBy().equals(userId));
    }

    /**
     * Проверяет, есть ли бронирования на указанную дату.
     *
     * @param date the date check to
     * @return true if books exist, false otherwise
     */
    public boolean hasBookingOnDate(LocalDate date) {
        return workspaceList.stream().anyMatch(workspace ->
                workspace.isBooked() && workspace.getBookingTime().toLocalDate().isEqual(date));
    }

    /**
     * Бронируем конкретное рабочеее место.
     *
     * @param workspaceId the workspace ID
     * @param userId the user ID
     * @param bookingTime the booking time
     */
    public void bookSpecificWorkspace(String workspaceId, String userId, LocalDateTime bookingTime) {
        Workspace workspace = getWorkspace(workspaceId);
        if (workspace != null && isBookingTimeAvailable(bookingTime)) {
            workspace.book(userId, bookingTime);
        } else {
            throw new IllegalStateException("Workspace is not available or booking time is not valid");
        }
    }
}
