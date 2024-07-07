package ru.domain.handlers;

import ru.domain.entities.User;
import ru.domain.io.in.ConsoleUserInput;
import ru.domain.io.in.UserInput;
import ru.domain.io.out.ConsoleUserOutput;
import ru.domain.io.out.UserOutput;
import ru.domain.managers.UserAuthenticationManager;
import ru.domain.managers.UserRegistrationManager;

import java.util.Optional;

/**
 * Определяем класс для регистрации и аутентификации пользователя.
 */
public class UserHandler {
    private final UserInput userInput;
    private final UserOutput userOutput;
    private final UserRegistrationManager registrationManager;
    private final UserAuthenticationManager authenticationManager;

    public UserHandler(UserInput userInput, UserOutput userOutput, UserRegistrationManager registrationManager, UserAuthenticationManager authenticationManager) {
        this.userInput = userInput;
        this.userOutput = userOutput;
        this.registrationManager = registrationManager;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Обрабатываем запрос на регистрацию нового пользователя
     */
    public void handleUserRegistration() {
        String userName = userInput.readLine("Enter Username:");
        String email = userInput.readLine("Enter Email:");
        String password = userInput.readLine("Enter Password:");

        try {
            registrationManager.registerUser(userName, email, password);
            userOutput.println("User registered successfully: " + userName);
        } catch (IllegalArgumentException e) {
            userOutput.println("Registration error: " + e.getMessage());
        } catch (Exception e) {
            userOutput.println("An unexpected error occurred during registration: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на авторизацию пользователя
     */
    public void handleUserLogin() {
        String userName = userInput.readLine("Enter Username:");
        String password = userInput.readLine("Enter Password:");

        try {
            Optional<User> loggedInUser = authenticationManager.loginUser(userName, password);
            if (loggedInUser.isPresent()) {
                userOutput.println("Logged in successfully: " + userName);
            } else {
                userOutput.println("Login failed: Invalid username or password.");
            }
        } catch (Exception e) {
            userOutput.println("An unexpected error occurred during login: " + e.getMessage());
        }
    }

    /**
     * Обрабатывает запрос на обновление информации о пользователе.
     */
    public void handleUpdateUser() {
        String userName = userInput.readLine("Enter Username to update:");
        Optional<User> userOpt = registrationManager.getUser(userName);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String newEmail = userInput.readLine("Enter new Email:");
            String newPassword = userInput.readLine("Enter new Password:");
            user.setEmail(newEmail);
            user.setPassword(newPassword);
            registrationManager.updateUser(user);
            userOutput.println("User updated successfully: " + userName);
        } else {
            userOutput.println("User not found: " + userName);
        }
    }

    /**
     * Обрабатывает запрос на удаление пользователя.
     */
    public void handleDeleteUser() {
        String userName = userInput.readLine("Enter Username to delete:");
        Optional<User> userOpt = registrationManager.getUser(userName);
        if (userOpt.isPresent()) {
            registrationManager.deleteUser(userOpt.get().getId());
            userOutput.println("User deleted successfully: " + userName);
        } else {
            userOutput.println("User not found: " + userName);
        }
    }
}
