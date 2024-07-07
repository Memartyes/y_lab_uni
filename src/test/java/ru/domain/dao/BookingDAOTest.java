//package ru.domain.dao;
//
//import liquibase.Contexts;
//import liquibase.Liquibase;
//import liquibase.database.Database;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.resource.ClassLoaderResourceAccessor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.domain.dao.impl.BookingDAOImpl;
//import ru.domain.entities.Booking;
//import ru.domain.util.jdbc.DatabaseUtil;
//
//import java.sql.Connection;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@Testcontainers
//public class BookingDAOTest {
//
//    @Container
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass");
//
//    private BookingDAO bookingDAO;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        DatabaseUtil.setUrl(postgresContainer.getJdbcUrl());
//        DatabaseUtil.setUser(postgresContainer.getUsername());
//        DatabaseUtil.setPassword(postgresContainer.getPassword());
//
//        try (Connection connection = DatabaseUtil.getConnection()) {
//            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
//            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
//            liquibase.update(new Contexts());
//        }
//
//        bookingDAO = new BookingDAOImpl();
//    }
//
//    @Test
//    @DisplayName("Add a new booking")
//    void testAddBooking() {
//        Booking booking = new Booking("user1", LocalDateTime.now(), 2);
//        booking.setWorkspaceId(1);
//        bookingDAO.addBooking(booking);
//
//        Optional<Booking> retrievedBooking = bookingDAO.findBookingById(booking.getId());
//        assertTrue(retrievedBooking.isPresent());
//        assertThat(retrievedBooking.get().getBookedBy()).isEqualTo("user1");
//    }
//
//    @Test
//    @DisplayName("Find a booking by ID")
//    void testFindBookingById() {
//        Booking booking = new Booking("user2", LocalDateTime.now(), 3);
//        booking.setWorkspaceId(2);
//        bookingDAO.addBooking(booking);
//
//        Optional<Booking> retrievedBooking = bookingDAO.findBookingById(booking.getId());
//        assertTrue(retrievedBooking.isPresent());
//        assertThat(retrievedBooking.get().getBookedBy()).isEqualTo("user2");
//    }
//
//    @Test
//    @DisplayName("Find all bookings")
//    void testFindAllBookings() {
//        bookingDAO.addBooking(new Booking("user3", LocalDateTime.now(), 4));
//        bookingDAO.addBooking(new Booking("user4", LocalDateTime.now(), 5));
//
//        List<Booking> bookings = bookingDAO.findAllBookings();
//        assertThat(bookings).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("Update a booking")
//    void testUpdateBooking() {
//        Booking booking = new Booking("user5", LocalDateTime.now(), 6);
//        booking.setWorkspaceId(3);
//        bookingDAO.addBooking(booking);
//
//        booking.setBookedBy("updatedUser");
//        bookingDAO.updateBooking(booking);
//
//        Optional<Booking> updatedBooking = bookingDAO.findBookingById(booking.getId());
//        assertTrue(updatedBooking.isPresent());
//        assertThat(updatedBooking.get().getBookedBy()).isEqualTo("updatedUser");
//    }
//
//    @Test
//    @DisplayName("Delete a booking")
//    void testDeleteBooking() {
//        Booking booking = new Booking("user6", LocalDateTime.now(), 7);
//        booking.setWorkspaceId(4);
//        bookingDAO.addBooking(booking);
//
//        bookingDAO.deleteBooking(booking.getId());
//
//        Optional<Booking> deletedBooking = bookingDAO.findBookingById(booking.getId());
//        assertThat(deletedBooking).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Find bookings by workspace ID")
//    void testFindBookingsByWorkspaceId() {
//        Booking booking1 = new Booking("user7", LocalDateTime.now(), 8);
//        booking1.setWorkspaceId(5);
//        Booking booking2 = new Booking("user8", LocalDateTime.now(), 9);
//        booking2.setWorkspaceId(5);
//
//        bookingDAO.addBooking(booking1);
//        bookingDAO.addBooking(booking2);
//
//        List<Booking> bookings = bookingDAO.findBookingsByWorkspaceId(5);
//        assertThat(bookings).hasSize(2);
//    }
//}
