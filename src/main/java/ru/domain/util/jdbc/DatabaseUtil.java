package ru.domain.util.jdbc;

import java.sql.*;
import java.util.Properties;

/**
 * Ютилити класс для управления database connection
 */
public class DatabaseUtil {
    private static String URL = ConfigUtil.getProperty("database.url");
    private static String USER = ConfigUtil.getProperty("database.username");
    private static String PASSWORD = ConfigUtil.getProperty("database.password");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
    }

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

    /**
     * Возвращает connection для database, принимает новый properties
     * @param properties
     * @return
     */
    public static Connection getConnectionFromProperties(Properties properties) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    properties.getProperty("database.url"),
                    properties.getProperty("database.username"),
                    properties.getProperty("database.password")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection error: " + e.getMessage());
        }
        return connection;
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
