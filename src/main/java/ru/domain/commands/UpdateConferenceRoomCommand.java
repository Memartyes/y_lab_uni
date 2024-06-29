package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class UpdateConferenceRoomCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public UpdateConferenceRoomCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleUpdateConferenceRoom();
    }
}
