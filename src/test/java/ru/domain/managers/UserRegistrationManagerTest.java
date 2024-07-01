package ru.domain.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.domain.entities.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class UserRegistrationManagerTest {

    private UserRegistrationManager userRegistrationManager;

    @BeforeEach
    public void setUp() {
        userRegistrationManager = new UserRegistrationManager();
    }

    @Test
    public void testRegisterUser() {
        String userName = "testUser";
        String password = "testPassword";

        userRegistrationManager.registerUser(userName, password);

        Optional<User> registeredUser = userRegistrationManager.getUser(userName);
        assertThat(registeredUser).isPresent();
        assertThat(registeredUser.get().getName()).isEqualTo(userName);
        assertThat(registeredUser.get().getPassword()).isEqualTo(password);
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        String userName = "testUser";
        String password = "testPassword";
        userRegistrationManager.registerUser(userName, password);

        assertThatThrownBy(() -> userRegistrationManager.registerUser(userName, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User with username " + userName + " already exists");
    }

    @Test
    public void testGetUser() {
        String userName = "testUser";
        String password = "testPassword";
        userRegistrationManager.registerUser(userName, password);

        Optional<User> user = userRegistrationManager.getUser(userName);
        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo(userName);
        assertThat(user.get().getPassword()).isEqualTo(password);
    }

    @Test
    public void testGetUserNotFound() {
        String userName = "nonexistentUser";

        Optional<User> user = userRegistrationManager.getUser(userName);
        assertThat(user).isNotPresent();
    }
}