package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class FilterBookingCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public FilterBookingCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.filterBooking();
    }
}
