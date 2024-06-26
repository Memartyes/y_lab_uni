package ru.domain.io;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.mockito.InOrder;
import org.mockito.Mock;
import ru.domain.config.WorkspaceConfig;
import ru.domain.managers.ConferenceRoomManager;

class ConsoleConferenceRoomInputTest {

    @Mock
    private ConsoleInput mockInput;

    @Mock
    private ConsoleOutput mockOutput;

    @Mock
    private ConferenceRoomManager mockManager;

    @Mock
    private ConsoleConferenceRoomInput consoleInput;

    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        mockInput = mock(ConsoleInput.class);
        mockOutput = mock(ConsoleOutput.class);
        mockManager = mock(ConferenceRoomManager.class);
        consoleInput = new ConsoleConferenceRoomInput(mockInput, mockOutput, mockManager);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testCreateConferenceRoomSuccess() {
        when(mockInput.readLine()).thenReturn("123"); // Simulate user input
        doNothing().when(mockManager).createConferenceRoom("123"); // Mocking the createConferenceRoom method

        consoleInput.createConferenceRoom();

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockOutput).println("Enter Conference Room ID:");
        verify(mockManager).createConferenceRoom(argumentCaptor.capture());
        verify(mockOutput).println("Conference Room created successfully.");
        assertEquals("123", argumentCaptor.getValue()); // Check if the correct ID was passed to manager
    }

    @Test
    void testCreateConferenceRoomFailure() {
        when(mockInput.readLine()).thenReturn("123"); // Simulate user input
        doThrow(new IllegalArgumentException("Room already exists")).when(mockManager).createConferenceRoom("123");

        consoleInput.createConferenceRoom();

        verify(mockOutput).println("Enter Conference Room ID:");
        verify(mockManager).createConferenceRoom("123");
        verify(mockOutput).println("Error creating conference room: Room already exists");
    }

    @Test
    void testViewConferenceRooms() {
    }

    @Test
    void testUpdateConferenceRoomSuccess() {
        when(mockInput.readLine()).thenReturn("OldRoomId", "NewRoomId");
        doNothing().when(mockManager).updateConferenceRoom("OldRoomId", "NewRoomId");

        consoleInput.updateConferenceRoom();

        verify(mockOutput).println("Enter old Conference Room ID:");
        verify(mockOutput).println("Enter new Conference Room ID:");
        verify(mockManager).updateConferenceRoom("OldRoomId", "NewRoomId");
        verify(mockOutput).println("Conference Room updated successfully.");
    }

    @Test
    void testUpdateConferenceRoomFailure() {
        when(mockInput.readLine()).thenReturn("OldRoomId", "NewRoomId");
        doThrow(new IllegalArgumentException("Room not found")).when(mockManager).updateConferenceRoom("OldRoomId", "NewRoomId");

        consoleInput.updateConferenceRoom();

        verify(mockOutput).println("Enter old Conference Room ID:");
        verify(mockOutput).println("Enter new Conference Room ID:");
        verify(mockManager).updateConferenceRoom("OldRoomId", "NewRoomId");
        verify(mockOutput).println("Error updating conference room: Room not found");
    }

    @Test
    void testDeleteConferenceRoomSuccess() {
        when(mockInput.readLine()).thenReturn("123");
        doNothing().when(mockManager).deleteConferenceRoom("123");

        consoleInput.deleteConferenceRoom();

        verify(mockOutput).println("Enter Conference Room ID to delete:");
        verify(mockManager).deleteConferenceRoom("123");
        verify(mockOutput).println("Conference Room deleted successfully.");
    }

    @Test
    void testDeleteConferenceRoomFailure() {
        when(mockInput.readLine()).thenReturn("123");
        doThrow(new IllegalArgumentException("Room not found")).when(mockManager).deleteConferenceRoom("123");

        consoleInput.deleteConferenceRoom();

        verify(mockOutput).println("Enter Conference Room ID to delete:");
        verify(mockManager).deleteConferenceRoom("123");
        verify(mockOutput).println("Error deleting conference room: Room not found");
    }

    @Test
    void testAddWorkspaceSuccess() {
        when(mockInput.readLine()).thenReturn("RoomId", "WorkspaceId");
        doNothing().when(mockManager).addWorkspaceToConferenceRoom("RoomId", "WorkspaceId");

        consoleInput.addWorkspace();

        verify(mockOutput).println("Enter Conference Room ID:");
        verify(mockOutput).println("Enter Workspace ID:");
        verify(mockManager).addWorkspaceToConferenceRoom("RoomId", "WorkspaceId");
        verify(mockOutput).println("Workspace added to conference room successfully.");
    }

    @Test
    void testAddWorkspaceFailure() {
        when(mockInput.readLine()).thenReturn("RoomId", "WorkspaceId");
        doThrow(new IllegalArgumentException("Room not found")).when(mockManager).addWorkspaceToConferenceRoom("RoomId", "WorkspaceId");

        consoleInput.addWorkspace();

        verify(mockOutput).println("Enter Conference Room ID:");
        verify(mockOutput).println("Enter Workspace ID:");
        verify(mockManager).addWorkspaceToConferenceRoom("RoomId", "WorkspaceId");
        verify(mockOutput).println("Error adding workspace to conference room: Room not found");
    }

    @Test
    void testBookWorkspace() {
        when(mockInput.readLine()).thenReturn("room1").thenReturn("workspace1").thenReturn("user1");
        LocalDateTime bookingTime = LocalDateTime.of(2024, 6, 25, 10, 0); // Example booking time

        consoleInput.bookWorkspace();

        InOrder inOrder = inOrder(mockOutput, mockManager, mockInput);
        inOrder.verify(mockOutput).println("Enter conference room ID:");
        inOrder.verify(mockInput, times(3)).readLine();
        inOrder.verify(mockManager).bookWorkspace("room1", "workspace1", "user1", bookingTime);
        inOrder.verify(mockOutput).println("Workspace Workspace1 in conference room room1 booked successfully.");
    }

    @Test
    void testBookConferenceRoom() {
        when(mockInput.readLine()).thenReturn("room1").thenReturn("user1");
        LocalDateTime bookingTime = LocalDateTime.of(2024, 6, 25, 10, 0); // Example booking time

        consoleInput.bookConferenceRoom();

        InOrder inOrder = inOrder(mockOutput, mockManager, mockInput);
        inOrder.verify(mockOutput).println("Enter conference room ID:");
        inOrder.verify(mockInput, times(2)).readLine();
        inOrder.verify(mockManager).bookAllWorkspaces("room1", "user1", bookingTime);
        inOrder.verify(mockOutput).println("All workspaces in conference room Room1 booked successfully.");
    }

    @Test
    void testCancelWorkspaceBooking() {
        when(mockInput.readLine()).thenReturn("room1").thenReturn("workspace1");

        consoleInput.cancelWorkspaceBooking();

        InOrder inOrder = inOrder(mockOutput, mockManager, mockInput);
        inOrder.verify(mockOutput).println("Enter conference room ID:");
        inOrder.verify(mockInput, times(2)).readLine();
        inOrder.verify(mockManager).cancelBookingForWorkspace("Room1", "Workspace1");
        inOrder.verify(mockOutput).println("Booking for workspace Workspace1 in conference room Room1 canceled.");
    }

    @Test
    void testCancelConferenceRoomBooking() {
        when(mockInput.readLine()).thenReturn("Room1");

        consoleInput.cancelConferenceRoomBooking();

        InOrder inOrder = inOrder(mockOutput, mockManager, mockInput);
        inOrder.verify(mockOutput).println("Enter conference Room ID:");
        inOrder.verify(mockInput).readLine();
        inOrder.verify(mockManager).cancelBookingForAllWorkspaces("Room1");
        inOrder.verify(mockOutput).println("All bookings in conference room Room1 canceled.");
    }

    @Test
    void testBookConferenceRoomForWholeDay() {
        when(mockInput.readLine())
                .thenReturn("room1")
                .thenReturn("user1")
                .thenReturn("2026-06-06");

        consoleInput.bookConferenceRoomForWholeDay();

        int startHour = WorkspaceConfig.START_HOUR.getValue();
        int endHour = WorkspaceConfig.END_HOUR.getValue();
        int bookingDuration = WorkspaceConfig.BOOKING_DURATION_HOURS.getValue();
        int expectedInvocations = (endHour - startHour) / bookingDuration;

        LocalDate bookingDate = LocalDate.of(2026, 06, 06);
        LocalDateTime bookingTime = bookingDate.atTime(startHour, 0);

        for (int i = 0; i < expectedInvocations; i++) {
            verify(mockManager).bookAllWorkspaces(
                    eq("room1"), eq("user1"), eq(bookingTime)
            );
            bookingTime = bookingTime.plusHours(bookingDuration);
        }
        outContent.toString();
        assertTrue(outContent.toString().contains("Conference room booked for the whole day successfully."));
    }
}