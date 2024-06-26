package ru.domain.commands;

import ru.domain.io.ConsoleInput;
import ru.domain.io.ConsoleOutput;

public class ExitCommand implements Command {
    private ConsoleOutput output;

    public ExitCommand(ConsoleOutput output) {
        this.output = output;
    }

    @Override
    public void execute() {
        output.println("Goodbye!");
    }
}
