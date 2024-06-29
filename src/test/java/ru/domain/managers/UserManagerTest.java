package ru.domain.managers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.domain.entities.User;

import java.util.HashMap;
import java.util.Map;

class UserManagerTest {

    @Mock
    private Map<String, User> usersMock;

    @InjectMocks
    private UserManager userManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        String userId = "user1";
        String password = "password1";

        userManager.registerUser(userId, password);

        assertTrue(usersMock.containsKey(userId));
        assertEquals(userId, usersMock.get(userId).getId());
        assertEquals(password, usersMock.get(userId).getPassword());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        String userId = "user2";
        String password = "password2";

        when(usersMock.containsKey(userId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userManager.registerUser(userId, password);
        });
        assertEquals("User with id " + userId + " already exists", exception.getMessage());

        verify(usersMock, never()).put(eq(userId), any());
    }

    @Test
    void testLoginUser_Success() {
        String userId = "user3";
        String password = "password3";
        User user = new User(userId, password);
        when(usersMock.get(userId)).thenReturn(user);

        boolean loggedIn = userManager.loginUser(userId, password);

        assertTrue(loggedIn);
    }

    @Test
    void testLoginUser_WrongPassword() {
        String userId = "user4";
        String password = "password4";
        User user = new User(userId, password);
        when(usersMock.get(userId)).thenReturn(user);

        boolean loggedIn = userManager.loginUser(userId, "wrongPassword");

        assertFalse(loggedIn);
    }

    @Test
    void testLoginUser_UserNotFound() {
        String userId = "nonExistingUser";
        when(usersMock.get(userId)).thenReturn(null);

        boolean loggedIn = userManager.loginUser(userId, "password");

        assertFalse(loggedIn);
    }

    @Test
    void testGetUsers_Empty() {
        when(usersMock.isEmpty()).thenReturn(true);

        Map<String, User> users = userManager.getUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void testGetUsers_NonEmpty() {
        Map<String, User> mockData = new HashMap<>();
        mockData.put("user1", new User("user1", "password1"));
        mockData.put("user2", new User("user2", "password2"));
        when(usersMock.isEmpty()).thenReturn(false);
        when(usersMock.entrySet()).thenReturn(mockData.entrySet());

        Map<String, User> users = userManager.getUsers();

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertThat(users).containsKeys("user1", "user2");
        assertThat(users.get("user1")).isEqualToComparingFieldByField(mockData.get("user1"));
        assertThat(users.get("user2")).isEqualToComparingFieldByField(mockData.get("user2"));
    }
}