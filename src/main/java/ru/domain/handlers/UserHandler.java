package ru.domain.handlers;

import ru.domain.io.ConsoleUserInput;
import ru.domain.io.ConsoleUserOutput;
import ru.domain.managers.UserAuthenticationManager;
import ru.domain.managers.UserRegistrationManager;

/**
 * Определяем класс для регистрации и аутентификации пользователя.
 */
public class UserHandler {
    private ConsoleUserInput userInput;
    private ConsoleUserOutput userOutput;
    private UserRegistrationManager registrationManager;
    private UserAuthenticationManager authenticationManager;

    public UserHandler(ConsoleUserInput userInput, ConsoleUserOutput userOutput, UserRegistrationManager registrationManager, UserAuthenticationManager authenticationManager) {
        this.userInput = userInput;
        this.userOutput = userOutput;
        this.registrationManager = registrationManager;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Обрабатываем запрос на регистрацию нового пользователя
     */
    public void handleRegisterUser() {
        String userName = userInput.readLine("Enter Username:");
        String password = userInput.readLine("Enter Password:");

        try {
            registrationManager.registerUser(userName, password);
            userOutput.println("User registered successfully: ");
        } catch (IllegalArgumentException e) {
            userOutput.println("Error: " + e.getMessage());
        }
    }

    /**
     * Обрабатываем запрос на авторизацию пользователя
     */
    public void handleLoginUser() {
        String userName = userInput.readLine("Enter Username:");
        String password = userInput.readLine("Enter Password:");

        boolean loggedIn = authenticationManager.loginUser(userName, password);
        if (loggedIn) {
            userOutput.println("Logged in successfully");
        } else {
            userOutput.println("Login failed");
        }
    }
}
