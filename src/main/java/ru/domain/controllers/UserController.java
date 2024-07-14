package ru.domain.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.domain.dto.UserDTO;
import ru.domain.entities.User;
import ru.domain.managers.UserRegistrationManager;
import ru.domain.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

/**
 * Rest-контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Controller Management", description = "API for managing users")
public class UserController {

    private final UserRegistrationManager userRegistrationManager;

    @Autowired
    public UserController(UserRegistrationManager userRegistrationManager) {
        this.userRegistrationManager = userRegistrationManager;
    }

    /**
     * Получаем список всех пользователей.
     *
     * @return the users list
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRegistrationManager.getAllUsers();
        return ResponseEntity.ok(UserMapper.INSTANCE.toDTOList(users));
    }

    /**
     * Получаем пользователя по ID.
     *
     * @param id the user ID
     * @return the user by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user by ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        Optional<User> user = userRegistrationManager.getUserById(id);
        return user.map(value -> ResponseEntity.ok(UserMapper.INSTANCE.toDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Создаем нового пользователя.
     *
     * @param userDTO the user to create
     * @return the HTTP-status
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Add a new user")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.INSTANCE.toEntity(userDTO);
            userRegistrationManager.registerUser(user.getName(), user.getEmail(), user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Обновляем информацию о пользователе.
     *
     * @param id the user ID
     * @param userDTO the user to update
     * @return the HTTP-status
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update existing user", description = "Update user by ID")
    public ResponseEntity<String> updateUser(@PathVariable int id, @Valid @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.INSTANCE.toEntity(userDTO);
            user.setId(id);
            userRegistrationManager.updateUser(user);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Удаляем пользователя по ID
     *
     * @param id the user ID
     * @return the HTTP-status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Remove a user by ID")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            userRegistrationManager.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
