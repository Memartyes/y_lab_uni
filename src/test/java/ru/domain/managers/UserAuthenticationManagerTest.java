//package ru.domain.managers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.domain.dao.UserDAO;
//import ru.domain.dao.impl.UserDAOImpl;
//import ru.domain.entities.User;
//import ru.domain.managers.UserAuthenticationManager;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//class UserAuthenticationManagerTest {
//
//    private UserAuthenticationManager userAuthenticationManager;
//    private UserDAO userDAO = new UserDAOImpl();
//
//    @BeforeEach
//    public void setUp() {
//        userAuthenticationManager = new UserAuthenticationManager(userDAO);
//    }
//
//    @Test
//    public void testLoginUser() {
//        String userName = "testUser";
//        String password = "testPassword";
//        User user = new User(userName, password);
//        userAuthenticationManager.getUsers().put(userName, user);
//
//        Optional<User> loggedInUser = userAuthenticationManager.loginUser(userName, password);
//
//        assertThat(loggedInUser).isPresent();
//        assertThat(loggedInUser.get().getName()).isEqualTo(userName);
//        assertThat(loggedInUser.get().getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    public void testLoginUserInvalidPassword() {
//        String userName = "testUser";
//        String password = "testPassword";
//        String invalidPassword = "invalidPassword";
//        User user = new User(userName, password);
//        userAuthenticationManager.getUsers().put(userName, user);
//
//        Optional<User> loggedInUser = userAuthenticationManager.loginUser(userName, invalidPassword);
//
//        assertThat(loggedInUser).isNotPresent();
//    }
//
//    @Test
//    public void testLoginUserNotFound() {
//        String userName = "nonexistentUser";
//        String password = "testPassword";
//
//        Optional<User> loggedInUser = userAuthenticationManager.loginUser(userName, password);
//
//        assertThat(loggedInUser).isNotPresent();
//    }
//}