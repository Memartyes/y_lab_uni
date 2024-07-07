package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class CreateConferenceRoomCommand implements Command {
    private final ConferenceRoomHandler conferenceRoomHandler;

    public CreateConferenceRoomCommand(ConferenceRoomHandler conferenceRoomHandler) {
        this.conferenceRoomHandler = conferenceRoomHandler;
    }

    @Override
    public void execute() {
        conferenceRoomHandler.createConferenceRoom();
    }
}
