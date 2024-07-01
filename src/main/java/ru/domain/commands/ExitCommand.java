package ru.domain.commands;

import ru.domain.io.ConsoleInput;
import ru.domain.io.ConsoleOutput;

public class ExitCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
