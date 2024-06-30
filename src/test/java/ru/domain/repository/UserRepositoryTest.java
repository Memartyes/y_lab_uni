package ru.domain.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.domain.entities.User;
import ru.domain.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserRepositoryTest {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("password")
            .withExposedPorts(5432, 5432)
            .withInitScript("db/changelog/changeset/init_user.sql");

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();
        DatabaseUtil.setUrl(jdbcUrl);
        DatabaseUtil.setUser(username);
        DatabaseUtil.setPassword(password);
        userRepository = new UserRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE coworking.\"users-liquibase\";");
        }
    }

    @Test
    void testSaveAndFindUser() throws SQLException {
        User user = new User("addUser", "add@test.com", "pw");
        userRepository.saveUser(user);
        User foundUser = userRepository.findByUsername("adduser").orElseThrow();
        assertNotNull(foundUser);
        assertEquals("addUser", foundUser.getName());
        assertEquals("add@test.com", foundUser.getEmail());
        assertEquals("pw", foundUser.getPassword());
    }

    @Test
    void testFindAllUsers() throws SQLException {
        User user1 = new User("findAll1", "findall1@test.com", "pw1");
        User user2 = new User("findAll2", "findall2@test.com", "pw2");
        userRepository.saveUser(user1);
        userRepository.saveUser(user2);
        List<User> userList = userRepository.findAllUsers();
        assertTrue(userList.size() >= 2);

    }

    @Test
    void testUpdateUser() throws SQLException {
        User user = new User("updateUser", "update@test.com", "pw");
        userRepository.saveUser(user);
        user.setEmail("updated@test.com");
        userRepository.updateUser(user);
        User updatedUser = userRepository.findByUsername("updateUser").orElseThrow();
        assertEquals("updated@test.com", updatedUser.getEmail());
    }

    @Test
    void testDeleteUser() throws SQLException {
        User user = new User("deleteUser", "delete@test.com", "pw");
        userRepository.saveUser(user);
        userRepository.deleteUser(user.getId());
        assertTrue(userRepository.findByUsername("deleteUser").isEmpty());
    }
}