package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class DeleteConferenceRoomCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public DeleteConferenceRoomCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleDeleteConferenceRoom();
    }
}
