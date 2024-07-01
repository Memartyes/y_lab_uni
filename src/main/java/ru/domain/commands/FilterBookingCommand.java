package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class FilterBookingCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public FilterBookingCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleFilterBooking();
    }
}
