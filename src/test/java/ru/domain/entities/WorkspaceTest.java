package ru.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

class WorkspaceTest {

    private Workspace workspace;
    private final String workspaceId = "workspace1";
    private final String userId = "user1";
    private final LocalDateTime bookingTime = LocalDateTime.of(2024, 6, 23, 10, 0);

    @BeforeEach
    public void setUp() {
        workspace = new Workspace(workspaceId);
    }

    @Test
    public void testGetId() {
        String id = workspace.getId();
        assertThat(id).isEqualTo(workspaceId);
    }

    @Test
    public void testIsBookedWhenNotBooked() {
        boolean isBooked = workspace.isBooked();
        assertThat(isBooked).isFalse();
    }

    @Test
    public void testIsBookedWhenBooked() {
        workspace.book(userId, bookingTime);
        boolean isBooked = workspace.isBooked();
        assertThat(isBooked).isTrue();
    }

    @Test
    public void testBookWhenNotBooked() {
        workspace.book(userId, bookingTime);
        assertThat(workspace.isBooked()).isTrue();
        assertThat(workspace.getBookedBy()).isEqualTo(userId);
        assertThat(workspace.getBookingTime()).isEqualTo(bookingTime);
        assertThat(workspace.getBookingEndTime()).isEqualTo(bookingTime.plusHours(1));
    }

    @Test
    public void testBookWhenAlreadyBooked() {
        workspace.book(userId, bookingTime);
        assertThatThrownBy(() -> workspace.book(userId, bookingTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Workspace is already booked.");
    }

    @Test
    public void testCancelBookingWhenBooked() {
        workspace.book(userId, bookingTime);
        workspace.cancelBooking();
        assertThat(workspace.isBooked()).isFalse();
        assertThat(workspace.getBookedBy()).isNull();
        assertThat(workspace.getBookingTime()).isNull();
        assertThat(workspace.getBookingEndTime()).isNull();
    }

    @Test
    public void testCancelBookingWhenNotBooked() {
        assertThatThrownBy(() -> workspace.cancelBooking())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Workspace is not booked.");
    }

    @Test
    public void testGetBookedByWhenNotBooked() {
        String bookedBy = workspace.getBookedBy();
        assertThat(bookedBy).isNull();
    }

    @Test
    public void testGetBookedByWhenBooked() {
        workspace.book(userId, bookingTime);
        String bookedBy = workspace.getBookedBy();
        assertThat(bookedBy).isEqualTo(userId);
    }

    @Test
    public void testGetBookingTimeWhenNotBooked() {
        LocalDateTime bookingTime = workspace.getBookingTime();
        assertThat(bookingTime).isNull();
    }

    @Test
    public void testGetBookingTimeWhenBooked() {
        workspace.book(userId, bookingTime);
        LocalDateTime returnedBookingTime = workspace.getBookingTime();
        assertThat(returnedBookingTime).isEqualTo(bookingTime);
    }

    @Test
    public void testIsBookingExpiredWhenNotBooked() {
        boolean isExpired = workspace.isBookingExpired();
        assertThat(isExpired).isFalse();
    }

    @Test
    public void testIsBookingExpiredWhenBookingActive() {
        try (var mockedTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime currentTime = bookingTime.plusMinutes(30); // within the booking duration
            mockedTime.when(LocalDateTime::now).thenReturn(currentTime);

            workspace.book(userId, bookingTime);
            boolean isExpired = workspace.isBookingExpired();
            assertThat(isExpired).isFalse();
        }
    }

    @Test
    public void testIsBookingExpiredWhenBookingExpired() {
        try (var mockedTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime currentTime = bookingTime.plusHours(2); // after the booking end time
            mockedTime.when(LocalDateTime::now).thenReturn(currentTime);

            workspace.book(userId, bookingTime);
            boolean isExpired = workspace.isBookingExpired();
            assertThat(isExpired).isTrue();
        }
    }

    @Test
    public void testGetBookingEndTimeWhenNotBooked() {
        String bookingEndTime = workspace.getBookingEndTime();
        assertThat(bookingEndTime).isNull();
    }
}