package ru.domain.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.domain.entities.ConferenceRoom;
import ru.domain.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ConferenceRoomRepositoryTest {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("password")
            .withInitScript("db/changelog/changeset/init_conference_room.sql");

    private ConferenceRoomRepository conferenceRoomRepository;

    @BeforeEach
    void setUp() {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();
        DatabaseUtil.setUrl(jdbcUrl);
        DatabaseUtil.setUser(username);
        DatabaseUtil.setPassword(password);
        conferenceRoomRepository = new ConferenceRoomRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE coworking.\"conference_rooms-liquibase\";");
        }
    }

    @Test
    void testAddAndFindConferenceRoom() throws SQLException {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("addConferenceRoom");
        conferenceRoom.setCapacity(10);
        conferenceRoomRepository.addConferenceRoom(conferenceRoom);
        ConferenceRoom foundConferenceRoom = conferenceRoomRepository.findConferenceRoomByName("addConferenceRoom").orElseThrow();
        assertNotNull(foundConferenceRoom);
        assertEquals("addConferenceRoom", foundConferenceRoom.getName());
        assertEquals(10, foundConferenceRoom.getCapacity());
    }

    @Test
    void testFindAllConferenceRooms() throws SQLException {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        ConferenceRoom conferenceRoom1 = new ConferenceRoom();
        conferenceRoom1.setName("addConferenceRoom");
        conferenceRoom1.setCapacity(10);
        conferenceRoomRepository.addConferenceRoom(conferenceRoom1);
        ConferenceRoom conferenceRoom2 = new ConferenceRoom();
        conferenceRoom2.setName("addConferenceRoom");
        conferenceRoom2.setCapacity(10);
        conferenceRoomRepository.addConferenceRoom(conferenceRoom2);
        List<ConferenceRoom> conferenceRoomList = conferenceRoomRepository.findAllConferenceRooms();
        assertTrue(conferenceRoomList.size() >= 2);

    }

    @Test
    void testUpdateConferenceRoom() throws SQLException {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("addConferenceRoom");
        conferenceRoom.setCapacity(10);
        conferenceRoomRepository.addConferenceRoom(conferenceRoom);
        conferenceRoom.setCapacity(20);
        conferenceRoomRepository.updateConferenceRoom(conferenceRoom);
        ConferenceRoom updatedConferenceRoom = conferenceRoomRepository.findConferenceRoomByName("addConferenceRoom").orElseThrow();
        assertEquals(20, updatedConferenceRoom.getCapacity());

    }

    @Test
    void testDeleteConferenceRoom() throws SQLException {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("addConferenceRoom");
        conferenceRoom.setCapacity(10);
        conferenceRoomRepository.addConferenceRoom(conferenceRoom);
        conferenceRoomRepository.deleteConferenceRoom(conferenceRoom.getId());
        assertTrue(conferenceRoomRepository.findConferenceRoomByName("addConferenceRoom").isEmpty());
    }
}