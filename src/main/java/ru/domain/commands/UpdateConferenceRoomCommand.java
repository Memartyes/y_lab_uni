package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class UpdateConferenceRoomCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public UpdateConferenceRoomCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.updateConferenceRoom();
    }
}
