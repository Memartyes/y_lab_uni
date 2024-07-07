package ru.domain.dao.impl;

import ru.domain.dao.UserDAO;
import ru.domain.entities.User;
import ru.domain.util.jdbc.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final String TABLE_NAME = "coworking.\"users-liquibase\"";

    /**
     * Создаем и добавляем нового пользователя в базу данных.
     *
     * @param user the user object to create and add
     */
    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, email, password) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to create user, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to create user, no ID obtained.");
                }
            }

            System.out.println("Created user: " + user.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
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
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"));
                    user.setId(resultSet.getInt("id"));

                    System.out.println("Found user: " + user.getName());
                    return Optional.of(user);
                }
            }

            System.out.println("User with id " + id + " not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
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
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"));
                    user.setId(resultSet.getInt("id"));

                    System.out.println("Found user: " + user.getName());
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
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
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"));
                    user.setId(resultSet.getInt("id"));

                    System.out.println("Found user: " + user.getName());
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Возвращаем список всех пользователей.
     *
     * @return List of users
     */
    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
                users.add(user);
            }

            System.out.println("Found " + users.size() + " users.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return users;
    }

    /**
     * Обновляем информацию о пользователе в базе данных.
     *
     * @param user the updated user object to perform update in database
     */
    @Override
    public void updateUser(User user) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, email = ?, password = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();

            System.out.println("Updated user: " + user.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Удаляем пользователя по его идентификатору.
     *
     * @param id the user's id
     */
    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            System.out.println("Deleted user: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }
}
