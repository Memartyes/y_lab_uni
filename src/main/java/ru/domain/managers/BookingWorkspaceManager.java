package ru.domain.managers;

import ru.domain.entities.Workspace;
import ru.domain.config.WorkspaceConfig;
import ru.domain.interfaces.BookingManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс для управления бронированием рабочих мест в Конференц-зале.
 */
public class BookingWorkspaceManager implements BookingManager {
    private List<Workspace> workspaceList;

    public BookingWorkspaceManager(List<Workspace> workspaceList) {
        this.workspaceList = workspaceList;
    }

    /**
     * Бронируем все доступные рабочие места для пользователя.
     *
     * @param userName the username
     * @param datetime the booking time
     */
    @Override
    public void bookAllWorkspaces(String userName, LocalDateTime datetime) {
        if (!isBookingTimeAvailable(datetime)) {
            throw new IllegalStateException("Booking time is not available");
        }
        workspaceList.stream()
                .filter(workspace -> !workspace.isBooked())
                .forEach(workspace -> workspace.book(userName, datetime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue()));
    }

    /**
     * Проверяем, входит ли время в рабочие часы.
     *
     * @param dateTime the booking time to check for availability
     * @return true if booking time is available
     */
    @Override
    public boolean isBookingTimeAvailable(LocalDateTime dateTime) {
        return isWithinWorkingHours(dateTime) && isWorkingDay(dateTime) && workspaceList.stream()
                .noneMatch(workspace -> workspace.isBooked() && workspace.getBookingTime().equals(dateTime));
    }

    /**
     * Отменяем бронирование рабочего места по workspace name
     *
     * @param workspaceName
     */
    @Override
    public void cancelBookingForWorkspace(String workspaceName) {
        Workspace workspace = getWorkspace(workspaceName)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found"));
        workspace.cancelBooking();
    }

    /**
     * Берем доступные рабочие места для бронирования на дату.
     *
     * @param date the date
     * @return the list of available places on the specified date
     */
    @Override
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
     * Возвращаем список всех незабронированных рабочих мест.
     *
     * @return the list of available workspaces to book
     */
    @Override
    public List<Workspace> getAvailableWorkspaces() {
        return workspaceList.stream()
                .filter(workspace -> !workspace.isBooked())
                .collect(Collectors.toList());
    }

    /**
     * отменяет бронирование всех рабочих мест.
     */
    @Override
    public void cancelBookingForAllWorkspaces() {
        workspaceList.forEach(Workspace::cancelBooking);
    }

    /**
     * Обновляем бронирование для конкретного рабочего места.
     *
     * @param workspaceName the workspace name
     * @param userName the username
     * @param newBookingTime the new booking time
     */
    @Override
    public void updateSpecificBooking(String workspaceName, String userName, LocalDateTime newBookingTime, int bookingDurationHours) {
        Workspace workspace = getWorkspace(workspaceName)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found or booking time is not valid"));
        if (isBookingTimeAvailable(newBookingTime)) {
            workspace.book(userName, newBookingTime, bookingDurationHours);
        } else {
            throw new IllegalStateException("Workspace is not available or booking time is not valid");
        }
    }

    /**
     * Проверяет, есть ли забронированные места пользователем.
     *
     * @param userName the username
     * @return true if user has booked workspaces, false otherwise
     */
    @Override
    public boolean hasBookingByUser(String userName) {
        return workspaceList.stream().anyMatch(workspace ->
                workspace.isBooked() && workspace.getBookedBy().equals(userName));
    }

    /**
     * Проверяет, есть ли бронирования на указанную дату.
     *
     * @param date the date check to
     * @return true if books exist, false otherwise
     */
    @Override
    public boolean hasBookingOnDate(LocalDate date) {
        return workspaceList.stream().anyMatch(workspace ->
                workspace.isBooked() && workspace.getBookingTime().toLocalDate().isEqual(date));
    }

    /**
     * Бронируем конкретное рабочеее место.
     *
     * @param workspaceName the workspace name
     * @param userName the username
     * @param bookingTime the booking time
     */
    @Override
    public void bookSpecificWorkspace(String workspaceName, String userName, LocalDateTime bookingTime, int bookingDurationHours) {
        Workspace workspace = getWorkspace(workspaceName)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found or booking time is not valid"));
        if (isBookingTimeAvailable(bookingTime)) {
            workspace.book(userName, bookingTime, bookingDurationHours);
        } else {
            throw new IllegalStateException("Workspace is not available or booking time is not valid");
        }
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
     * Получаем рабочее место по его названию.
     *
     * @param workspaceName the workspace name
     * @return workspace by name
     */
    private Optional<Workspace> getWorkspace(String workspaceName) {
        return workspaceList.stream()
                .filter(workspace -> workspace.getName().equals(workspaceName))
                .findFirst();
    }
}
