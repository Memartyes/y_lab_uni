//package ru.domain.entities;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import ru.domain.managers.WorkspaceManager;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//class ConferenceRoomTest {
//
//    private ConferenceRoom conferenceRoom;
//    private String roomName = "ConferenceRoom1";
//    private int capacity = 5;
//
//    @Mock
//    private WorkspaceManager workspaceManagerMock;
//    @Mock
//    private BookingWorkspaceManager bookingWorkspaceManagerMock;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        conferenceRoom = new ConferenceRoom(roomName, capacity);
//        conferenceRoom.setWorkspaceManager(workspaceManagerMock);
//        conferenceRoom.setBookingWorkspaceManager(bookingWorkspaceManagerMock);
//
//        List<Workspace> mockWorkspaces = new ArrayList<>();
//        for (int i = 1; i <= capacity; i++) {
//            Workspace workspace = new Workspace(String.valueOf(i));
//            mockWorkspaces.add(workspace);
//        }
//        when(workspaceManagerMock.initializeWorkspaces(capacity)).thenReturn(mockWorkspaces);
//        conferenceRoom.setWorkspaces(mockWorkspaces);
//    }
//
//    @Test
//    public void testGetWorkspace() {
//        Optional<Workspace> workspace = conferenceRoom.getWorkspace("1");
//
//        assertThat(workspace).isPresent();
//        assertThat(workspace.get().getName()).isEqualTo("1");
//
//        Optional<Workspace> nonExistentWorkspace = conferenceRoom.getWorkspace("nonexistent");
//
//        assertThat(nonExistentWorkspace).isNotPresent();
//    }
//
//    @Test
//    public void testAddWorkspace() {
//        Workspace newWorkspace = new Workspace("6");
//        conferenceRoom.addWorkspace(newWorkspace);
//
//        assertThat(conferenceRoom.getWorkspace("6")).isPresent();
//        assertThat(conferenceRoom.getWorkspace("6").get().getName()).isEqualTo("6");
//
//        assertThatThrownBy(() -> conferenceRoom.addWorkspace(new Workspace("6")))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Workspace with name 6 already exists.");
//    }
//
//    @Test
//    public void testGetAvailableWorkspaceCount() {
//        assertThat(conferenceRoom.getAvailableWorkspaceCount()).isEqualTo(capacity);
//    }
//
//    @Test
//    public void testBookAllWorkspaces() {
//        String userName = "testUser";
//        LocalDateTime bookingTime = LocalDateTime.now();
//
//        conferenceRoom.bookAllWorkspaces(userName, bookingTime);
//
//        verify(bookingWorkspaceManagerMock, times(1)).bookAllWorkspaces(userName, bookingTime);
//    }
//
//    @Test
//    public void testCancelBookingForWorkspace() {
//        String workspaceName = "1";
//
//        conferenceRoom.cancelBookingForWorkspace(workspaceName);
//
//        verify(bookingWorkspaceManagerMock, times(1)).cancelBookingForWorkspace(workspaceName);
//    }
//
//    @Test
//    public void testIsBookingTimeAvailable() {
//        LocalDateTime dateTime = LocalDateTime.now();
//
//        when(bookingWorkspaceManagerMock.isBookingTimeAvailable(dateTime)).thenReturn(true);
//
//        assertThat(conferenceRoom.isBookingTimeAvailable(dateTime)).isTrue();
//
//        when(bookingWorkspaceManagerMock.isBookingTimeAvailable(dateTime)).thenReturn(false);
//
//        assertThat(conferenceRoom.isBookingTimeAvailable(dateTime)).isFalse();
//    }
//
//    @Test
//    public void testCancelBookingForAllWorkspaces() {
//        conferenceRoom.cancelBookingForAllWorkspaces();
//
//        verify(bookingWorkspaceManagerMock, times(1)).cancelBookingForAllWorkspaces();
//    }
//
//    @Test
//    public void testHasBookingOnDate() {
//        LocalDate date = LocalDate.now();
//
//        when(bookingWorkspaceManagerMock.hasBookingOnDate(date)).thenReturn(true);
//
//        assertThat(conferenceRoom.hasBookingOnDate(date)).isTrue();
//
//        when(bookingWorkspaceManagerMock.hasBookingOnDate(date)).thenReturn(false);
//
//        assertThat(conferenceRoom.hasBookingOnDate(date)).isFalse();
//    }
//
//    @Test
//    public void testHasBookingByUser() {
//        String userId = "testUser";
//
//        when(bookingWorkspaceManagerMock.hasBookingByUser(userId)).thenReturn(true);
//
//        assertThat(conferenceRoom.hasBookingByUser(userId)).isTrue();
//
//        when(bookingWorkspaceManagerMock.hasBookingByUser(userId)).thenReturn(false);
//
//        assertThat(conferenceRoom.hasBookingByUser(userId)).isFalse();
//    }
//
//    @Test
//    public void testHasAvailableWorkspaces() {
//        when(workspaceManagerMock.getWorkspaces()).thenReturn(conferenceRoom.getWorkspaces());
//
//        assertThat(conferenceRoom.hasAvailableWorkspaces()).isTrue();
//
//        conferenceRoom.getWorkspaces().forEach(workspace -> workspace.setBooked(true));
//
//        assertThat(conferenceRoom.hasAvailableWorkspaces()).isFalse();
//    }
//}