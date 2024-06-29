package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class BookWorkspaceCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public BookWorkspaceCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.bookWorkspace();
    }
}
