package ru.domain.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.domain.dao.ConferenceRoomDAO;
import ru.domain.entities.Booking;
import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.Workspace;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ConferenceRoomDAOImpl implements ConferenceRoomDAO {
    private static final String CONFERENCE_TABLE_NAME = "coworking.\"conference_rooms-liquibase\"";
    private static final String WORKSPACE_TABLE_NAME = "coworking.\"workspaces-liquibase\"";
    private static final String BOOKING_TABLE_NAME = "coworking.\"bookings-liquibase\"";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор с внедрением зависимости DataSource.
     *
     * @param dataSource источник данных
     */
    @Autowired
    public ConferenceRoomDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Преобразовуем строки результата SQL-запроса в объект ConferenceRoom.
     */
    private static final class ConferenceRoomRowMapper implements RowMapper<ConferenceRoom> {
        @Override
        public ConferenceRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConferenceRoom conferenceRoom = new ConferenceRoom();
            conferenceRoom.setId(rs.getInt("id"));
            conferenceRoom.setName(rs.getString("name"));
            conferenceRoom.setCapacity(rs.getInt("capacity"));
            return conferenceRoom;
        }
    }

    /**
     * МПреобразовуем строки результата SQL-запроса в объект Workspace.
     */
    private static final class WorkspaceRowMapper implements RowMapper<Workspace> {
        @Override
        public Workspace mapRow(ResultSet rs, int rowNum) throws SQLException {
            Workspace workspace = new Workspace();
            workspace.setId(rs.getInt("id"));
            workspace.setName(rs.getString("name"));
            return workspace;
        }
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
     * Добавляем новый конференц-зал в базу данных.
     *
     * @param conferenceRoom the conference room
     */
    @Override
    public void addConferenceRoom(ConferenceRoom conferenceRoom) {
        String sql = "INSERT INTO " + CONFERENCE_TABLE_NAME + " (name, capacity) VALUES (?, ?) RETURNING id";
        Integer newId = jdbcTemplate.queryForObject(sql, new Object[]{conferenceRoom.getName(), conferenceRoom.getCapacity()}, Integer.class);
        if (newId != null) {
            conferenceRoom.setId(newId);
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
        List<ConferenceRoom> conferenceRooms = jdbcTemplate.query(sql, new Object[]{id}, new ConferenceRoomRowMapper());
        return conferenceRooms.isEmpty() ? Optional.empty() : Optional.of(conferenceRooms.get(0));
    }

    /**
     * Возвращаем список всех конференц-залов.
     *
     * @return the list of conference rooms
     */
    @Override
    public List<ConferenceRoom> findAllConferenceRooms() {
        String sql = "SELECT * FROM " + CONFERENCE_TABLE_NAME;
        return jdbcTemplate.query(sql, new ConferenceRoomRowMapper());
    }

    /**
     * Обновляем информацию о конференц-зале.
     *
     * @param conferenceRoom the conference room to update
     */
    @Override
    public void updateConferenceRoom(ConferenceRoom conferenceRoom) {
        String sql = "UPDATE " + CONFERENCE_TABLE_NAME + " SET name = ?, capacity = ? WHERE id = ?";
        jdbcTemplate.update(sql, conferenceRoom.getName(), conferenceRoom.getCapacity(), conferenceRoom.getId());
    }

    /**
     * Удаляем конференц-зал по его ID.
     *
     * @param id the conference room id
     */
    @Override
    public void deleteConferenceRoom(int id) {
        String sql = "DELETE FROM " + CONFERENCE_TABLE_NAME + " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Добавляем рабочее место в конференц-зал.
     *
     * @param conferenceRoomId the conference room ID
     * @param workspace        the workspace
     */
    @Override
    public void addWorkspaceToConferenceRoom(int conferenceRoomId, Workspace workspace) {
        String sql = "INSERT INTO " + WORKSPACE_TABLE_NAME + " (name, \"conference_room_id\") VALUES (?, ?) RETURNING id";
        Integer newId = jdbcTemplate.queryForObject(sql, new Object[]{workspace.getName(), conferenceRoomId}, Integer.class);
        if (newId != null) {
            workspace.setId(newId);
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
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME + " WHERE \"conference_room_id\" = ?";
        return jdbcTemplate.query(sql, new Object[]{conferenceRoomId}, new WorkspaceRowMapper());
    }

    /**
     * Находим бронирование по ID рабочего места.
     *
     * @param workspaceId the workspace ID
     * @return the list of bookings in workspace
     */
    @Override
    public List<Booking> findBookingsByWorkspaceId(int workspaceId) {
        String sql = "SELECT * FROM " + BOOKING_TABLE_NAME + " WHERE \"workspace_id\" = ?";
        return jdbcTemplate.query(sql, new Object[]{workspaceId}, new BookingRowMapper());
    }

    /**
     * Находим конференц-залы с бронированиями на указанную дату.
     *
     * @param date the date
     * @return the list of conference rooms
     */
    @Override
    public List<ConferenceRoom> findRoomsByDate(LocalDate date) {
        String sql = "SELECT " + CONFERENCE_TABLE_NAME + ".* FROM " + CONFERENCE_TABLE_NAME +
                " JOIN " + WORKSPACE_TABLE_NAME + " ON " + CONFERENCE_TABLE_NAME + ".id = " + WORKSPACE_TABLE_NAME + ".\"conference_room_id\"" +
                " JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\"" +
                " WHERE " + BOOKING_TABLE_NAME + ".\"booking_time\"::date = ?";
        return jdbcTemplate.query(sql, new Object[]{date}, new ConferenceRoomRowMapper());
    }

    /**
     * Находим конференц-залы с бронированиями пользователем.
     *
     * @param userName the user's name
     * @return the list of conference rooms
     */
    @Override
    public List<ConferenceRoom> findRoomsByUser(String userName) {
        String sql = "SELECT " + CONFERENCE_TABLE_NAME + ".* FROM " + CONFERENCE_TABLE_NAME +
                " JOIN " + WORKSPACE_TABLE_NAME + " ON " + CONFERENCE_TABLE_NAME + ".id = " + WORKSPACE_TABLE_NAME + ".\"conference_room_id\"" +
                " JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\"" +
                " WHERE " + BOOKING_TABLE_NAME + ".\"booked_by\" = ?";
        return jdbcTemplate.query(sql, new Object[]{userName}, new ConferenceRoomRowMapper());
    }

    /**
     * Находим конференц-залы с доступными рабочими местами.
     *
     * @return the list of conference rooms
     */
    @Override
    public List<ConferenceRoom> findRoomsWithAvailableWorkspaces() {
        String sql = "SELECT " + CONFERENCE_TABLE_NAME + ".* FROM " + CONFERENCE_TABLE_NAME +
                " JOIN " + WORKSPACE_TABLE_NAME + " ON " + CONFERENCE_TABLE_NAME + ".id = " + WORKSPACE_TABLE_NAME + ".\"conference_room_id\"" +
                " LEFT JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\"" +
                " WHERE " + BOOKING_TABLE_NAME + ".id IS NULL";
        return jdbcTemplate.query(sql, new ConferenceRoomRowMapper());
    }
}
