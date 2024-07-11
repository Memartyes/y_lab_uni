package ru.domain.dao.impl;

import ru.domain.dao.ConferenceRoomDAO;
import ru.domain.entities.Booking;
import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.Workspace;
import ru.domain.util.jdbc.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConferenceRoomDAOImpl implements ConferenceRoomDAO {
    private static final String CONFERENCE_TABLE_NAME = "coworking.\"conference_rooms-liquibase\"";
    private static final String WORKSPACE_TABLE_NAME = "coworking.\"workspaces-liquibase\"";
    private static final String BOOKING_TABLE_NAME = "coworking.\"bookings-liquibase\"";

    /**
     * Добавляем новый конференц-зал в базу данных.
     *
     * @param conferenceRoom the conference room
     */
    @Override
    public void addConferenceRoom(ConferenceRoom conferenceRoom) {
        String sql = "INSERT INTO " + CONFERENCE_TABLE_NAME + " (name, capacity) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, conferenceRoom.getName());
            preparedStatement.setInt(2, conferenceRoom.getCapacity());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating conference room failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    conferenceRoom.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating conference room failed, no ID obtained.");
                }
            }

            System.out.println("Created conference room: " + conferenceRoom.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Находим конференц-зал по его ID
     *
     * @param id the conference room ID
     * @return Optional with conference room
     */
    @Override
    public Optional<ConferenceRoom> findConferenceRoomById(int id) {
        String sql = "SELECT * FROM " + CONFERENCE_TABLE_NAME + " WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ConferenceRoom conferenceRoom = new ConferenceRoom();
                    conferenceRoom.setId(resultSet.getInt("id"));
                    conferenceRoom.setName(resultSet.getString("name"));
                    conferenceRoom.setCapacity(resultSet.getInt("capacity"));

                    List<Workspace> workspaces = findWorkspacesByConferenceRoomId(conferenceRoom.getId());
                    conferenceRoom.setWorkspaces(workspaces);

                    return Optional.of(conferenceRoom);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Возвращаем список всех конференц-залов.
     *
     * @return the list of conference rooms
     */
    @Override
    public List<ConferenceRoom> findAllConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = new ArrayList<>();
        String sql = "SELECT * FROM " + CONFERENCE_TABLE_NAME;
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ConferenceRoom conferenceRoom = new ConferenceRoom();
                conferenceRoom.setId(resultSet.getInt("id"));
                conferenceRoom.setName(resultSet.getString("name"));
                conferenceRoom.setCapacity(resultSet.getInt("capacity"));

                List<Workspace> workspaces = findWorkspacesByConferenceRoomId(conferenceRoom.getId());
                conferenceRoom.setWorkspaces(workspaces);

                conferenceRooms.add(conferenceRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conferenceRooms;
    }

    /**
     * Обновляем информацию о конференц-зале.
     *
     * @param conferenceRoom the conference room to update
     */
    @Override
    public void updateConferenceRoom(ConferenceRoom conferenceRoom) {
        String sql = "UPDATE " + CONFERENCE_TABLE_NAME + " SET name = ?, capacity = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, conferenceRoom.getName());
            preparedStatement.setInt(2, conferenceRoom.getCapacity());
            preparedStatement.setInt(3, conferenceRoom.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляем конференц-зал по его ID.
     *
     * @param id the conference room id
     */
    @Override
    public void deleteConferenceRoom(int id) {
        String sql = "DELETE FROM " + CONFERENCE_TABLE_NAME + " WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляем рабочее место в конференц-зал.
     *
     * @param conferenceRoomId the conference room ID
     * @param workspace        the workspace
     */
    @Override
    public void addWorkspaceToConferenceRoom(int conferenceRoomId, Workspace workspace) {
        String sql = "INSERT INTO " + WORKSPACE_TABLE_NAME + " (name, \"conference_room_id\") VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setInt(2, conferenceRoomId);
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

            System.out.println("Added workspace: " + workspace.getName() + " to conference room ID: " + conferenceRoomId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Находим все рабочие места в конференц-зале.
     *
     * @param conferenceRoomId the conference room ID
     * @return the list of workspaces in conference room
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

    /**
     * Находим бронирование по ID рабочего места.
     *
     * @param workspaceId the workspace ID
     * @return the list of bookings in workspace
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
        }
        return bookings;
    }

    /**
     * Находим конференц-залы с бронированиями на указанную дату.
     *
     * @param date the date
     * @return the list of conference rooms
     */
    @Override
    public List<ConferenceRoom> findRoomsByDate(LocalDate date) {
        List<ConferenceRoom> conferenceRooms = new ArrayList<>();
        String sql = "SELECT " + CONFERENCE_TABLE_NAME + ".* FROM " + CONFERENCE_TABLE_NAME +
                " JOIN " + WORKSPACE_TABLE_NAME + " ON " + CONFERENCE_TABLE_NAME + ".id = " + WORKSPACE_TABLE_NAME + ".\"conference_room_id\"" +
                " JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\"" +
                " WHERE " + BOOKING_TABLE_NAME + ".\"booking_time\"::date = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ConferenceRoom conferenceRoom = new ConferenceRoom();
                    conferenceRoom.setId(resultSet.getInt("id"));
                    conferenceRoom.setName(resultSet.getString("name"));
                    conferenceRoom.setCapacity(resultSet.getInt("capacity"));

                    List<Workspace> workspaces = findWorkspacesByConferenceRoomId(conferenceRoom.getId());
                    conferenceRoom.setWorkspaces(workspaces);

                    conferenceRooms.add(conferenceRoom);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conferenceRooms;
    }

    /**
     * Находим конференц-залы с бронированиями пользователем.
     *
     * @param userName the user's name
     * @return the list of conference rooms
     */
    @Override
    public List<ConferenceRoom> findRoomsByUser(String userName) {
        List<ConferenceRoom> conferenceRooms = new ArrayList<>();
        String sql = "SELECT " + CONFERENCE_TABLE_NAME + ".* FROM " + CONFERENCE_TABLE_NAME +
                " JOIN " + WORKSPACE_TABLE_NAME + " ON " + CONFERENCE_TABLE_NAME + ".id = " + WORKSPACE_TABLE_NAME + ".\"conference_room_id\"" +
                " JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\"" +
                " WHERE " + BOOKING_TABLE_NAME + ".\"booked_by\" = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ConferenceRoom conferenceRoom = new ConferenceRoom();
                    conferenceRoom.setId(resultSet.getInt("id"));
                    conferenceRoom.setName(resultSet.getString("name"));
                    conferenceRoom.setCapacity(resultSet.getInt("capacity"));

                    List<Workspace> workspaces = findWorkspacesByConferenceRoomId(conferenceRoom.getId());
                    conferenceRoom.setWorkspaces(workspaces);

                    conferenceRooms.add(conferenceRoom);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conferenceRooms;
    }

    /**
     * Находим конференц-залы с доступными рабочими местами.
     *
     * @return the list of conference rooms
     */
    @Override
    public List<ConferenceRoom> findRoomsWithAvailableWorkspaces() {
        List<ConferenceRoom> conferenceRooms = new ArrayList<>();
        String sql = "SELECT " + CONFERENCE_TABLE_NAME + ".* FROM " + CONFERENCE_TABLE_NAME +
                " JOIN " + WORKSPACE_TABLE_NAME + " ON " + CONFERENCE_TABLE_NAME + ".id = " + WORKSPACE_TABLE_NAME + ".\"conference_room_id\"" +
                " LEFT JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\"" +
                " WHERE " + BOOKING_TABLE_NAME + ".id IS NULL";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ConferenceRoom conferenceRoom = new ConferenceRoom();
                conferenceRoom.setId(resultSet.getInt("id"));
                conferenceRoom.setName(resultSet.getString("name"));
                conferenceRoom.setCapacity(resultSet.getInt("capacity"));

                List<Workspace> workspaces = findWorkspacesByConferenceRoomId(conferenceRoom.getId());
                conferenceRoom.setWorkspaces(workspaces);

                conferenceRooms.add(conferenceRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conferenceRooms;
    }
}
