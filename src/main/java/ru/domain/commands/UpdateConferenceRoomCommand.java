package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class UpdateConferenceRoomCommand implements Command {
    private final ConferenceRoomHandler conferenceRoomHandler;

    public UpdateConferenceRoomCommand(ConferenceRoomHandler conferenceRoomHandler) {
        this.conferenceRoomHandler = conferenceRoomHandler;
    }

    @Override
    public void execute() {
        conferenceRoomHandler.updateConferenceRoom();
    }
}
