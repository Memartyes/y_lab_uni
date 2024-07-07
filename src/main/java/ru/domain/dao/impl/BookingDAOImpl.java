package ru.domain.dao.impl;

import ru.domain.dao.BookingDAO;
import ru.domain.entities.Booking;
import ru.domain.util.jdbc.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDAOImpl implements BookingDAO {
    private static final String TABLE_NAME = "coworking.\"bookings-liquibase\"";

    /**
     * Добавляем бронирование.
     *
     * @param booking the booking
     */
    @Override
    public void addBooking(Booking booking) {
        String sql = "INSERT INTO " + TABLE_NAME + " (\"workspace_id\", \"booked_by\", \"booking_time\", \"duration_hours\") VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, booking.getWorkspaceId());
            preparedStatement.setString(2, booking.getBookedBy());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(booking.getBookingTime()));
            preparedStatement.setInt(4, booking.getBookingDurationHours());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert row into " + TABLE_NAME);
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to insert row into " + TABLE_NAME);
                }
            }

            System.out.println("Booking added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
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
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE \"id\" = ?";
        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Booking booking = new Booking();
                    booking.setId(resultSet.getInt("id"));
                    booking.setWorkspaceId(resultSet.getInt("workspace_id"));
                    booking.setBookedBy(resultSet.getString("booked_by"));
                    booking.setBookingTime(resultSet.getTimestamp("booking_time").toLocalDateTime());
                    booking.setBookingDurationHours(resultSet.getInt("duration_hours"));

                    return Optional.of(booking);
                }
            }
        } catch (SQLException e ) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Находим все бронирования.
     *
     * @return the list of all bookings
     */
    @Override
    public List<Booking> findAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getInt("id"));
                booking.setWorkspaceId(resultSet.getInt("workspace_id"));
                booking.setBookedBy(resultSet.getString("booked_by"));
                booking.setBookingTime(resultSet.getTimestamp("booking_time").toLocalDateTime());
                booking.setBookingDurationHours(resultSet.getInt("duration_hours"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Обновляем бронирование.
     *
     * @param booking the booking to update
     */
    @Override
    public void updateBooking(Booking booking) {
        String sql = "UPDATE " + TABLE_NAME + " SET \"workspace_id\" = ?, \"booked_by\" = ?, \"booking_time\" = ?, \"duration_hours\" = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, booking.getWorkspaceId());
            preparedStatement.setString(2, booking.getBookedBy());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(booking.getBookingTime()));
            preparedStatement.setInt(4, booking.getBookingDurationHours());
            preparedStatement.setInt(5, booking.getId());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to update row into " + TABLE_NAME);
            }

            System.out.println("Booking updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Удаляем бронирование по ID.
     *
     * @param id the booking ID to delete
     */
    @Override
    public void deleteBooking(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE \"id\" = ?";
        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to delete row into " + TABLE_NAME);
            }

            System.out.println("Booking deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }

    }

    /**
     * Находим бронирования по ID рабочего места.
     *
     * @param workspaceId the workspace ID
     * @return the list of bookings by workspace ID
     */
    @Override
    public List<Booking> findBookingsByWorkspaceId(int workspaceId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE \"workspace_id\" = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, workspaceId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = new Booking();
                    booking.setId(resultSet.getInt("id"));
                    booking.setWorkspaceId(resultSet.getInt("workspace_id"));
                    booking.setBookedBy(resultSet.getString("booked_by"));
                    booking.setBookingTime(resultSet.getTimestamp("booking_time").toLocalDateTime());
                    booking.setBookingDurationHours(resultSet.getInt("duration_hours"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Находим бронирования по имени пользователя.
     *
     * @param bookedBy the user's name
     * @return the list of bookings by user's name
     */
    @Override
    public List<Booking> findBookingsByUser(String bookedBy) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE \"booked_by\" = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, bookedBy);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = new Booking();
                    booking.setId(resultSet.getInt("id"));
                    booking.setWorkspaceId(resultSet.getInt("workspace_id"));
                    booking.setBookedBy(resultSet.getString("booked_by"));
                    booking.setBookingTime(resultSet.getTimestamp("booking_time").toLocalDateTime());
                    booking.setBookingDurationHours(resultSet.getInt("duration_hours"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Находим бронирования на определенную дату.
     *
     * @param date the date of bookings
     * @return the list of bookings on the specified date
     */
    @Override
    public List<Booking> findBookingsByDate(LocalDateTime date) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE \"booking_time\"::date = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(date.toLocalDate()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = new Booking();
                    booking.setId(resultSet.getInt("id"));
                    booking.setWorkspaceId(resultSet.getInt("workspace_id"));
                    booking.setBookedBy(resultSet.getString("booked_by"));
                    booking.setBookingTime(resultSet.getTimestamp("booking_time").toLocalDateTime());
                    booking.setBookingDurationHours(resultSet.getInt("duration_hours"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Отменяем бронирования по имени пользователя.
     *
     * @param bookedBy the user's name
     */
    @Override
    public void cancelBookingsByUser(String bookedBy) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE \"booked_by\" = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, bookedBy);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, workspaceId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(bookingTime));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
