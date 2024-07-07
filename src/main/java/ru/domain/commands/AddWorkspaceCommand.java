package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;
import ru.domain.handlers.WorkspaceHandler;

public class AddWorkspaceCommand implements Command {
    private final ConferenceRoomHandler conferenceRoomHandler;

    public AddWorkspaceCommand(ConferenceRoomHandler conferenceRoomHandler) {
        this.conferenceRoomHandler = conferenceRoomHandler;
    }

    @Override
    public void execute() {
        conferenceRoomHandler.addWorkspaceToConferenceRoom();
    }
}
