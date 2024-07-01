package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class ViewConferenceRoomsCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public ViewConferenceRoomsCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleViewConferenceRooms();
    }
}
