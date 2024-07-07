package ru.domain.commands;

import ru.domain.handlers.UserHandler;

public class LoginUserCommand implements Command {
    private final UserHandler userHandler;

    public LoginUserCommand(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public void execute() {
        userHandler.handleUserLogin();
    }
}
