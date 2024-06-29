package ru.domain.io;

import java.util.Scanner;

/**
 * Определяем класс для оброаботки ввода через консоль
 */
public class ConsoleInput {
    private Scanner scanner;

    public ConsoleInput() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Считываем строку введенную из консоли.
     *
     * @return the input line
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Закрываем поток Scanner.
     */
    public void close() {
        scanner.close();
    }
}
