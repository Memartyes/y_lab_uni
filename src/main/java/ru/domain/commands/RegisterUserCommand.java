package ru.domain.commands;

import ru.domain.handlers.UserHandler;

public class RegisterUserCommand implements Command {
    private final UserHandler userHandler;

    public RegisterUserCommand(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public void execute() {
        userHandler.handleUserRegistration();
    }
}
