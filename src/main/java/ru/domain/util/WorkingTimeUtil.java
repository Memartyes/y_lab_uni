package ru.domain.util;

import ru.domain.config.WorkspaceConfig;

import java.time.LocalDateTime;

/**
 * Класс для проверки рабочих дней и рабочих часов.
 */
public class WorkingTimeUtil {
    /**
     * Проверяет, является ли указанная дата рабочим днем.
     *
     * @param dateTime the date time
     * @return true if the day is working day, false otherwise
     */
    public static boolean isWorkingDay(LocalDateTime dateTime) {
        String dayOfWeek = dateTime.toLocalDate().getDayOfWeek().name();
        for (String validDay : WorkspaceConfig.WORK_DAYS.getDays()) {
            if (validDay.equalsIgnoreCase(dayOfWeek)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет, входит ли время в рабочие часы.
     *
     * @param dateTime the date time
     * @return true if the hour is working hour, false otherwise
     */
    public static boolean isWithinWorkingHours(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        return hour >= WorkspaceConfig.START_HOUR.getValue() && hour < WorkspaceConfig.END_HOUR.getValue();
    }
}
