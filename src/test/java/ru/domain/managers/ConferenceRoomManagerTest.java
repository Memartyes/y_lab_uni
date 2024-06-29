package ru.domain.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import ru.domain.entities.ConferenceRoom;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConferenceRoomManagerTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testInitializeConferenceRooms_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertNotNull(manager.getConferenceRoomRepository().get("room1"));
        assertNotNull(manager.getConferenceRoomRepository().get("room2"));
    }

    @Test
    void testCreateConferenceRoom_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");

        Map<String, ConferenceRoom> conferenceRoomRepository = manager.getConferenceRoomRepository();
        assertTrue(conferenceRoomRepository.containsKey("room1"));
    }

    @Test
    void testCreateConferenceRoom_AlreadyExists() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room2");

        assertThrows(IllegalArgumentException.class, () -> manager.createConferenceRoom("room2"));
    }

    @Test
    void testAddWorkspaceToConferenceRoom_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("RoomWithWorkspaces");

        manager.addWorkspaceToConferenceRoom("RoomWithWorkspaces", "Workspace1");

        ConferenceRoom room = manager.getConferenceRoomRepository().get("RoomWithWorkspaces");
        assertNotNull(room.getWorkspace("Workspace1"));
    }

    @Test
    void testAddWorkspaceToConferenceRoom_RoomNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertThrows(IllegalArgumentException.class, () -> manager.addWorkspaceToConferenceRoom("NonExistingRoom", "Workspace1"));
    }

    @Test
    void testBookWorkspace_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace1");

        LocalDateTime bookingTime = LocalDateTime.now().plusHours(1);
        manager.bookWorkspace("room1", "workspace1", "user1", bookingTime);

        ConferenceRoom room = manager.getConferenceRoomRepository().get("room1");
        assertTrue(room.getWorkspace("workspace1").isBooked());
    }

    @Test
    void testBookWorkspace_ConferenceRoomNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertThrows(IllegalArgumentException.class, () ->
                manager.bookWorkspace("NonExistingRoom", "workspace1", "user1", LocalDateTime.now()));
    }

    @Test
    void testBookWorkspace_WorkspaceNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room2");

        assertThrows(IllegalArgumentException.class, () ->
                manager.bookWorkspace("room2", "NonExistingWorkspace", "user1", LocalDateTime.now()));
    }

    @Test
    void testBookWorkspace_TimeSlotUnavailable() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room3");
        manager.addWorkspaceToConferenceRoom("room3", "workspace1");
        manager.bookWorkspace("room3", "workspace1", "user1", LocalDateTime.now());

        LocalDateTime overlappingTime = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class, () ->
                manager.bookWorkspace("room3", "workspace1", "user2", overlappingTime));
    }

    @Test
    void testBookAllWorkspaces_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace2");

        LocalDateTime bookingTime = LocalDateTime.now().plusHours(1);
        manager.bookAllWorkspaces("room1", "user1", bookingTime);

        ConferenceRoom room = manager.getConferenceRoomRepository().get("room1");
        assertTrue(room.getWorkspace("workspace1").isBooked());
        assertTrue(room.getWorkspace("workspace2").isBooked());
    }

    @Test
    void testBookAllWorkspaces_ConferenceRoomNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertThrows(IllegalArgumentException.class, () ->
                manager.bookAllWorkspaces("NonExistingRoom", "user1", LocalDateTime.now()));
    }

    @Test
    void testUpdateConferenceRoom_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("OldRoomId");
        manager.updateConferenceRoom("OldRoomId", "NewRoomId");

        Map<String, ConferenceRoom> conferenceRoomRepository = manager.getConferenceRoomRepository();
        assertFalse(conferenceRoomRepository.containsKey("OldRoomId"));
        assertTrue(conferenceRoomRepository.containsKey("NewRoomId"));
    }

    @Test
    void testUpdateConferenceRoom_NewIdAlreadyExists() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.createConferenceRoom("room2");

        assertThrows(IllegalArgumentException.class, () -> manager.updateConferenceRoom("room1", "room2"));
    }

    @Test
    void testDeleteConferenceRoom_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("RoomToDelete");

        manager.deleteConferenceRoom("RoomToDelete");

        Map<String, ConferenceRoom> conferenceRoomRepository = manager.getConferenceRoomRepository();
        assertFalse(conferenceRoomRepository.containsKey("RoomToDelete"));
    }

    @Test
    void testDeleteConferenceRoom_NotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertThrows(IllegalArgumentException.class, () -> manager.deleteConferenceRoom("NonExistingRoom"));
    }

    @Test
    void testGetConferenceRoomRepository() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.createConferenceRoom("room2");

        Map<String, ConferenceRoom> repository = manager.getConferenceRoomRepository();

        assertEquals(2, repository.size());
        assertTrue(repository.containsKey("room1"));
        assertTrue(repository.containsKey("room2"));
    }

    @Test
    void testGetAvailableSlots_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace2");

        LocalDateTime bookingTime = LocalDateTime.now().plusHours(1);
        manager.bookWorkspace("room1", "workspace1", "user1", bookingTime);

        LocalDate bookingDate = bookingTime.toLocalDate();
        List<String> availableSlots = manager.getAvailableSlots("room1", bookingDate);

        assertEquals(1, availableSlots.size());
        assertTrue(availableSlots.get(0).contains("workspace2"));
    }

    @Test
    void testGetAvailableSlots_ConferenceRoomNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertThrows(IllegalArgumentException.class, () ->
                manager.getAvailableSlots("NonExistingRoom", LocalDate.now()));
    }

    @Test
    void testCancelBookingForWorkspace_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace1");
        manager.bookWorkspace("room1", "workspace1", "user1", LocalDateTime.now());

        manager.cancelBookingForWorkspace("room1", "workspace1");

        ConferenceRoom room = manager.getConferenceRoomRepository().get("room1");
        assertFalse(room.getWorkspace("workspace1").isBooked());
    }

    @Test
    void testCancelBookingForWorkspace_ConferenceRoomNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertThrows(IllegalArgumentException.class, () ->
                manager.cancelBookingForWorkspace("NonExistingRoom", "workspace1"));
    }

    @Test
    void testCancelBookingForWorkspace_WorkspaceNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room2");

        assertThrows(IllegalArgumentException.class, () ->
                manager.cancelBookingForWorkspace("room2", "NonExistingWorkspace"));
    }

    @Test
    void testCancelBookingForAllWorkspaces_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace2");
        manager.bookWorkspace("room1", "workspace1", "user1", LocalDateTime.now());
        manager.bookWorkspace("room1", "workspace2", "user2", LocalDateTime.now());

        manager.cancelBookingForAllWorkspaces("room1");

        ConferenceRoom room = manager.getConferenceRoomRepository().get("room1");
        assertFalse(room.getWorkspace("workspace1").isBooked());
        assertFalse(room.getWorkspace("workspace2").isBooked());
    }

    @Test
    void testCancelBookingForAllWorkspaces_ConferenceRoomNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertThrows(IllegalArgumentException.class, () ->
                manager.cancelBookingForAllWorkspaces("NonExistingRoom"));
    }

    @Test
    void testFilterByDate_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.createConferenceRoom("room2");

        manager.addWorkspaceToConferenceRoom("room1", "workspace1");
        manager.addWorkspaceToConferenceRoom("room2", "workspace2");

        manager.bookWorkspace("room1", "workspace1", "user1", LocalDateTime.now().plusHours(1));
        manager.bookWorkspace("room2", "workspace2", "user2", LocalDateTime.now().plusDays(1));

        List<String> filteredRooms = manager.filterByDate(LocalDate.now().plusDays(1));

        assertEquals(1, filteredRooms.size());
        assertTrue(filteredRooms.get(0).contains("roomb"));
    }

    @Test
    void testFilterByDate_NoBookingsOnDate() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");

        List<String> filteredRooms = manager.filterByDate(LocalDate.now().plusDays(1));

        assertTrue(filteredRooms.isEmpty());
    }

    @Test
    void testFilterByDate_ConferenceRoomNotFound() {
        ConferenceRoomManager manager = new ConferenceRoomManager();

        assertThrows(IllegalArgumentException.class, () ->
                manager.filterByDate(LocalDate.now()));
    }

    @Test
    void testFilterByUser_Success() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.createConferenceRoom("room2");

        manager.addWorkspaceToConferenceRoom("room1", "workspace1");
        manager.addWorkspaceToConferenceRoom("room2", "workspace2");

        manager.bookWorkspace("room1", "workspace1", "user1", LocalDateTime.now().plusHours(1));
        manager.bookWorkspace("room2", "workspace2", "user2", LocalDateTime.now().plusDays(1));

        List<String> filteredRooms = manager.filterByUser("user2");

        assertEquals(1, filteredRooms.size());
        assertTrue(filteredRooms.get(0).contains("room2"));
    }

    @Test
    void testFilterByUser_NoBookingsByUser() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.addWorkspaceToConferenceRoom("room1", "workspace1");

        List<String> filteredRooms = manager.filterByUser("NonExistingUser");

        assertTrue(filteredRooms.isEmpty());
    }

    @Test
    void testFilterByAvailableWorkspaces() {
        ConferenceRoomManager manager = new ConferenceRoomManager();
        manager.createConferenceRoom("room1");
        manager.createConferenceRoom("room2");

        manager.addWorkspaceToConferenceRoom("room1", "workspace1");
        manager.addWorkspaceToConferenceRoom("room2", "workspace2");

        assertTrue(manager.filterByAvailableWorkspaces().contains("room1 has available workspaces"));
        assertTrue(manager.filterByAvailableWorkspaces().contains("room2 has available workspaces"));

        manager.bookWorkspace("room1", "workspace1", "user1", LocalDateTime.now().plusHours(1));

        assertFalse(manager.filterByAvailableWorkspaces().contains("room1 has available workspaces"));
    }
}