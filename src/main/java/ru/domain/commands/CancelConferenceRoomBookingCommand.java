package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class CancelConferenceRoomBookingCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public CancelConferenceRoomBookingCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleCancelConferenceRoomBooking();
    }
}
