package ru.domain.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.domain.config.WorkspaceConfig;
import ru.domain.io.in.UserInput;
import ru.domain.io.out.UserOutput;
import ru.domain.managers.ConferenceRoomManager;
import ru.domain.managers.WorkspaceManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class WorkspaceHandlerTest {

    private UserInput userInput;
    private UserOutput userOutput;
    private WorkspaceManager workspaceManager;
    private ConferenceRoomManager conferenceRoomManager;
    private WorkspaceHandler workspaceHandler;

    @BeforeEach
    public void setUp() {
        userInput = mock(UserInput.class);
        userOutput = mock(UserOutput.class);
        workspaceManager = mock(WorkspaceManager.class);
        conferenceRoomManager = mock(ConferenceRoomManager.class);
        workspaceHandler = new WorkspaceHandler(userInput, userOutput, workspaceManager, conferenceRoomManager);
    }

    @Test
    public void testHandleAddWorkspace() {
        when(userInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1");

        workspaceHandler.handleAddWorkspace();

        verify(conferenceRoomManager, times(1)).addWorkspaceToConferenceRoom("Test Room", "Workspace 1");
        verify(userOutput, times(1)).println("Workspace Workspace 1 added successfully to conference room: Test Room");
    }

    @Test
    public void testHandleAddWorkspaceWithEmptyFields() {
        when(userInput.readLine(anyString())).thenReturn("", "Workspace 1");

        workspaceHandler.handleAddWorkspace();

        verify(userOutput, times(1)).println("Conference Room Name and Workspace Name cannot be empty.");
        verify(conferenceRoomManager, times(0)).addWorkspaceToConferenceRoom(anyString(), anyString());
    }

    @Test
    public void testHandleAddWorkspaceWithException() {
        when(userInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1");
        doThrow(new IllegalArgumentException("Workspace already exists")).when(conferenceRoomManager).addWorkspaceToConferenceRoom("Test Room", "Workspace 1");

        workspaceHandler.handleAddWorkspace();

        verify(userOutput, times(1)).println("Error: Workspace already exists");
    }

    @Test
    public void testHandleBookWorkspace() {
        when(userInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1", "Test User", "2024-07-07 10:00");

        workspaceHandler.handleBookWorkspace();

        verify(conferenceRoomManager, times(1)).bookWorkspace("Test Room", "Workspace 1", "Test User", LocalDateTime.of(2024, 7, 7, 10, 0), WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());
        verify(userOutput, times(1)).println("Workspace booked successfully.");
    }

    @Test
    public void testHandleBookWorkspaceWithEmptyFields() {
        when(userInput.readLine(anyString())).thenReturn("", "Workspace 1", "Test User", "2024-07-07 10:00");

        workspaceHandler.handleBookWorkspace();

        verify(userOutput, times(1)).println("All fields are required");
        verify(conferenceRoomManager, times(0)).bookWorkspace(anyString(), anyString(), anyString(), any(LocalDateTime.class), anyInt());
    }

    @Test
    public void testHandleBookWorkspaceWithInvalidDateFormat() {
        when(userInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1", "Test User", "invalid-date");

        workspaceHandler.handleBookWorkspace();

        verify(userOutput, times(1)).println("Error: invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
        verify(conferenceRoomManager, times(0)).bookWorkspace(anyString(), anyString(), anyString(), any(LocalDateTime.class), anyInt());
    }

    @Test
    public void testHandleBookWorkspaceWithException() {
        when(userInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1", "Test User", "2024-07-07 10:00");
        doThrow(new IllegalArgumentException("Workspace already booked")).when(conferenceRoomManager).bookWorkspace("Test Room", "Workspace 1", "Test User", LocalDateTime.of(2024, 7, 7, 10, 0), WorkspaceConfig.BOOKING_DURATION_HOURS.getValue());

        workspaceHandler.handleBookWorkspace();

        verify(userOutput, times(1)).println("Error: Workspace already booked");
    }

    @Test
    public void testHandleCancelBookingWorkspace() {
        when(userInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1");

        workspaceHandler.handleCancelBookingWorkspace();

        verify(conferenceRoomManager, times(1)).cancelBookingForWorkspace("Test Room", "Workspace 1");
        verify(userOutput, times(1)).println("Booking cancel for workspace: Workspace 1 in conference room Test Room");
    }

    @Test
    public void testHandleCancelBookingWorkspaceWithEmptyFields() {
        when(userInput.readLine(anyString())).thenReturn("", "Workspace 1");

        workspaceHandler.handleCancelBookingWorkspace();

        verify(userOutput, times(1)).println("Conference Room Name and Workspace Name cannot be empty.");
        verify(conferenceRoomManager, times(0)).cancelBookingForWorkspace(anyString(), anyString());
    }

    @Test
    public void testHandleCancelBookingWorkspaceWithException() {
        when(userInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1");
        doThrow(new IllegalArgumentException("Workspace not found")).when(conferenceRoomManager).cancelBookingForWorkspace("Test Room", "Workspace 1");

        workspaceHandler.handleCancelBookingWorkspace();

        verify(userOutput, times(1)).println("Error: Workspace not found");
    }

    @Test
    public void testHandleCancelBookingForAllWorkspaces() {
        when(userInput.readLine(anyString())).thenReturn("Test Room");

        workspaceHandler.handleCancelBookingForAllWorkspaces();

        verify(conferenceRoomManager, times(1)).cancelBookingForAllWorkspaces("Test Room");
        verify(userOutput, times(1)).println("All bookings cancelled for conference room: Test Room");
    }

    @Test
    public void testHandleCancelBookingForAllWorkspacesWithEmptyField() {
        when(userInput.readLine(anyString())).thenReturn("");

        workspaceHandler.handleCancelBookingForAllWorkspaces();

        verify(userOutput, times(1)).println("Conference Room Name cannot be empty.");
        verify(conferenceRoomManager, times(0)).cancelBookingForAllWorkspaces(anyString());
    }

    @Test
    public void testHandleCancelBookingForAllWorkspacesWithException() {
        when(userInput.readLine(anyString())).thenReturn("Test Room");
        doThrow(new IllegalArgumentException("Room not found")).when(conferenceRoomManager).cancelBookingForAllWorkspaces("Test Room");

        workspaceHandler.handleCancelBookingForAllWorkspaces();

        verify(userOutput, times(1)).println("Error: Room not found");
    }

    @Test
    public void testHandleReadBookingDateTime() {
        when(userInput.readLine(anyString())).thenReturn("2024-07-07 10:00");

        LocalDateTime dateTime = workspaceHandler.handleReadBookingDateTime();

        assertThat(dateTime).isEqualTo(LocalDateTime.of(2024, 7, 7, 10, 0));
    }

    @Test
    public void testHandleReadBookingDateTimeWithInvalidFormat() {
        when(userInput.readLine(anyString())).thenReturn("invalid-date");

        LocalDateTime dateTime = workspaceHandler.handleReadBookingDateTime();

        assertThat(dateTime).isNull();
        verify(userOutput, times(1)).println("Error: Invalid date format. Please use 'yyyy-MM-dd HH:mm'.");
    }
}