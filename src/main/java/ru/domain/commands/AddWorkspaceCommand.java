package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;
import ru.domain.handlers.WorkspaceHandler;

public class AddWorkspaceCommand implements Command {
    private WorkspaceHandler workspaceHandler;

    public AddWorkspaceCommand(WorkspaceHandler workspaceHandler) {
        this.workspaceHandler = workspaceHandler;
    }

    @Override
    public void execute() {
        workspaceHandler.handleAddWorkspace();
    }
}
