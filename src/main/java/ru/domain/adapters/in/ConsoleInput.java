package ru.domain.adapters.in;

import java.util.Scanner;

/**
 * Определяем класс для оброаботки ввода через консоль
 */
public class ConsoleInput {
    private Scanner scanner = new Scanner(System.in);

    /**
     * Считываем строку введенную из консоли
     *
     * @return the input line
     */
    public String readLine() {
        return scanner.nextLine();
    }
}
