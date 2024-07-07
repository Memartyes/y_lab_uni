//package ru.domain.handlers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import ru.domain.entities.User;
//import ru.domain.io.in.UserInput;
//import ru.domain.io.out.UserOutput;
//import ru.domain.managers.UserAuthenticationManager;
//import ru.domain.managers.UserRegistrationManager;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//class UserHandlerTest {
//
//    private UserInput userInput;
//    private UserOutput userOutput;
//    private UserRegistrationManager registrationManager;
//    private UserAuthenticationManager authenticationManager;
//    private UserHandler userHandler;
//
//    @BeforeEach
//    public void setUp() {
//        userInput = mock(UserInput.class);
//        userOutput = mock(UserOutput.class);
//        registrationManager = mock(UserRegistrationManager.class);
//        authenticationManager = mock(UserAuthenticationManager.class);
//        userHandler = new UserHandler(userInput, userOutput, registrationManager, authenticationManager);
//    }
//
//    @Test
//    public void testHandleUserRegistration() {
//        when(userInput.readLine(anyString())).thenReturn("testUser", "password");
//
//        userHandler.handleUserRegistration();
//
//        verify(registrationManager, times(1)).registerUser("testUser", "password");
//        verify(userOutput, times(1)).println("User registered successfully: testUser");
//    }
//
//    @Test
//    public void testHandleUserRegistrationWithException() {
//        when(userInput.readLine(anyString())).thenReturn("testUser", "password");
//        doThrow(new IllegalArgumentException("User already exists")).when(registrationManager).registerUser("testUser", "password");
//
//        userHandler.handleUserRegistration();
//
//        verify(userOutput, times(1)).println("Registration error: User already exists");
//    }
//
//    @Test
//    public void testHandleUserRegistrationWithUnexpectedException() {
//        when(userInput.readLine(anyString())).thenReturn("testUser", "password");
//        doThrow(new RuntimeException("Unexpected error")).when(registrationManager).registerUser("testUser", "password");
//
//        userHandler.handleUserRegistration();
//
//        verify(userOutput, times(1)).println("An unexpected error occurred during registration: Unexpected error");
//    }
//
//    @Test
//    public void testHandleUserLogin() {
//        when(userInput.readLine(anyString())).thenReturn("testUser", "password");
//        when(authenticationManager.loginUser("testUser", "password")).thenReturn(Optional.of(new User("testUser", "password")));
//
//        userHandler.handleUserLogin();
//
//        verify(userOutput, times(1)).println("Logged in successfully: testUser");
//    }
//
//    @Test
//    public void testHandleUserLoginFailed() {
//        when(userInput.readLine(anyString())).thenReturn("testUser", "password");
//        when(authenticationManager.loginUser("testUser", "password")).thenReturn(Optional.empty());
//
//        userHandler.handleUserLogin();
//
//        verify(userOutput, times(1)).println("Login failed: Invalid username or password.");
//    }
//
//    @Test
//    public void testHandleUserLoginWithUnexpectedException() {
//        when(userInput.readLine(anyString())).thenReturn("testUser", "password");
//        doThrow(new RuntimeException("Unexpected error")).when(authenticationManager).loginUser("testUser", "password");
//
//        userHandler.handleUserLogin();
//
//        verify(userOutput, times(1)).println("An unexpected error occurred during login: Unexpected error");
//    }
//}