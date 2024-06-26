package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class ViewConferenceRoomsCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public ViewConferenceRoomsCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.viewConferenceRooms();
    }
}
