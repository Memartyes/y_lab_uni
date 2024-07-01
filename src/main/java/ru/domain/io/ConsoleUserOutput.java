package ru.domain.io;

import java.util.Scanner;

/**
 * Класс для обработки вывода пользователя через консоль
 */
public class ConsoleUserOutput {
    private final ConsoleOutput output;

    public ConsoleUserOutput(ConsoleOutput output) {
        this.output = output;
    }

    /**
     * Выводим указанный message в консоль.
     *
     * @param message the message print to
     */
    public void println(String message) {
        output.println(message);
    }
}
