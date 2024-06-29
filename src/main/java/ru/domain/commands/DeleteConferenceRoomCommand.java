package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class DeleteConferenceRoomCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public DeleteConferenceRoomCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.deleteConferenceRoom();
    }
}
