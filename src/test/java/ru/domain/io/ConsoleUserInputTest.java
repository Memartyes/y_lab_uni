package ru.domain.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.domain.managers.UserManager;

import static org.mockito.Mockito.*;

class ConsoleUserInputTest {

    private ConsoleInput mockConsoleInput;
    private ConsoleOutput mockConsoleOutput;
    private UserManager mockUserManager;
    private ConsoleUserInput consoleUserInput;

    @BeforeEach
    void setUp() {
        mockConsoleInput = mock(ConsoleInput.class);
        mockConsoleOutput = mock(ConsoleOutput.class);
        mockUserManager = mock(UserManager.class);
        consoleUserInput = new ConsoleUserInput(mockConsoleInput, mockConsoleOutput, mockUserManager);
    }

    @Test
    void testHandleRegisterUser_Success() {
        when(mockConsoleInput.readLine())
                .thenReturn("testUserId")
                .thenReturn("testPassword");

        consoleUserInput.handleRegisterUser();

        verify(mockConsoleOutput).println("Enter IO:");
        verify(mockConsoleOutput).println("Enter Password:");
        verify(mockUserManager).registerUser("testUserId", "testPassword");
        verify(mockConsoleOutput).println("User registered successfully: ");
    }

    @Test
    void testHandleRegisterUser_Failure() {
        when(mockConsoleInput.readLine())
                .thenReturn("existingUserId")
                .thenReturn("testPassword");
        doThrow(new IllegalArgumentException("User with id existingUserId already exists"))
                .when(mockUserManager).registerUser("existingUserId", "testPassword");

        consoleUserInput.handleRegisterUser();

        verify(mockConsoleOutput).println("Enter IO:");
        verify(mockConsoleOutput).println("Enter Password:");
        verify(mockUserManager).registerUser("existingUserId", "testPassword");
        verify(mockConsoleOutput).println("Error: User with id existingUserId already exists");
    }

    @Test
    void testHandleLoginUser_Success() {
        when(mockConsoleInput.readLine())
                .thenReturn("testUserId")
                .thenReturn("testPassword");
        when(mockUserManager.loginUser("testUserId", "testPassword"))
                .thenReturn(true);

        consoleUserInput.handleLoginUser();

        verify(mockConsoleOutput).println("Enter IO:");
        verify(mockConsoleOutput).println("Enter Password:");
        verify(mockUserManager).loginUser("testUserId", "testPassword");
        verify(mockConsoleOutput).println("Logged in successfully");
    }

    @Test
    void testHandleLoginUser_Failure() {
        when(mockConsoleInput.readLine())
                .thenReturn("testUserId")
                .thenReturn("testPassword");
        when(mockUserManager.loginUser("testUserId", "testPassword"))
                .thenReturn(false);

        consoleUserInput.handleLoginUser();

        verify(mockConsoleOutput).println("Enter IO:");
        verify(mockConsoleOutput).println("Enter Password:");
        verify(mockUserManager).loginUser("testUserId", "testPassword");
        verify(mockConsoleOutput).println("Login failed");
    }
}