package ru.domain.commands;

import ru.domain.handlers.WorkspaceHandler;

public class BookWorkspaceCommand implements Command {
    private WorkspaceHandler workspaceHandler;

    public BookWorkspaceCommand(WorkspaceHandler workspaceHandler) {
        this.workspaceHandler = workspaceHandler;
    }

    @Override
    public void execute() {
        workspaceHandler.handleBookWorkspace();
    }
}
