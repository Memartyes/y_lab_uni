package ru.domain.commands;

import ru.domain.handlers.UserHandler;

public class RegisterUserCommand implements Command {
    private UserHandler userInput;

    public RegisterUserCommand(UserHandler userInput) {
        this.userInput = userInput;
    }

    @Override
    public void execute() {
        userInput.handleRegisterUser();
    }
}
