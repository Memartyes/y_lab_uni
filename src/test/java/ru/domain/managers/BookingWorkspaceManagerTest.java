//package ru.domain.managers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.domain.config.WorkspaceConfig;
//import ru.domain.entities.Workspace;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//class BookingWorkspaceManagerTest {
//
//    private BookingWorkspaceManager bookingWorkspaceManager;
//    private List<Workspace> workspaceList;
//
//    @BeforeEach
//    public void setUp() {
//        workspaceList = new ArrayList<>();
//        bookingWorkspaceManager = new BookingWorkspaceManager(workspaceList);
//    }
//
//    @Test
//    public void testBookAllWorkspaces() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspaceList.add(workspace1);
//        workspaceList.add(workspace2);
//
//        LocalDateTime bookingTime = LocalDateTime.of(2024, 7, 7, 10, 0);
//        bookingWorkspaceManager.bookAllWorkspaces("user1", bookingTime);
//
//        assertThat(workspace1.isBooked()).isTrue();
//        assertThat(workspace2.isBooked()).isTrue();
//        assertThat(workspace1.getBookedBy()).isEqualTo("user1");
//        assertThat(workspace2.getBookedBy()).isEqualTo("user1");
//    }
//
//    @Test
//    public void testIsBookingTimeAvailable() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        workspaceList.add(workspace1);
//        LocalDateTime bookingTime = LocalDateTime.of(2024, 7, 7, 10, 0);
//        workspace1.book("user1", bookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//
//        boolean isAvailable = bookingWorkspaceManager.isBookingTimeAvailable(bookingTime);
//
//        assertThat(isAvailable).isFalse();
//    }
//
//    @Test
//    public void testCancelBookingForWorkspace() {
//        Workspace workspace = new Workspace("Workspace 1");
//        workspace.book("user1", LocalDateTime.of(2024, 7, 7, 10, 0), WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//        workspaceList.add(workspace);
//
//        bookingWorkspaceManager.cancelBookingForWorkspace("Workspace 1");
//
//        assertThat(workspace.isBooked()).isFalse();
//    }
//
//    @Test
//    public void testGetAvailableSlots() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspaceList.add(workspace1);
//        workspaceList.add(workspace2);
//        LocalDateTime date = LocalDateTime.of(2024, 7, 7, 0, 0);
//
//        List<String> availableSlots = bookingWorkspaceManager.getAvailableSlots(date);
//
//        assertThat(availableSlots).isNotEmpty();
//    }
//
//    @Test
//    public void testGetAvailableWorkspaces() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspaceList.add(workspace1);
//        workspaceList.add(workspace2);
//        workspace1.book("user1", LocalDateTime.of(2024, 7, 7, 10, 0), WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//
//        List<Workspace> availableWorkspaces = bookingWorkspaceManager.getAvailableWorkspaces();
//
//        assertThat(availableWorkspaces).contains(workspace2);
//        assertThat(availableWorkspaces).doesNotContain(workspace1);
//    }
//
//    @Test
//    public void testCancelBookingForAllWorkspaces() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspaceList.add(workspace1);
//        workspaceList.add(workspace2);
//        workspace1.book("user1", LocalDateTime.of(2027, 7, 4, 10, 0), WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//        workspace2.book("user1", LocalDateTime.of(2027, 7, 4, 10, 0), WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//
//        bookingWorkspaceManager.cancelBookingForAllWorkspaces();
//
//        assertThat(workspace1.isBooked()).isFalse();
//        assertThat(workspace2.isBooked()).isFalse();
//    }
//
//    @Test
//    public void testUpdateSpecificBooking() {
//        Workspace workspace = new Workspace("Workspace 1");
//        workspaceList.add(workspace);
//        LocalDateTime initialBookingTime = LocalDateTime.of(2024, 7, 7, 10, 0);
//        workspace.book("user1", initialBookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//        LocalDateTime newBookingTime = LocalDateTime.of(2024, 7, 8, 10, 0);
//
//        bookingWorkspaceManager.updateSpecificBooking("Workspace 1", "user1", newBookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//
//        assertThat(workspace.getBookingTime()).isEqualTo(newBookingTime);
//    }
//
//    @Test
//    public void testHasBookingByUser() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspaceList.add(workspace1);
//        workspaceList.add(workspace2);
//        workspace1.book("user1", LocalDateTime.of(2024, 7, 7, 10, 0), WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//
//        boolean hasBooking = bookingWorkspaceManager.hasBookingByUser("user1");
//
//        assertThat(hasBooking).isTrue();
//    }
//
//    @Test
//    public void testHasBookingOnDate() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspaceList.add(workspace1);
//        workspaceList.add(workspace2);
//        LocalDateTime bookingTime = LocalDateTime.of(2024, 7, 7, 10, 0);
//        workspace1.book("user1", bookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//
//        boolean hasBooking = bookingWorkspaceManager.hasBookingOnDate(bookingTime.toLocalDate());
//
//        assertThat(hasBooking).isTrue();
//    }
//
//    @Test
//    public void testBookSpecificWorkspace() {
//        Workspace workspace = new Workspace("Workspace 1");
//        workspaceList.add(workspace);
//        LocalDateTime bookingTime = LocalDateTime.of(2024, 7, 7, 10, 0);
//
//        bookingWorkspaceManager.bookSpecificWorkspace("Workspace 1", "user1", bookingTime, WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
//
//        assertThat(workspace.isBooked()).isTrue();
//        assertThat(workspace.getBookedBy()).isEqualTo("user1");
//        assertThat(workspace.getBookingTime()).isEqualTo(bookingTime);
//    }
//}