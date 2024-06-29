package ru.domain.commands;

import ru.domain.io.ConsoleUserInput;

public class LoginUserCommand implements Command {
    private final ConsoleUserInput userInput;

    public LoginUserCommand(ConsoleUserInput userInput) {
        this.userInput = userInput;
    }

    @Override
    public void execute() {
        userInput.handleLoginUser();
    }
}
