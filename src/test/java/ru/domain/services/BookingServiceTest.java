//package ru.domain.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import ru.domain.interfaces.Bookable;
//import ru.domain.services.BookingService;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//import static org.assertj.core.api.Assertions.*;
//
//class BookingServiceTest {
//    private BookingService bookingService;
//    private Bookable bookable;
//
//    @BeforeEach
//    public void setUp() {
//        bookingService = new BookingService();
//        bookable = mock(Bookable.class);
//    }
//
//    @Test
//    public void testBook() {
//        String userName = "testUser";
//        LocalDateTime bookingTime = LocalDateTime.of(2024, 7, 7, 10, 0);
//        int bookingDurationHours = 2;
//
//        bookingService.book(bookable, userName, bookingTime, bookingDurationHours);
//
//        verify(bookable, times(1)).book(userName, bookingTime, bookingDurationHours);
//    }
//
//    @Test
//    public void testCancelBooking() {
//        bookingService.cancelBooking(bookable);
//
//        verify(bookable, times(1)).cancelBooking();
//    }
//
//    @Test
//    public void testIsBooked() {
//        when(bookable.isBooked()).thenReturn(true);
//
//        boolean result = bookingService.isBooked(bookable);
//
//        verify(bookable, times(1)).isBooked();
//        assertTrue(result);
//    }
//}