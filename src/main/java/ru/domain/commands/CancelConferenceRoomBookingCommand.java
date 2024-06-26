package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class CancelConferenceRoomBookingCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public CancelConferenceRoomBookingCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.cancelConferenceRoomBooking();
    }
}
