package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class CreateConferenceRoomCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public CreateConferenceRoomCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.createConferenceRoom();
    }
}
