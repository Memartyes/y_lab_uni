package ru.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("user1", "pw1");
    }

    @Test
    public void testGetUserId() {
        String userId = user.getId();
        assertThat(userId).isEqualTo("user1");
    }

    @Test
    public void testGetPassword() {
        String password = user.getPassword();
        assertThat(password).isEqualTo("pw1");
    }
}