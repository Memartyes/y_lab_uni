package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class CancelWorkspaceBookingCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public CancelWorkspaceBookingCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.cancelWorkspaceBooking();
    }
}
