package ru.domain.util;

import java.sql.*;

/**
 * Ютилити класс для управления database connection
 */
public class DatabaseUtil {
    private static String URL = ConfigUtil.getProperty("database.url");
    private static String USER = ConfigUtil.getProperty("database.username");
    private static String PASSWORD = ConfigUtil.getProperty("database.password");

    // Сеттеры для работы с тестконтейнерами
    public static void setUrl(String newUrl) {
        URL = newUrl;
    }

    public static void setUser(String newUser) {
        USER = newUser;
    }

    public static void setPassword(String newPassword) {
        PASSWORD = newPassword;
    }

    /**
     * Возврашает connection для database
     * @return a connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            System.out.println("Connection successful: " + connection.isValid(10));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }
}
