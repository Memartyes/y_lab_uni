package ru.domain.entities;

/**
 * Enum для определения Конференц-залов по умолчанию
 */
public enum DefaultConferenceRooms {
    MATHEMATICS("Mathematics"),
    HISTORY("History"),
    PHILOSOPHY("Philosophy"),
    INFORMATION_TECHNOLOGY("Information Technology"),
    FOREIGN_LANGUAGES("Foreign Languages");

    private final String name;

    DefaultConferenceRooms(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
