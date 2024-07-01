package ru.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookingTest {

    private String bookedBy;
    private LocalDateTime bookingTime;
    private int bookingDurationHours;
    private ZoneId zoneId;

    @BeforeEach
    public void setUp() {
        bookedBy = "testUser";
        bookingTime = LocalDateTime.of(2024, 7, 7, 12, 0);
        bookingDurationHours = 2;
        zoneId = ZoneId.systemDefault();
    }

    @Test
    public void testBookingConstructorWithValidArguments() {
        Booking booking = new Booking(bookedBy, bookingTime, bookingDurationHours);

        assertThat(booking.getBookedBy()).isEqualTo(bookedBy);
        assertThat(booking.getBookingTime()).isEqualTo(bookingTime);
        assertThat(booking.getBookingDurationHours()).isEqualTo(bookingDurationHours);
        assertThat(booking.getZoneId()).isEqualTo(zoneId);
    }

    @Test
    public void testBookingConstructorWithNullArguments() {
        assertThatThrownBy(() -> new Booking(null, bookingTime, bookingDurationHours))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Booked by User and Booking time cannot be null");

        assertThatThrownBy(() -> new Booking(bookedBy, null, bookingDurationHours))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Booked by User and Booking time cannot be null");
    }

    @Test
    public void testIsExpiredWhenNotExpired() {
        Booking booking = new Booking(bookedBy, bookingTime, bookingDurationHours);
        LocalDateTime futureTime = bookingTime.plusHours(1);

        assertThat(futureTime.isAfter(bookingTime)).isTrue();
        assertThat(booking.isExpired()).isFalse();
    }

    @Test
    public void testIsExpiredWhenExpired() {
        Booking booking = new Booking(bookedBy, bookingTime, bookingDurationHours);
        LocalDateTime pastTime = bookingTime.plusHours(bookingDurationHours + 1);

        assertThat(pastTime.isAfter(bookingTime)).isTrue();
        assertThat(booking.isExpired()).isTrue();
    }

    @Test
    public void testGetFormattedEndTime() {
        Booking booking = new Booking(bookedBy, bookingTime, bookingDurationHours);
        String expectedEndTime = bookingTime.plusHours(bookingDurationHours).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        assertThat(booking.getFormattedEndTime()).isEqualTo(expectedEndTime);
    }

    @Test
    public void testGetFormattedEndTimeWithNullBookingTime() {
        Booking booking = new Booking(bookedBy, bookingTime, bookingDurationHours);
        booking.setBookingTime(null);

        assertThat(booking.getFormattedEndTime()).isNull();
    }
}