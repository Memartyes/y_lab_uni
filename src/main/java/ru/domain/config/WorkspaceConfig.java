package ru.domain.config;

/**
 * Добавим ENUM для удобства быстрой смены максимального параметр вместимости рабочих мест в Конференц-зале
 */
public enum WorkspaceConfig {
    BOOKING_DURATION_HOURS(1),
    WORKSPACES_CAPACITY(8),
    START_HOUR(8),
    END_HOUR(16),
    WORK_DAYS(new String[]{
                "MONDAY",
                "TUESDAY",
                "WEDNESDAY",
                "THURSDAY",
                "FRIDAY"
    });

    private int value;
    private String[] days;

    WorkspaceConfig(int value) {
        this.value = value;
        this.days = null;
    }

    WorkspaceConfig(String[] days) {
        this.value = -1;
        this.days = days;
    }

    public int getValue() {
        return value;
    }

    public String[] getDays() {
        return days;
    }
}
