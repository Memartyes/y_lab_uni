package ru.domain.repository;

import ru.domain.entities.User;
import ru.domain.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Хранилище для управления сущностей
 */
public class UserRepository {

    /**
     * Сохраняем пользователя в database.
     *
     * @param user the user to save
     */
    public void saveUser(User user) {
        String sql = "INSERT INTO coworking.\"users-liquibase\" (name, email, password) VALUES (?, ?, ?) RETURNING id";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("User saved successfully: " + user.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Находим все сущности User в database.
     *
     * @return the list of users
     */
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email, password FROM coworking.\"users-liquibase\"";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }

            System.out.println("Users retrieved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
        return users;
    }

    /**
     * Находим пользователя по userName.
     *
     * @param userName the userName
     * @return the found user
     * @throws SQLException SQLException
     */
    public Optional<User> findByUsername(String userName) throws SQLException {
        String sql = "SELECT id, name, email, password FROM coworking.\"users-liquibase\" WHERE name = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    return Optional.of(user);
                }
            }

            System.out.println("User " + userName + " retrieved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error finding userName by userName: " + e.getMessage());
            throw e;
        }
        return Optional.empty();
    }

    /**
     * Обновляем данные пользователя в database.
     *
     * @param user the user
     */
    public void updateUser(User user) {
        String sql = "UPDATE coworking.\"users-liquibase\" SET name = ?, email = ?, password = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();

            System.out.println("User " + user.getName() + " updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in updating user: " + e.getMessage());
        }
    }

    /**
     * Удаляем пользователя из database по его ID.
     *
     * @param userId the user's ID
     */
    public void deleteUser(int userId) {
        String sql = "DELETE FROM coworking.\"users-liquibase\" WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

            System.out.println("User with ID: " + userId + " has been deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in deleting user: " + e.getMessage());
        }
    }
}
