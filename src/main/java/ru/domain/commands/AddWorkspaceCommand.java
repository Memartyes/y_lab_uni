package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;
import ru.domain.io.ConsoleUserInput;

public class AddWorkspaceCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public AddWorkspaceCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.addWorkspace();
    }
}
