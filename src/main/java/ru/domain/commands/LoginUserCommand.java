package ru.domain.commands;

import ru.domain.handlers.UserHandler;

public class LoginUserCommand implements Command {
    private final UserHandler userInput;

    public LoginUserCommand(UserHandler userInput) {
        this.userInput = userInput;
    }

    @Override
    public void execute() {
        userInput.handleUserLogin();
    }
}
