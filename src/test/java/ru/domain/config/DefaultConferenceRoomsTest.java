package ru.domain.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultConferenceRoomsTest {

    @Test
    public void testDefaultConferenceRoomsNames() {
        assertThat(DefaultConferenceRooms.PHILOSOPHY.getName()).isEqualTo("Philosophy");
        assertThat(DefaultConferenceRooms.INFORMATION_TECHNOLOGY.getName()).isEqualTo("Information Technology");
        assertThat(DefaultConferenceRooms.FOREIGN_LANGUAGES.getName()).isEqualTo("Foreign Languages");
    }

    @Test
    public void testDefaultConferenceRoomsValues() {
        DefaultConferenceRooms[] rooms = DefaultConferenceRooms.values();
        assertThat(rooms).containsExactly(
                DefaultConferenceRooms.MATHEMATICS,
                DefaultConferenceRooms.HISTORY,
                DefaultConferenceRooms.PHILOSOPHY,
                DefaultConferenceRooms.INFORMATION_TECHNOLOGY,
                DefaultConferenceRooms.FOREIGN_LANGUAGES
        );
    }

    @Test
    public void testValueOf() {
        assertThat(DefaultConferenceRooms.valueOf("PHILOSOPHY")).isEqualTo(DefaultConferenceRooms.PHILOSOPHY);
        assertThat(DefaultConferenceRooms.valueOf("INFORMATION_TECHNOLOGY")).isEqualTo(DefaultConferenceRooms.INFORMATION_TECHNOLOGY);
        assertThat(DefaultConferenceRooms.valueOf("FOREIGN_LANGUAGES")).isEqualTo(DefaultConferenceRooms.FOREIGN_LANGUAGES);
    }
}