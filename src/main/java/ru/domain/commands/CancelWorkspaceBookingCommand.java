package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;
import ru.domain.handlers.WorkspaceHandler;

public class CancelWorkspaceBookingCommand implements Command {
    private final WorkspaceHandler workspaceHandler;

    public CancelWorkspaceBookingCommand(WorkspaceHandler workspaceHandler) {
        this.workspaceHandler = workspaceHandler;
    }

    @Override
    public void execute() {
        workspaceHandler.handleCancelBookingWorkspace();
    }
}
