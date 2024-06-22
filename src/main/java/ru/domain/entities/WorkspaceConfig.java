package ru.domain.entities;

import java.time.DayOfWeek;

/**
 * Добавим ENUM для удобства быстрой смены максимального параметр вместимости рабочих мест в Конференц-зале
 */
public enum WorkspaceConfig {
    BOOKING_DURATION_HOURS(1),
    WORKSPACES_CAPACITY(8),
    START_HOUR(8),
    END_HOUR(16),
    WORK_DAYS(new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY});

    private final int value;
    private final DayOfWeek[] days;

    WorkspaceConfig(int value) {
        this.value = value;
        this.days = null;
    }

    WorkspaceConfig(DayOfWeek[] days) {
        this.value = -1;
        this.days = days;
    }

    public int getValue() {
        return value;
    }

    public DayOfWeek[] getDays() {
        return days;
    }
}
