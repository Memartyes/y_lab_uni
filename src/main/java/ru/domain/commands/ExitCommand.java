package ru.domain.commands;

public class ExitCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Goodbye!");
    }
}
