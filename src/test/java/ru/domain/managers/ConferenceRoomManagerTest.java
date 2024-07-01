package ru.domain.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.Workspace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConferenceRoomManagerTest {
    private ConferenceRoomManager conferenceRoomManager;
    private Map<String, ConferenceRoom> conferenceRooms;
    private ConferenceRoomFilter conferenceRoomFilter;

    @BeforeEach
    public void setUp() {
        conferenceRoomManager = new ConferenceRoomManager();
        conferenceRooms = conferenceRoomManager.getConferenceRooms();
        conferenceRoomFilter = mock(ConferenceRoomFilter.class);
        conferenceRoomManager.setConferenceRoomFilter(conferenceRoomFilter);
    }

    @Test
    public void testAddConferenceRoom() {
        String roomName = "Test Room";
        conferenceRoomManager.addConferenceRoom(roomName);

        assertThat(conferenceRooms).containsKey(roomName);
        assertThat(conferenceRooms.get(roomName).getName()).isEqualTo(roomName);
    }

    @Test
    public void testAddConferenceRoomAlreadyExists() {
        String roomName = "Test Room";
        conferenceRoomManager.addConferenceRoom(roomName);

        assertThatThrownBy(() -> conferenceRoomManager.addConferenceRoom(roomName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    public void testUpdateConferenceRoomName() {
        String oldRoomName = "Old Room";
        String newRoomName = "New Room";
        conferenceRoomManager.addConferenceRoom(oldRoomName);

        conferenceRoomManager.updateConferenceRoomName(oldRoomName, newRoomName);

        assertThat(conferenceRooms).containsKey(newRoomName);
        assertThat(conferenceRooms).doesNotContainKey(oldRoomName);
    }

    @Test
    public void testUpdateConferenceRoomNameOldRoomNotFound() {
        String oldRoomName = "Old Room";
        String newRoomName = "New Room";

        assertThatThrownBy(() -> conferenceRoomManager.updateConferenceRoomName(oldRoomName, newRoomName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void testUpdateConferenceRoomNameNewRoomAlreadyExists() {
        String oldRoomName = "Old Room";
        String newRoomName = "New Room";
        conferenceRoomManager.addConferenceRoom(oldRoomName);
        conferenceRoomManager.addConferenceRoom(newRoomName);

        assertThatThrownBy(() -> conferenceRoomManager.updateConferenceRoomName(oldRoomName, newRoomName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    public void testDeleteConferenceRoom() {
        String roomName = "Test Room";
        conferenceRoomManager.addConferenceRoom(roomName);

        conferenceRoomManager.deleteConferenceRoom(roomName);

        assertThat(conferenceRooms).doesNotContainKey(roomName);
    }

    @Test
    public void testDeleteConferenceRoomNotFound() {
        String roomName = "Test Room";

        assertThatThrownBy(() -> conferenceRoomManager.deleteConferenceRoom(roomName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void testGetConferenceRoom() {
        String roomName = "Test Room";
        conferenceRoomManager.addConferenceRoom(roomName);

        ConferenceRoom room = conferenceRoomManager.getConferenceRoom(roomName);

        assertThat(room).isNotNull();
        assertThat(room.getName()).isEqualTo(roomName);
    }

    @Test
    public void testGetConferenceRoomNotFound() {
        String roomName = "Test Room";

        assertThatThrownBy(() -> conferenceRoomManager.getConferenceRoom(roomName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void testAddWorkspaceToConferenceRoom() {
        String roomName = "Test Room";
        String workspaceName = "Test Workspace";
        conferenceRoomManager.addConferenceRoom(roomName);

        conferenceRoomManager.addWorkspaceToConferenceRoom(roomName, workspaceName);

        ConferenceRoom room = conferenceRooms.get(roomName);
        Optional<Workspace> workspace = room.getWorkspace(workspaceName);

        assertThat(workspace).isPresent();
        assertThat(workspace.get().getName()).isEqualTo(workspaceName);
    }

    @Test
    public void testAddWorkspaceToConferenceRoomWhereRoomNotFound() {
        String roomName = "Test Room";
        String workspaceName = "Test Workspace";

        assertThatThrownBy(() -> conferenceRoomManager.addWorkspaceToConferenceRoom(roomName, workspaceName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void testAddWorkspaceToConferenceRoomWhereRoomAlreadyExists() {
        String roomName = "Test Room";
        String workspaceName = "Test Workspace";
        conferenceRoomManager.addConferenceRoom(roomName);
        conferenceRoomManager.addWorkspaceToConferenceRoom(roomName, workspaceName);

        assertThatThrownBy(() -> conferenceRoomManager.addWorkspaceToConferenceRoom(roomName, workspaceName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    public void testBookWholeConferenceRoom() {
        String roomName = "Test Room";
        String userName = "Test User";
        LocalDateTime bookingTime = LocalDateTime.now();
        conferenceRoomManager.addConferenceRoom(roomName);

        ConferenceRoom room = conferenceRooms.get(roomName);
        Workspace workspace1 = new Workspace("Workspace 1");
        Workspace workspace2 = new Workspace("Workspace 2");
        room.addWorkspace(workspace1);
        room.addWorkspace(workspace2);

        conferenceRoomManager.bookWholeConferenceRoom(roomName, userName, bookingTime);

        assertThat(workspace1.isBooked()).isTrue();
        assertThat(workspace2.isBooked()).isTrue();
    }

    @Test
    public void testBookWorkspace() {
        String roomName = "Test Room";
        String workspaceName = "Test Workspace";
        String userName = "Test User";
        LocalDateTime bookingTime = LocalDateTime.now();
        conferenceRoomManager.addConferenceRoom(roomName);

        ConferenceRoom room = conferenceRooms.get(roomName);
        Workspace workspace = new Workspace(workspaceName);
        room.addWorkspace(workspace);

        conferenceRoomManager.bookWorkspace(roomName, workspaceName, userName, bookingTime, 1);

        assertThat(workspace.isBooked()).isTrue();
    }

    @Test
    public void testBookWorkspaceRoomNotFound() {
        String roomName = "Test Room";
        String workspaceName = "Test Workspace";
        String userName = "Test User";
        LocalDateTime bookingTime = LocalDateTime.now();

        assertThatThrownBy(() -> conferenceRoomManager.bookWorkspace(roomName, workspaceName, userName, bookingTime, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void testBookWorkspaceAlreadyBooked() {
        String roomName = "Test Room";
        String workspaceName = "Test Workspace";
        String userName = "Test User";
        LocalDateTime bookingTime = LocalDateTime.now();
        conferenceRoomManager.addConferenceRoom(roomName);

        ConferenceRoom room = conferenceRooms.get(roomName);
        Workspace workspace = new Workspace(workspaceName);
        room.addWorkspace(workspace);
        workspace.book(userName, bookingTime, 1);

        assertThatThrownBy(() -> conferenceRoomManager.bookWorkspace(roomName, workspaceName, userName, bookingTime, 1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already booked");
    }

    @Test
    public void testCancelBookingForWorkspace() {
        String roomName = "Test Room";
        String workspaceName = "Test Workspace";
        String userName = "Test User";
        LocalDateTime bookingTime = LocalDateTime.now();
        conferenceRoomManager.addConferenceRoom(roomName);

        ConferenceRoom room = conferenceRooms.get(roomName);
        Workspace workspace = new Workspace(workspaceName);
        room.addWorkspace(workspace);
        workspace.book(userName, bookingTime, 1);

        conferenceRoomManager.cancelBookingForWorkspace(roomName, workspaceName);

        assertThat(workspace.isBooked()).isFalse();
    }

    @Test
    public void testCancelBookingForAllWorkspaces() {
        String roomName = "Test Room";
        String userName = "Test User";
        LocalDateTime bookingTime = LocalDateTime.now();
        conferenceRoomManager.addConferenceRoom(roomName);

        ConferenceRoom room = conferenceRooms.get(roomName);
        Workspace workspace1 = new Workspace("Workspace 1");
        Workspace workspace2 = new Workspace("Workspace 2");
        room.addWorkspace(workspace1);
        room.addWorkspace(workspace2);
        workspace1.book(userName, bookingTime, 1);
        workspace2.book(userName, bookingTime, 1);

        conferenceRoomManager.cancelBookingForAllWorkspaces(roomName);

        assertThat(workspace1.isBooked()).isFalse();
        assertThat(workspace2.isBooked()).isFalse();
    }

    @Test
    public void testGetAvailableSlots() {
        String roomName = "Test Room";
        conferenceRoomManager.addConferenceRoom(roomName);
        ConferenceRoom room = conferenceRooms.get(roomName);
        Workspace workspace = new Workspace("Workspace 1");
        room.addWorkspace(workspace);
        LocalDate date = LocalDate.now();

        List<String> availableSlots = conferenceRoomManager.getAvailableSlots(roomName, date);

        assertThat(availableSlots).isNotNull();
    }

    @Test
    public void testFilterByDate() {
        LocalDate date = LocalDate.now();
        List<String> expectedResults = Arrays.asList("Room 1 has booking on " + date, "Room 2 has booking on " + date);

        when(conferenceRoomFilter.filterByDate(date)).thenReturn(expectedResults);

        List<String> results = conferenceRoomManager.filterByDate(date);

        assertThat(results).isEqualTo(expectedResults);
    }

    @Test
    public void testFilterByUser() {
        String userId = "Test User";
        List<String> expectedResults = Arrays.asList("Room 1 has booking by user " + userId, "Room 2 has booking by user " + userId);

        when(conferenceRoomFilter.filterByUser(userId)).thenReturn(expectedResults);

        List<String> results = conferenceRoomManager.filterByUser(userId);

        assertThat(results).isEqualTo(expectedResults);
    }

    @Test
    public void testFilterByAvailableWorkspaces() {
        List<String> expectedResults = Arrays.asList("Room 1 has available workspaces", "Room 2 has available workspaces");

        when(conferenceRoomFilter.filterByAvailableWorkspaces()).thenReturn(expectedResults);

        List<String> results = conferenceRoomManager.filterByAvailableWorkspaces();

        assertThat(results).isEqualTo(expectedResults);
    }
}