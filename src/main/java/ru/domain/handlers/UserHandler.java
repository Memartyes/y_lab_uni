package ru.domain.handlers;

import ru.domain.entities.User;
import ru.domain.managers.UserAuthenticationManager;
import ru.domain.managers.UserRegistrationManager;

import java.util.Optional;

/**
 * Определяем класс для регистрации и аутентификации пользователя.
 */
public class UserHandler {
    private final UserRegistrationManager registrationManager;
    private final UserAuthenticationManager authenticationManager;

    public UserHandler(UserRegistrationManager registrationManager, UserAuthenticationManager authenticationManager) {
        this.registrationManager = registrationManager;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Обрабатываем запрос на регистрацию нового пользователя
     */
    public void handleUserRegistration(String userName, String email, String password) {
        try {
            registrationManager.registerUser(userName, email, password);
            System.out.println("User registered successfully: " + userName);
        } catch (IllegalArgumentException e) {
            System.out.println("Registration error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during registration: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на авторизацию пользователя
     */
    public void handleUserLogin(String userName, String password) {
        try {
            Optional<User> loggedInUser = authenticationManager.loginUser(userName, password);
            if (loggedInUser.isPresent()) {
                System.out.println("Logged in successfully: " + userName);
            } else {
                System.out.println("Login failed: Invalid username or password.");
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during login: " + e.getMessage());
        }
    }
}
