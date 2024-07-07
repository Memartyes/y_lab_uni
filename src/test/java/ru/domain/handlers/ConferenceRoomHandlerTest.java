//package ru.domain.handlers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import ru.domain.config.WorkspaceConfig;
//import ru.domain.entities.ConferenceRoom;
//import ru.domain.entities.Workspace;
//import ru.domain.io.in.UserInput;
//import ru.domain.io.out.UserOutput;
//import ru.domain.managers.ConferenceRoomManager;
//import ru.domain.managers.WorkspaceManager;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeParseException;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//class ConferenceRoomHandlerTest {
//
//    private UserInput conferenceRoomInput;
//    private UserOutput conferenceRoomOutput;
//    private ConferenceRoomManager conferenceRoomManager;
//    private WorkspaceManager workspaceManager;
//    private ConferenceRoomHandler conferenceRoomHandler;
//
//    @BeforeEach
//    public void setUp() {
//        conferenceRoomInput = mock(UserInput.class);
//        conferenceRoomOutput = mock(UserOutput.class);
//        conferenceRoomManager = mock(ConferenceRoomManager.class);
//        workspaceManager = mock(WorkspaceManager.class);
//        conferenceRoomHandler = new ConferenceRoomHandler(conferenceRoomInput, conferenceRoomOutput, conferenceRoomManager, workspaceManager);
//    }
//
//    @Test
//    public void testHandleCreateConferenceRoom() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room");
//
//        conferenceRoomHandler.handleCreateConferenceRoom();
//
//        verify(conferenceRoomManager, times(1)).addConferenceRoom("Test Room");
//        verify(conferenceRoomOutput, times(1)).println("Conference Room created successfully.");
//    }
//
//    @Test
//    public void testHandleCreateConferenceRoomWithException() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room");
//        doThrow(new IllegalArgumentException("Room already exists")).when(conferenceRoomManager).addConferenceRoom("Test Room");
//
//        conferenceRoomHandler.handleCreateConferenceRoom();
//
//        verify(conferenceRoomManager, times(1)).addConferenceRoom("Test Room");
//        verify(conferenceRoomOutput, times(1)).println("Error creating conference room: Room already exists");
//    }
//
//    @Test
//    public void testHandleViewConferenceRooms() {
//        ConferenceRoom mockRoom = mock(ConferenceRoom.class);
//        Workspace mockWorkspace = mock(Workspace.class);
//
//        when(mockRoom.getName()).thenReturn("Test Room");
//        when(mockRoom.getAvailableWorkspaceCount()).thenReturn(1);
//        when(mockRoom.getWorkspaces()).thenReturn(Collections.singletonList(mockWorkspace));
//        when(mockWorkspace.isBooked()).thenReturn(true);
//        when(mockWorkspace.getName()).thenReturn("Workspace 1");
//        when(mockWorkspace.getBookedBy()).thenReturn("User 1");
//        when(mockWorkspace.getBookingTime()).thenReturn(LocalDateTime.of(2024, 7, 7, 10, 0));
//        when(mockWorkspace.getBookingEndTime()).thenReturn("2024-07-07 11:00");
//
//        when(conferenceRoomManager.getConferenceRooms()).thenReturn(Collections.singletonMap("Test Room", mockRoom));
//
//        conferenceRoomHandler.handleViewConferenceRooms();
//
//        verify(conferenceRoomOutput, times(1)).println("Available Conference Rooms:");
//        verify(conferenceRoomOutput, times(1)).println("Conference Room name: Test Room\nAvailable Workspaces: 1");
//        verify(conferenceRoomOutput, times(1)).println(" Name: Workspace 1 - Booked by Username: User 1 from 2024-07-07T10:00 to 2024-07-07 11:00");
//    }
//
//    @Test
//    public void testHandleUpdateConferenceRoom() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Old Room", "New Room");
//
//        conferenceRoomHandler.handleUpdateConferenceRoom();
//
//        verify(conferenceRoomManager, times(1)).updateConferenceRoomName("Old Room", "New Room");
//        verify(conferenceRoomOutput, times(1)).println("Conference Room updated successfully.");
//    }
//
//    @Test
//    public void testHandleUpdateConferenceRoomWithException() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Old Room", "New Room");
//        doThrow(new IllegalArgumentException("Room not found")).when(conferenceRoomManager).updateConferenceRoomName("Old Room", "New Room");
//
//        conferenceRoomHandler.handleUpdateConferenceRoom();
//
//        verify(conferenceRoomManager, times(1)).updateConferenceRoomName("Old Room", "New Room");
//        verify(conferenceRoomOutput, times(1)).println("Error: Room not found");
//    }
//
//    @Test
//    public void testHandleDeleteConferenceRoom() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room");
//
//        conferenceRoomHandler.handleDeleteConferenceRoom();
//
//        verify(conferenceRoomManager, times(1)).deleteConferenceRoom("Test Room");
//        verify(conferenceRoomOutput, times(1)).println("Conference Room Test Room deleted successfully.");
//    }
//
//    @Test
//    public void testHandleDeleteConferenceRoomWithException() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room");
//        doThrow(new IllegalArgumentException("Room not found")).when(conferenceRoomManager).deleteConferenceRoom("Test Room");
//
//        conferenceRoomHandler.handleDeleteConferenceRoom();
//
//        verify(conferenceRoomManager, times(1)).deleteConferenceRoom("Test Room");
//        verify(conferenceRoomOutput, times(1)).println("Error: Room not found");
//    }
//
//    @Test
//    public void testHandleViewAvailableSlots() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room", "2024-07-07");
//        when(conferenceRoomManager.getAvailableSlots("Test Room", LocalDate.of(2024, 7, 7))).thenReturn(Collections.singletonList("10:00"));
//
//        conferenceRoomHandler.handleViewAvailableSlots();
//
//        verify(conferenceRoomOutput, times(1)).println("Available slots on 2024-07-07 are:");
//        verify(conferenceRoomOutput, times(1)).println("  10:00");
//    }
//
//    @Test
//    public void testHandleViewAvailableSlotsWithInvalidDate() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room", "invalid-date");
//
//        conferenceRoomHandler.handleViewAvailableSlots();
//
//        verify(conferenceRoomOutput, times(1)).println("Error: Invalid date format. Please use 'yyyy-MM-dd'.");
//    }
//
//    @Test
//    public void testHandleBookConferenceRoom() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room", "Test User", "2024-07-07 10:00");
//
//        conferenceRoomHandler.handleBookConferenceRoom();
//
//        verify(conferenceRoomManager, times(1)).bookWholeConferenceRoom("Test Room", "Test User", LocalDateTime.of(2024, 7, 7, 10, 0));
//        verify(conferenceRoomOutput, times(1)).println("Conference room booked successfully.");
//    }
//
//    @Test
//    public void testHandleBookConferenceRoomWithException() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room", "Test User", "2024-07-07 10:00");
//        doThrow(new IllegalArgumentException("Room not found")).when(conferenceRoomManager).bookWholeConferenceRoom("Test Room", "Test User", LocalDateTime.of(2024, 7, 7, 10, 0));
//
//        conferenceRoomHandler.handleBookConferenceRoom();
//
//        verify(conferenceRoomManager, times(1)).bookWholeConferenceRoom("Test Room", "Test User", LocalDateTime.of(2024, 7, 7, 10, 0));
//        verify(conferenceRoomOutput, times(1)).println("Error: Room not found");
//    }
//
//    @Test
//    public void testHandleBookConferenceRoomForWholeDay() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room", "Test User", "2024-07-07");
//
//        conferenceRoomHandler.handleBookConferenceRoomForWholeDay();
//
//        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
//        verify(conferenceRoomManager, times(WorkspaceConfig.END_HOUR.getValue() - WorkspaceConfig.START_HOUR.getValue())).bookWholeConferenceRoom(eq("Test Room"), eq("Test User"), captor.capture());
//
//        List<LocalDateTime> bookingTimes = captor.getAllValues();
//        for (int i = 0; i < bookingTimes.size(); i++) {
//            assertThat(bookingTimes.get(i)).isEqualTo(LocalDateTime.of(2024, 7, 7, WorkspaceConfig.START_HOUR.getValue() + i, 0));
//        }
//        verify(conferenceRoomOutput, times(1)).println("Conference room booked for the whole day successfully.");
//    }
//
//    @Test
//    public void testHandleCancelWorkspaceBooking() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1");
//
//        conferenceRoomHandler.handleCancelWorkspaceBooking();
//
//        verify(conferenceRoomManager, times(1)).cancelBookingForWorkspace("Test Room", "Workspace 1");
//        verify(conferenceRoomOutput, times(1)).println("Workspace canceled successfully.");
//    }
//
//    @Test
//    public void testHandleCancelWorkspaceBookingWithException() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room", "Workspace 1");
//        doThrow(new IllegalArgumentException("Workspace not found")).when(conferenceRoomManager).cancelBookingForWorkspace("Test Room", "Workspace 1");
//
//        conferenceRoomHandler.handleCancelWorkspaceBooking();
//
//        verify(conferenceRoomManager, times(1)).cancelBookingForWorkspace("Test Room", "Workspace 1");
//        verify(conferenceRoomOutput, times(1)).println("Error: Workspace not found");
//    }
//
//    @Test
//    public void testHandleCancelConferenceRoomBooking() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room");
//
//        conferenceRoomHandler.handleCancelConferenceRoomBooking();
//
//        verify(conferenceRoomManager, times(1)).cancelBookingForAllWorkspaces("Test Room");
//        verify(conferenceRoomOutput, times(1)).println("Conference Room booking canceled successfully.");
//    }
//
//    @Test
//    public void testHandleCancelConferenceRoomBookingWithException() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test Room");
//        doThrow(new IllegalArgumentException("Room not found")).when(conferenceRoomManager).cancelBookingForAllWorkspaces("Test Room");
//
//        conferenceRoomHandler.handleCancelConferenceRoomBooking();
//
//        verify(conferenceRoomManager, times(1)).cancelBookingForAllWorkspaces("Test Room");
//        verify(conferenceRoomOutput, times(1)).println("Error: Room not found");
//    }
//
//    @Test
//    public void testHandleFilterBooking() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("1");
//        doNothing().when(conferenceRoomOutput).printList(anyList());
//
//        conferenceRoomHandler.handleFilterBooking();
//
//        verify(conferenceRoomInput, times(1)).readLine(anyString());
//    }
//
//    @Test
//    public void testHandleFilterBookingInvalidOption() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("invalid");
//
//        conferenceRoomHandler.handleFilterBooking();
//
//        verify(conferenceRoomOutput, times(1)).println("Invalid option.");
//    }
//
//    @Test
//    public void testFilterByDate() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("2024-07-07");
//        when(conferenceRoomManager.filterByDate(LocalDate.of(2024, 7, 7))).thenReturn(Collections.singletonList("Test Room"));
//
//        conferenceRoomHandler.handleFilterBooking();
//
//        verify(conferenceRoomOutput, times(1)).printList(Collections.singletonList("Test Room"));
//    }
//
//    @Test
//    public void testFilterByDateWithInvalidOption() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("4");
//
//        conferenceRoomHandler.handleFilterBooking();
//
//        verify(conferenceRoomOutput, times(1)).println("Invalid option.");
//    }
//
//    @Test
//    public void testFilterByUser() {
//        when(conferenceRoomInput.readLine(anyString())).thenReturn("Test User");
//        when(conferenceRoomManager.filterByUser("Test User")).thenReturn(Collections.singletonList("Test Room"));
//
//        conferenceRoomHandler.handleFilterBooking();
//
//        verify(conferenceRoomOutput, times(1)).printList(Collections.singletonList("Test Room"));
//    }
//
//    @Test
//    public void testFilterByAvailableWorkspaces() {
//        when(conferenceRoomManager.filterByAvailableWorkspaces()).thenReturn(Collections.singletonList("Test Room"));
//
//        conferenceRoomHandler.handleFilterBooking();
//
//        verify(conferenceRoomOutput, times(1)).printList(Collections.singletonList("Test Room"));
//    }
//}