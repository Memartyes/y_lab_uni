package ru.domain.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.domain.dto.UserDTO;
import ru.domain.entities.User;
import ru.domain.managers.UserRegistrationManager;
import ru.domain.mapper.UserMapper;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRegistrationManager userRegistrationManager;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User("Spinoza", "bspinoza@gmail.com", "ethic");
        user.setId(1);
        userDTO = UserMapper.INSTANCE.toDTO(user);
    }

    @Test
    void testGetAllUsers() throws Exception {
        given(userRegistrationManager.getAllUsers()).willReturn(Arrays.asList(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(user.getName()));
    }
}