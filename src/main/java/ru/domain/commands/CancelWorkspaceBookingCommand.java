package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class CancelWorkspaceBookingCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public CancelWorkspaceBookingCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleCancelWorkspaceBooking();
    }
}
