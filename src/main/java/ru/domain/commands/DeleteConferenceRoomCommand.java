package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class DeleteConferenceRoomCommand implements Command {
    private final ConferenceRoomHandler conferenceRoomHandler;

    public DeleteConferenceRoomCommand(ConferenceRoomHandler conferenceRoomHandler) {
        this.conferenceRoomHandler = conferenceRoomHandler;
    }

    @Override
    public void execute() {
        conferenceRoomHandler.deleteConferenceRoom();
    }
}
