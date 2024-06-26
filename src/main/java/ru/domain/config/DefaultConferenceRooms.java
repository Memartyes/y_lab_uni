package ru.domain.config;

import lombok.Getter;

/**
 * Enum для определения Конференц-залов по умолчанию
 */
@Getter
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

}
