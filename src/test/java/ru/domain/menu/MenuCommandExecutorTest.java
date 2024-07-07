//package ru.domain.menu;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import ru.domain.commands.*;
//import ru.domain.handlers.ConferenceRoomHandler;
//import ru.domain.handlers.UserHandler;
//import ru.domain.handlers.WorkspaceHandler;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.times;
//
//class MenuCommandExecutorTest {
//    private MenuCommandExecutor menuCommandExecutor;
//    private UserHandler userHandler;
//    private ConferenceRoomHandler conferenceRoomHandler;
//    private WorkspaceHandler workspaceHandler;
//
//    @BeforeEach
//    public void setUp() {
//        userHandler = Mockito.mock(UserHandler.class);
//        conferenceRoomHandler = Mockito.mock(ConferenceRoomHandler.class);
//        workspaceHandler = Mockito.mock(WorkspaceHandler.class);
//        menuCommandExecutor = new MenuCommandExecutor(userHandler, conferenceRoomHandler, workspaceHandler);
//    }
//
//    @Test
//    public void testInitializeCommands() {
//        assertThat(menuCommandExecutor).isNotNull();
//    }
//
//    @Test
//    public void testExecuteCommand_RegisterUser() {
//        menuCommandExecutor.executeCommand("1");
//        verify(userHandler, times(1)).handleUserRegistration();
//    }
//
//    @Test
//    public void testExecuteCommand_LoginUser() {
//        menuCommandExecutor.executeCommand("2");
//        verify(userHandler, times(1)).handleUserLogin();
//    }
//
//    @Test
//    public void testExecuteCommand_CreateConferenceRoom() {
//        menuCommandExecutor.executeCommand("3");
//        verify(conferenceRoomHandler, times(1)).handleCreateConferenceRoom();
//    }
//
//    @Test
//    public void testExecuteCommand_ViewConferenceRooms() {
//        menuCommandExecutor.executeCommand("4");
//        verify(conferenceRoomHandler, times(1)).handleViewConferenceRooms();
//    }
//
//    @Test
//    public void testExecuteCommand_UpdateConferenceRoom() {
//        menuCommandExecutor.executeCommand("5");
//        verify(conferenceRoomHandler, times(1)).handleUpdateConferenceRoom();
//    }
//
//    @Test
//    public void testExecuteCommand_DeleteConferenceRoom() {
//        menuCommandExecutor.executeCommand("6");
//        verify(conferenceRoomHandler, times(1)).handleDeleteConferenceRoom();
//    }
//
//    @Test
//    public void testExecuteCommand_AddWorkspace() {
//        menuCommandExecutor.executeCommand("7");
//        verify(workspaceHandler, times(1)).handleAddWorkspace();
//    }
//
//    @Test
//    public void testExecuteCommand_BookWorkspace() {
//        menuCommandExecutor.executeCommand("8");
//        verify(workspaceHandler, times(1)).handleBookWorkspace();
//    }
//
//    @Test
//    public void testExecuteCommand_ViewAvailableSlots() {
//        menuCommandExecutor.executeCommand("9");
//        verify(conferenceRoomHandler, times(1)).handleViewAvailableSlots();
//    }
//
//    @Test
//    public void testExecuteCommand_BookConferenceRoom() {
//        menuCommandExecutor.executeCommand("10");
//        verify(conferenceRoomHandler, times(1)).handleBookConferenceRoom();
//    }
//
//    @Test
//    public void testExecuteCommand_CancelWorkspaceBooking() {
//        menuCommandExecutor.executeCommand("11");
//        verify(conferenceRoomHandler, times(1)).handleCancelWorkspaceBooking();
//    }
//
//    @Test
//    public void testExecuteCommand_CancelConferenceRoomBooking() {
//        menuCommandExecutor.executeCommand("12");
//        verify(conferenceRoomHandler, times(1)).handleCancelConferenceRoomBooking();
//    }
//
//    @Test
//    public void testExecuteCommand_FilterBooking() {
//        menuCommandExecutor.executeCommand("13");
//        verify(conferenceRoomHandler, times(1)).handleFilterBooking();
//    }
//
//    @Test
//    public void testExecuteCommand_Exit() {
//        boolean exit = menuCommandExecutor.executeCommand("0");
//        assertThat(exit).isTrue();
//    }
//
//    @Test
//    public void testExecuteCommand_InvalidOption() {
//        boolean result = menuCommandExecutor.executeCommand("invalid");
//        assertThat(result).isFalse();
//    }
//}