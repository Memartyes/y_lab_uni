package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class ViewAvailableSlotsCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public ViewAvailableSlotsCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleViewAvailableSlots();
    }
}
