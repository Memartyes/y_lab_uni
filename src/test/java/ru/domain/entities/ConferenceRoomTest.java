package ru.domain.entities;

import ru.domain.managers.ConferenceRoomManager;
import ru.domain.config.WorkspaceConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConferenceRoomTest {

    @Mock
    private ConferenceRoom conferenceRoom;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        conferenceRoom = new ConferenceRoom("room1");
    }

    @Test
    public void testInitializeWorkspace() {
        Workspace workspace1 = new Workspace("workspace1");
        conferenceRoom.addWorkspace(workspace1);
        assertThat(conferenceRoom.getWorkspaces()).contains(workspace1);
    }

    @Test
    public void testGetName() {
        String name = conferenceRoom.getName();
        assertThat(name).isEqualTo("room1");
    }

    @Test
    public void testSetName() {
        conferenceRoom.setName("newRoom");
        assertThat(conferenceRoom.getName()).isEqualTo("newRoom");
    }

    @Test
    public void testGetWorkspace() {
        Workspace workspace = new Workspace("workspace1");
        conferenceRoom.addWorkspace(workspace);
        Workspace retrievedWorkspace = conferenceRoom.getWorkspace("workspace1");
        assertThat(retrievedWorkspace).isEqualTo(workspace);
    }

    @Test
    public void testGetWorkspace_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> conferenceRoom.getWorkspace("workspace1"));
    }

    @Test
    public void testGetWorkspaces() {
        Workspace workspace1 = new Workspace("workspace1");
        Workspace workspace2 = new Workspace("workspace2");
        conferenceRoom.addWorkspace(workspace1);
        conferenceRoom.addWorkspace(workspace2);
        List<Workspace> workspaces = conferenceRoom.getWorkspaces();
        assertThat(workspaces).contains(workspace1, workspace2);
    }

    @Test
    public void testAddWorkspace_SuccessfullyAdded() {
        Workspace workspace = new Workspace("workspace1");

        conferenceRoom.addWorkspace(workspace);

        assertThat(conferenceRoom.getWorkspaces()).contains(workspace);
        assertThat(conferenceRoom.getAvailableWorkspaceCount()).isEqualTo(WorkspaceConfig.WORKSPACES_CAPACITY.getValue() + 1);
    }

    @Test
    public void testAddWorkspace_NullWorkspace() {
        assertThrows(IllegalArgumentException.class, () -> conferenceRoom.addWorkspace(null));
    }

    @Test
    public void testAddWorkspace_DuplicateWorkspace() {
        Workspace workspace1 = new Workspace("workspace1");
        Workspace workspace2 = new Workspace("workspace1");

        conferenceRoom.addWorkspace(workspace1);

        assertThrows(IllegalArgumentException.class, () -> conferenceRoom.addWorkspace(workspace2));
        assertThat(conferenceRoom.getAvailableWorkspaceCount()).isEqualTo(WorkspaceConfig.WORKSPACES_CAPACITY.getValue() + 1);
    }

    @Test
    public void testGetAvailableWorkspaceCount() {
        Workspace workspace1 = new Workspace("workspace1");
        Workspace workspace2 = new Workspace("workspace2");
        conferenceRoom.addWorkspace(workspace1);
        conferenceRoom.addWorkspace(workspace2);
        int count = conferenceRoom.getAvailableWorkspaceCount();
        assertThat(count).isEqualTo(WorkspaceConfig.WORKSPACES_CAPACITY.getValue() + 2);
    }

    @Test
    public void testIsBookingTimeAvailable() {
        Workspace workspace1 = new Workspace("workspace1");
        conferenceRoom.addWorkspace(workspace1);
        LocalDateTime bookingTime = LocalDateTime.now();
        workspace1.book("user1", bookingTime);

        boolean available = conferenceRoom.isBookingTimeAvailable(bookingTime);

        assertThat(available).isFalse();
    }

    @Test
    public void testGetAvailableSlots() {
        Workspace workspace1 = new Workspace("workspace1");
        Workspace workspace2 = new Workspace("workspace2");
        conferenceRoom.addWorkspace(workspace1);
        conferenceRoom.addWorkspace(workspace2);

        List<String> availableSlots = conferenceRoom.getAvailableSlots(LocalDateTime.now());

        assertThat(availableSlots).isNotEmpty();
    }

    @Test
    public void testHasBookingOnDate() {
        Workspace workspace = new Workspace("workspace1");
        conferenceRoom.addWorkspace(workspace);
        LocalDateTime bookingTime = LocalDateTime.now();
        workspace.book("user1", bookingTime);
        boolean hasBooking = conferenceRoom.hasBookingOnDate(bookingTime.toLocalDate());
        assertThat(hasBooking).isTrue();
    }

    @Test
    public void testHasBookingByUser() {
        Workspace workspace = new Workspace("workspace1");
        conferenceRoom.addWorkspace(workspace);
        workspace.book("user1", LocalDateTime.now());
        boolean hasBooking = conferenceRoom.hasBookingByUser("user1");
        assertThat(hasBooking).isTrue();
    }

    @Test
    public void testHasAvailableWorkspaces() {
        Workspace workspace1 = new Workspace("workspace1");
        conferenceRoom.addWorkspace(workspace1);
        boolean hasAvailable = conferenceRoom.hasAvailableWorkspaces();
        assertThat(hasAvailable).isTrue();
    }

    @Test
    public void testBookAllWorkspaces() {
        Workspace workspace1 = new Workspace("workspace1");
        Workspace workspace2 = new Workspace("workspace2");
        conferenceRoom.addWorkspace(workspace1);
        conferenceRoom.addWorkspace(workspace2);

        conferenceRoom.bookAllWorkspaces("user1", LocalDateTime.now());

        assertThat(workspace1.isBooked()).isTrue();
        assertThat(workspace2.isBooked()).isTrue();
        assertThat(workspace1.getBookedBy()).isEqualTo("user1");
        assertThat(workspace2.getBookedBy()).isEqualTo("user1");
    }

    @Test
    public void testCancelBookingForWorkspace() {
        Workspace workspace = new Workspace("workspace1");
        conferenceRoom.addWorkspace(workspace);
        workspace.book("user1", LocalDateTime.now());

        conferenceRoom.cancelBookingForWorkspace("workspace1");

        assertThat(workspace.isBooked()).isFalse();
    }

    @Test
    public void testCancelBookingForAllWorkspaces() {
        Workspace workspace1 = new Workspace("workspace1");
        Workspace workspace2 = new Workspace("workspace2");
        conferenceRoom.addWorkspace(workspace1);
        conferenceRoom.addWorkspace(workspace2);
        workspace1.book("user1", LocalDateTime.now());
        workspace2.book("user2", LocalDateTime.now());

        conferenceRoom.cancelBookingForAllWorkspaces();

        assertThat(workspace1.isBooked()).isFalse();
        assertThat(workspace2.isBooked()).isFalse();
    }
}