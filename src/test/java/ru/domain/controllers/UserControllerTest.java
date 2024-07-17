//package ru.domain.controllers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.domain.dto.UserDTO;
//import ru.domain.entities.User;
//import ru.domain.managers.UserRegistrationManager;
//import ru.domain.mapper.UserMapper;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserRegistrationManager userRegistrationManager;
//
//    private User user;
//    private UserDTO userDTO;
//
//    @BeforeEach
//    void setUp() {
//        user = new User("Spinoza", "bspinoza@gmail.com", "ethic");
//        user.setId(1);
//        userDTO = UserMapper.INSTANCE.toDTO(user);
//    }
//
//    @Test
//    void testGetAllUsers() throws Exception {
//        given(userRegistrationManager.getAllUsers()).willReturn(Arrays.asList(user));
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value(user.getName()));
//    }
//
//    @Test
//    void testGetUserById() throws Exception {
//        given(userRegistrationManager.getUserById(anyInt())).willReturn(Optional.of(user));
//
//        mockMvc.perform(get("/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(user.getName()));
//    }
//
//    @Test
//    void testCreateUser() throws Exception {
//        doNothing().when(userRegistrationManager).registerUser(any(), any(), any());
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"Spinoza\", \"email\": \"bspinoza@gmail.com\", \"password\": \"ethic\"}"))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void testUpdateUser() throws Exception {
//        doNothing().when(userRegistrationManager).updateUser(any(User.class));
//
//        mockMvc.perform(put("/users/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"Spinoza\", \"email\": \"bspinoza@gmail.com\", \"password\": \"ethic\"}"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeleteUser() throws Exception {
//        doNothing().when(userRegistrationManager).deleteUser(anyInt());
//
//        mockMvc.perform(delete("/users/1"))
//                .andExpect(status().isNoContent());
//    }
//}