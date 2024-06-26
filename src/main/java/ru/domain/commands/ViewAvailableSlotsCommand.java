package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class ViewAvailableSlotsCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public ViewAvailableSlotsCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.viewAvailableSlots();
    }
}
