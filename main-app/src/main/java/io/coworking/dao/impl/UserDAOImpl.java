package io.coworking.dao.impl;

import io.coworking.dao.UserDAO;
import io.coworking.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * Реализвация DAO для пользователей
 */
@Repository
public class UserDAOImpl implements UserDAO {

    private static final String TABLE_NAME = "coworking.\"users-liquibase\"";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор с внедрением зависимости DataSource.
     *
     * @param dataSource
     */
    @Autowired
    public UserDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Преобразовуем строки результата SQL-запроса в объект User.
     */
    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            return user;
        }
    }

    /**
     * Создаем и добавляем нового пользователя в базу данных.
     *
     * @param user the user object to create and add
     */
    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, email, password) VALUES (?, ?, ?) RETURNING id";
        Integer newId = jdbcTemplate.queryForObject(sql, new Object[]{user.getName(), user.getEmail(), user.getPassword()}, Integer.class);
        if (newId != null) {
            user.setId(newId);
        }
    }

    /**
     * Находим пользователя по его ID
     *
     * @param id the user's ID
     * @return the Optional with User if the user found, empty Optional otherwise
     */
    @Override
    public Optional<User> findUserById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{id}, new UserRowMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    /**
     * Находим пользователя по его имени.
     *
     * @param name the username
     * @return the Optional with User if the user found, empty Optional otherwise
     */
    @Override
    public Optional<User> findUserByName(String name) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE name = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{name}, new UserRowMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    /**
     * Находим пользователя по его email.
     *
     * @param email the user's email
     * @return the Optional with User if the user found, empty Optional otherwise
     */
    @Override
    public Optional<User> findUserByEmail(String email) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{email}, new UserRowMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    /**
     * Возвращаем список всех пользователей.
     *
     * @return List of users
     */
    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    /**
     * Обновляем информацию о пользователе в базе данных.
     *
     * @param user the updated user object to perform update in database
     */
    @Override
    public void updateUser(User user) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getId());
    }

    /**
     * Удаляем пользователя по его идентификатору.
     *
     * @param id the user's id
     */
    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
