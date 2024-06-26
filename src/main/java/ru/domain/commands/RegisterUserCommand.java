package ru.domain.commands;

import ru.domain.io.ConsoleUserInput;

public class RegisterUserCommand implements Command {
    private ConsoleUserInput userInput;

    public RegisterUserCommand(ConsoleUserInput userInput) {
        this.userInput = userInput;
    }

    @Override
    public void execute() {
        userInput.handleRegisterUser();
    }
}
