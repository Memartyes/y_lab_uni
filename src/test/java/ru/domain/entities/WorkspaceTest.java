//package ru.domain.entities;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class WorkspaceTest {
//    private Workspace workspace;
//
//    @BeforeEach
//    public void setUp() {
//        workspace = new Workspace("Workspace 1");
//    }
//
//    @Test
//    public void testConstructor() {
//        assertThat(workspace.getName()).isEqualTo("Workspace 1");
//        assertThat(workspace.getBookings()).isEmpty();
//    }
//
//    @Test
//    public void testBookWorkspace() {
//        LocalDateTime bookingTime = LocalDateTime.now();
//        workspace.book("user1", bookingTime, 2);
//
//        assertThat(workspace.getBookings()).hasSize(1);
//        assertThat(workspace.getBookings().get(0).getBookedBy()).isEqualTo("user1");
//        assertThat(workspace.getBookings().get(0).getBookingTime()).isEqualTo(bookingTime);
//    }
//
//    @Test
//    public void testCancelBooking() {
//        LocalDateTime bookingTime = LocalDateTime.now();
//        workspace.book("user1", bookingTime, 2);
//
//        workspace.cancelBooking();
//
//        assertThat(workspace.getBookings()).isEmpty();
//    }
//
//    @Test
//    public void testRemoveBooking() {
//        LocalDateTime bookingTime = LocalDateTime.now();
//        workspace.book("user1", bookingTime, 2);
//
//        workspace.removeBooking(bookingTime);
//
//        assertThat(workspace.getBookings()).isEmpty();
//    }
//
//    @Test
//    public void testRemoveBookingWithInvalidTime() {
//        LocalDateTime bookingTime = LocalDateTime.now();
//        workspace.book("user1", bookingTime, 2);
//
//        LocalDateTime invalidTime = bookingTime.plusHours(1);
//
//        assertThrows(IllegalArgumentException.class, () -> workspace.removeBooking(invalidTime));
//    }
//
//    @Test
//    public void testIsBooked() {
//        assertThat(workspace.isBooked()).isFalse();
//
//        LocalDateTime bookingTime = LocalDateTime.now();
//        workspace.book("user1", bookingTime, 2);
//
//        assertThat(workspace.isBooked()).isTrue();
//    }
//
//    @Test
//    public void testGetBookingEndTime() {
//        LocalDateTime bookingTime = LocalDateTime.now();
//        workspace.book("user1", bookingTime, 2);
//
//        String expectedEndTime = bookingTime.plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//        assertThat(workspace.getBookingEndTime()).isEqualTo(expectedEndTime);
//    }
//
//    @Test
//    public void testGetBookingEndTimeWithNoBookings() {
//        assertThat(workspace.getBookingEndTime()).isNull();
//    }
//}