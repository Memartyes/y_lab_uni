//package ru.domain.dao;
//
//import org.junit.jupiter.api.*;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.domain.entities.User;
//import ru.domain.util.jdbc.DatabaseUtil;
//import ru.domain.dao.impl.UserDAOImpl;
//import liquibase.Contexts;
//import liquibase.Liquibase;
//import liquibase.database.Database;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.resource.ClassLoaderResourceAccessor;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Testcontainers
//class UserDAOTest {
//
//    @Container
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass");
//
//    private UserDAO userDAO;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        DatabaseUtil.setUrl(postgresContainer.getJdbcUrl());
//        DatabaseUtil.setUser(postgresContainer.getUsername());
//        DatabaseUtil.setPassword(postgresContainer.getPassword());
//
//        try (Connection connection = DatabaseUtil.getConnection()) {
//            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
//            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
//            liquibase.update(new Contexts());
//        }
//
//        userDAO = new UserDAOImpl();
//    }
//
//    @Test
//    @DisplayName("Insert user to the database")
//    public void testAddUser() throws SQLException {
//        User user = new User("TestUser", "test@example.com", "password");
//        userDAO.addUser(user);
//
//        assertThat(user.getId()).isNotNull();
//        Optional<User> foundUser = userDAO.findUserById(user.getId());
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getName()).isEqualTo("TestUser");
//        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
//    }
//
//    @Test
//    @DisplayName("Find user by ID")
//    public void testFindUserById() throws SQLException {
//        User user = new User("TestUser", "test@example.com", "password");
//        userDAO.addUser(user);
//
//        Optional<User> foundUser = userDAO.findUserById(user.getId());
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getName()).isEqualTo("TestUser");
//        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
//    }
//
//    @Test
//    @DisplayName("Find user by username")
//    void testFindUserByName() throws SQLException {
//        User user = new User("TestUser", "test@example.com", "password");
//        userDAO.addUser(user);
//
//        Optional<User> foundUser = userDAO.findUserByName("TestUser");
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getName()).isEqualTo("TestUser");
//        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
//    }
//
//    @Test
//    @DisplayName("Find user by email")
//    void testFindUserByEmail() {
//        User user = new User("TestUser", "test@example.com", "password");
//        userDAO.addUser(user);
//
//        Optional<User> foundUser = userDAO.findUserByEmail("test@example.com");
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getName()).isEqualTo("TestUser");
//        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
//    }
//
//    @Test
//    @DisplayName("Find all users")
//    public void testFindAllUsers() throws SQLException {
//        User user1 = new User("User1", "user1@example.com", "password1");
//        User user2 = new User("User2", "user2@example.com", "password2");
//
//        userDAO.addUser(user1);
//        userDAO.addUser(user2);
//
//        List<User> users = userDAO.findAllUsers();
//
//        assertThat(users).hasSize(2);
//        assertThat(users).extracting(User::getName).containsExactlyInAnyOrder("User1", "User2");
//        assertThat(users).extracting(User::getEmail).containsExactlyInAnyOrder("user1@example.com", "user2@example.com");
//    }
//
//    @Test
//    @DisplayName("Update user")
//    public void testUpdateUser() throws SQLException {
//        User user = new User("TestUser", "test@example.com", "password");
//        userDAO.addUser(user);
//
//        user.setName("UpdatedName");
//        user.setEmail("updated@example.com");
//        userDAO.updateUser(user);
//
//        Optional<User> updatedUser = userDAO.findUserById(user.getId());
//        assertThat(updatedUser).isPresent();
//        assertThat(updatedUser.get().getName()).isEqualTo("UpdatedName");
//        assertThat(updatedUser.get().getEmail()).isEqualTo("updated@example.com");
//    }
//
//    @Test
//    @DisplayName("Delete user")
//    public void testDeleteUser() throws SQLException {
//        User user = new User("TestUser", "test@example.com", "password");
//        userDAO.addUser(user);
//
//        userDAO.deleteUser(user.getId());
//
//        Optional<User> deletedUser = userDAO.findUserById(user.getId());
//        assertThat(deletedUser).isNotPresent();
//    }
//}