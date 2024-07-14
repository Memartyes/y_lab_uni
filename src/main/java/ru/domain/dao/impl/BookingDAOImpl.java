package ru.domain.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.domain.dao.BookingDAO;
import ru.domain.entities.Booking;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализвация DAO для бронирований.
 */
@Repository
public class BookingDAOImpl implements BookingDAO {

    private static final String TABLE_NAME = "coworking.\"bookings-liquibase\"";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор с внедрением зависимости DataSource.
     *
     * @param dataSource источник данных
     */
    @Autowired
    public BookingDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Преобразовуем строки результата SQL-запроса в объект Booking.
     */
    private static final class BookingRowMapper implements RowMapper<Booking> {
        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            Booking booking = new Booking();
            booking.setId(rs.getInt("id"));
            booking.setWorkspaceId(rs.getInt("workspace_id"));
            booking.setBookedBy(rs.getString("booked_by"));
            booking.setBookingTime(rs.getTimestamp("booking_time").toLocalDateTime());
            booking.setBookingDurationHours(rs.getInt("duration_hours"));
            return booking;
        }
    }

    /**
     * Добавляем бронирование.
     *
     * @param booking the booking
     */
    @Override
    public void addBooking(Booking booking) {
        String sql = "INSERT INTO " + TABLE_NAME + " (\"workspace_id\", \"booked_by\", \"booking_time\", \"duration_hours\") VALUES (?, ?, ?, ?) RETURNING id";
        Integer newId = jdbcTemplate.queryForObject(sql, new Object[]{booking.getWorkspaceId(), booking.getBookedBy(), booking.getBookingTime(), booking.getBookingDurationHours()}, Integer.class);
        if (newId != null) {
            booking.setId(newId);
        }
    }

    /**
     * Находим бронирование по ID
     *
     * @param id the booking ID
     * @return the Optional object of Booking
     */
    @Override
    public Optional<Booking> findBookingById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        List<Booking> bookings = jdbcTemplate.query(sql, new Object[]{id}, new BookingRowMapper());
        return bookings.isEmpty() ? Optional.empty() : Optional.of(bookings.get(0));
    }

    /**
     * Находим все бронирования.
     *
     * @return the list of all bookings
     */
    @Override
    public List<Booking> findAllBookings() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return jdbcTemplate.query(sql, new BookingRowMapper());
    }

    /**
     * Обновляем бронирование.
     *
     * @param booking the booking to update
     */
    @Override
    public void updateBooking(Booking booking) {
        String sql = "UPDATE " + TABLE_NAME + " SET \"workspace_id\" = ?, \"booked_by\" = ?, \"booking_time\" = ?, \"duration_hours\" = ? WHERE id = ?";
        jdbcTemplate.update(sql, booking.getWorkspaceId(), booking.getBookedBy(), booking.getBookingTime(), booking.getBookingDurationHours(), booking.getId());
    }

    /**
     * Удаляем бронирование по ID.
     *
     * @param id the booking ID to delete
     */
    @Override
    public void deleteBooking(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Находим бронирования по ID рабочего места.
     *
     * @param workspaceId the workspace ID
     * @return the list of bookings by workspace ID
     */
    @Override
    public List<Booking> findBookingsByWorkspaceId(int workspaceId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE \"workspace_id\" = ?";
        return jdbcTemplate.query(sql, new Object[]{workspaceId}, new BookingRowMapper());
    }

    /**
     * Находим бронирования по имени пользователя.
     *
     * @param bookedBy the user's name
     * @return the list of bookings by user's name
     */
    @Override
    public List<Booking> findBookingsByUser(String bookedBy) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE \"booked_by\" = ?";
        return jdbcTemplate.query(sql, new Object[]{bookedBy}, new BookingRowMapper());
    }

    /**
     * Находим бронирования на определенную дату.
     *
     * @param date the date of bookings
     * @return the list of bookings on the specified date
     */
    @Override
    public List<Booking> findBookingsByDate(LocalDateTime date) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE \"booking_time\"::date = ?";
        return jdbcTemplate.query(sql, new Object[]{date.toLocalDate()}, new BookingRowMapper());
    }

    /**
     * Отменяем бронирования по имени пользователя.
     *
     * @param bookedBy the user's name
     */
    @Override
    public void cancelBookingsByUser(String bookedBy) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE \"booked_by\" = ?";
        jdbcTemplate.update(sql, bookedBy);
    }

    /**
     * Проверяем доступность бронирования на определенное время.
     *
     * @param workspaceId the workspace ID
     * @param bookingTime the booking time to check
     * @return true if the booking is available, false otherwise
     */
    @Override
    public boolean isBookingAvailable(int workspaceId, LocalDateTime bookingTime) {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE \"workspace_id\" = ? AND \"booking_time\" = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{workspaceId, bookingTime}, Integer.class);
        return count != null && count == 0;
    }
}
