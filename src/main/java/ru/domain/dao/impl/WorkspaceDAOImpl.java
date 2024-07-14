package ru.domain.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.domain.dao.WorkspaceDAO;
import ru.domain.entities.Booking;
import ru.domain.entities.Workspace;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализвация DAO для рабочих мест.
 */
@Repository
public class WorkspaceDAOImpl implements WorkspaceDAO {
    private static final String WORKSPACE_TABLE_NAME = "coworking.\"workspaces-liquibase\"";
    private static final String BOOKING_TABLE_NAME = "coworking.\"bookings-liquibase\"";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор с внедрением зависимости DataSource.
     *
     * @param dataSource источник данных
     */
    @Autowired
    public WorkspaceDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Преобразовуем строки результата SQL-запроса в объект Workspace.
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
     * Добавляем рабочее место в базу данных.
     *
     * @param workspace the workspace to add
     */
    @Override
    public void addWorkspace(Workspace workspace) {
        String sql = "INSERT INTO " + WORKSPACE_TABLE_NAME + " (name) VALUES (?) RETURNING id";
        Integer newId = jdbcTemplate.queryForObject(sql, new Object[]{workspace.getName()}, Integer.class);
        if (newId != null) {
            workspace.setId(newId);
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
        List<Workspace> workspaces = jdbcTemplate.query(sql, new Object[]{id}, new WorkspaceRowMapper());
        return workspaces.isEmpty() ? Optional.empty() : Optional.of(workspaces.get(0));
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
        List<Workspace> workspaces = jdbcTemplate.query(sql, new Object[]{name}, new WorkspaceRowMapper());
        return workspaces.isEmpty() ? Optional.empty() : Optional.of(workspaces.get(0));
    }

    /**
     * Находим все рабочие места.
     *
     * @return the list of workspaces
     */
    @Override
    public List<Workspace> findAllWorkspaces() {
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME;
        return jdbcTemplate.query(sql, new WorkspaceRowMapper());
    }

    /**
     * Обновляем рабочее место
     *
     * @param workspace the workspace object to update
     */
    @Override
    public void updateWorkspace(Workspace workspace) {
        String sql = "UPDATE " + WORKSPACE_TABLE_NAME + " SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, workspace.getName(), workspace.getId());
    }

    /**
     * Удаляем рабочее место по ID
     *
     * @param id the workspace ID
     */
    @Override
    public void deleteWorkspace(int id) {
        String sql = "DELETE FROM " + WORKSPACE_TABLE_NAME + " WHERE id = ?";
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
        String sql = "SELECT * FROM " + BOOKING_TABLE_NAME + " WHERE \"workspace_id\" = ?";
        return jdbcTemplate.query(sql, new Object[]{workspaceId}, new BookingRowMapper());
    }

    /**
     * Находим рабочие места, которые доступны для бронирования.
     *
     * @return the list of available workspaces
     */
    @Override
    public List<Workspace> findAvailableWorkspaces() {
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME +
                " LEFT JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\" WHERE " + BOOKING_TABLE_NAME + ".id IS NULL";
        return jdbcTemplate.query(sql, new WorkspaceRowMapper());
    }

    /**
     * Находим все рабочие места, связанные с определенным конференц-залом.
     *
     * @param conferenceRoomId the conference room ID
     * @return the list of workspaces by conference room ID
     */
    @Override
    public List<Workspace> findWorkspacesByConferenceRoomId(int conferenceRoomId) {
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME + " WHERE \"conference_room_id\" = ?";
        return jdbcTemplate.query(sql, new Object[]{conferenceRoomId}, new WorkspaceRowMapper());
    }

    /**
     * Находим забронированные рабочие места.
     *
     * @return
     */
    @Override
    public List<Workspace> findBookedWorkspaces() {
        String sql = "SELECT * FROM " + WORKSPACE_TABLE_NAME +
                " JOIN " + BOOKING_TABLE_NAME + " ON " + WORKSPACE_TABLE_NAME + ".id = " + BOOKING_TABLE_NAME + ".\"workspace_id\"";
        return jdbcTemplate.query(sql, new WorkspaceRowMapper());
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
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{workspaceId, bookingTime}, Integer.class);
        return count != null && count == 0;
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
