package io.coworking.controllers;

import io.coworking.dto.UserDTO;
import io.coworking.entities.User;
import io.coworking.managers.UserRegistrationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRegistrationManager userRegistrationManager;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user = new User("Benedict Spinoza", "bspinoza@gmail.com", "ethic");
        given(userRegistrationManager.getAllUsers()).willReturn(Collections.singletonList(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Benedict Spinoza"))
                .andExpect(jsonPath("$[0].email").value("bspinoza@gmail.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User("Benedict Spinoza", "bspinoza@gmail.com", "ethic");
        given(userRegistrationManager.getUserById(anyInt())).willReturn(Optional.of(user));

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Benedict Spinoza"))
                .andExpect(jsonPath("$.email").value("bspinoza@gmail.com"));
    }

    @Test
    void testGetUserByIdShouldReturnNotFound() throws Exception {
        given(userRegistrationManager.getUserById(anyInt())).willReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Benedict Spinoza");
        userDTO.setEmail("bspinoza@gmail.com");
        userDTO.setPassword("ethic");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Benedict Spinoza\",\"email\":\"bspinoza@gmail.com\",\"password\":\"ethic\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateUserShouldReturnConflict() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Benedict Spinoza");
        userDTO.setEmail("bspinoza@gmail.com");
        userDTO.setPassword("ethic");

        willThrow(new IllegalArgumentException("User already exists"))
                .given(userRegistrationManager)
                .registerUser(any(), any(), any());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Benedict Spinoza\",\"email\":\"bspinoza@gmail.com\",\"password\":\"ethic\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Benedict Spinoza");
        userDTO.setEmail("bspinoza@gmail.com");
        userDTO.setPassword("ethic");

        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Benedict Spinoza\",\"email\":\"bspinoza@gmail.com\",\"password\":\"ethic\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUserShouldReturnNotFound() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Benedict Spinoza");
        userDTO.setEmail("bspinoza@gmail.com");
        userDTO.setPassword("ethic");

        willThrow(new IllegalArgumentException("User not found")).given(userRegistrationManager).updateUser(any());

        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Benedict Spinoza\",\"email\":\"bspinoza@gmail.com\",\"password\":\"ethic\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testDeleteUserShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserShouldReturnNotFound() throws Exception {
        willThrow(new IllegalArgumentException("User not found")).given(userRegistrationManager).deleteUser(anyInt());

        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}