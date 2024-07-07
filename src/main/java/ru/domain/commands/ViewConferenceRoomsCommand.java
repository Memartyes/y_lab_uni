package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class ViewConferenceRoomsCommand implements Command {
    private final ConferenceRoomHandler conferenceRoomHandler;

    public ViewConferenceRoomsCommand(ConferenceRoomHandler conferenceRoomHandler) {
        this.conferenceRoomHandler = conferenceRoomHandler;
    }

    @Override
    public void execute() {
        conferenceRoomHandler.viewConferenceRooms();
    }
}
