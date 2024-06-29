package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class CreateConferenceRoomCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public CreateConferenceRoomCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleCreateConferenceRoom();
    }
}
