package ru.domain.dao.impl;

import ru.domain.dao.WorkspaceDAO;
import ru.domain.entities.Booking;
import ru.domain.entities.Workspace;
import ru.domain.util.jdbc.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkspaceDAOImpl implements WorkspaceDAO {
    private static final String CONFERENCE_TABLE_NAME = "coworking.\"conference_rooms-liquibase\"";
    private static final String WORKSPACE_TABLE_NAME = "coworking.\"workspaces-liquibase\"";
    private static final String BOOKING_TABLE_NAME = "coworking.\"bookings-liquibase\"";

    /**
     * Добавляем рабочее место в базу данных.
     *
     * @param workspace the workspace to add
     */
    @Override
    public void addWorkspace(Workspace workspace) {
        String sql = "INSERT INTO " + WORKSPACE_TABLE_NAME + " (name) VALUES (?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, workspace.getName());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating workspace failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    workspace.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating workspace failed, no ID obtained.");
                }
            }

            System.out.println("Created workspace: " + workspace.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Находим рабочее место по ID.
     *
     * @param id the workspace ID
     * @return the workspace
     */
    @Override
    public Optional<Workspace> findWorkspaceById(int id) {
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME + " WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Workspace workspace = new Workspace(resultSet.getString("name"));
                    workspace.setId(resultSet.getInt("id"));

                    List<Booking> bookings = findBookingsByWorkspaceId(workspace.getId());
                    workspace.setBookings(bookings);

                    System.out.println("Found workspace: " + workspace.getName());
                    return Optional.of(workspace);
                }
            }

            System.out.println("Failed to find workspace with ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Находим рабочее место по его названию.
     *
     * @param name the workspace name
     * @return the Optional with Workspace if the workspace found, empty Optional otherwise
     */
    @Override
    public Optional<Workspace> findWorkspaceByName(String name) {
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME + " WHERE name = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Workspace workspace = new Workspace(resultSet.getString("name"));
                    workspace.setId(resultSet.getInt("id"));

                    List<Booking> bookings = findBookingsByWorkspaceId(workspace.getId());
                    workspace.setBookings(bookings);

                    return Optional.of(workspace);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Находим все рабочие места.
     *
     * @return the list of workspaces
     */
    @Override
    public List<Workspace> findAllWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME;
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Workspace workspace = new Workspace(resultSet.getString("name"));
                workspace.setId(resultSet.getInt("id"));

                List<Booking> bookings = findBookingsByWorkspaceId(workspace.getId());
                workspace.setBookings(bookings);

                workspaces.add(workspace);
            }

            System.out.println("Found " + workspaces.size() + " workspaces");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }

        return workspaces;
    }

    /**
     * Обновляем рабочее место
     *
     * @param workspace the workspace object to update
     */
    @Override
    public void updateWorkspace(Workspace workspace) {
        String sql = "UPDATE " + WORKSPACE_TABLE_NAME + " SET name = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setInt(2, workspace.getId());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Удаляем рабочее место по ID
     *
     * @param id the workspace ID
     */
    @Override
    public void deleteWorkspace(int id) {
        String sql = "DELETE FROM " + WORKSPACE_TABLE_NAME + " WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
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
        String sql = "SELECT * FROM " + BOOKING_TABLE_NAME + " WHERE \"workspace_id\" = ?";
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
     * Находим рабочие места, которые доступны для бронирования.
     *
     * @return the list of available workspaces
     */
    @Override
    public List<Workspace> findAvailableWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME +
                " LEFT JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\" WHERE " + BOOKING_TABLE_NAME + ".id IS NULL";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Workspace workspace = new Workspace(resultSet.getString("name"));
                workspace.setId(resultSet.getInt("id"));
                workspaces.add(workspace);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workspaces;
    }

    /**
     * Находим все рабочие места, связанные с определенным конференц-залом.
     *
     * @param conferenceRoomId the conference room ID
     * @return the list of workspaces by conference room ID
     */
    @Override
    public List<Workspace> findWorkspacesByConferenceRoomId(int conferenceRoomId) {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME + " WHERE \"conference_room_id\" = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, conferenceRoomId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Workspace workspace = new Workspace(resultSet.getString("name"));
                    workspace.setId(resultSet.getInt("id"));

                    List<Booking> bookings = findBookingsByWorkspaceId(workspace.getId());
                    workspace.setBookings(bookings);

                    workspaces.add(workspace);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workspaces;
    }

    @Override
    public List<Workspace> findBookedWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME +
                " JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\"";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Workspace workspace = new Workspace(resultSet.getString("name"));
                workspace.setId(resultSet.getInt("id"));

                List<Booking> bookings = findBookingsByWorkspaceId(workspace.getId());
                workspace.setBookings(bookings);

                workspaces.add(workspace);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workspaces;
    }

    /**
     * Проверяем доступность рабочего места на определенное время.
     *
     * @param workspaceId the workspace ID
     * @param bookingTime the booking time
     * @return true if the workspace is available at the given time, false otherwise
     */
    @Override
    public boolean isWorkspaceAvailable(int workspaceId, LocalDateTime bookingTime) {
        String sql = "SELECT COUNT(*) FROM " + BOOKING_TABLE_NAME + " WHERE \"workspace_id\" = ? AND \"booking_time\" = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, workspaceId);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(bookingTime));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Находим рабочее место и его бронирования по идентификатору.
     *
     * @param id the workspace ID
     * @return the workspace with its bookings, or empty Optional if not found
     */
    @Override
    public Optional<Workspace> findWorkspaceWithBookingsById(int id) {
        Optional<Workspace> workspaceOptional = findWorkspaceById(id);
        if (workspaceOptional.isPresent()) {
            Workspace workspace = workspaceOptional.get();
            List<Booking> bookings = findBookingsByWorkspaceId(workspace.getId());
            workspace.setBookings(bookings);
            return Optional.of(workspace);
        }
        return Optional.empty();
    }
}
