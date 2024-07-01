package ru.domain.io.in;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Класс для обработки ввода пользователя через консоль
 */
public class ConsoleUserInput implements UserInput {
    private final Scanner scanner = new Scanner(System.in);

    /**
     *
     * @param prompt the prompt
     * @return the input string
     */
    @Override
    public String readLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    /**
     * Считываем ввод пользователя с консоли
     *
     * @return user's input
     */
    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Считываем целое число, введенное пользователем.
     *
     * @param prompt the prompt
     * @return the input integer
     */
    @Override
    public int readInt(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Пожалуйста, введите корректное целое число.");
                scanner.next(); // Очистка некорректного ввода
            }
        }
    }

    /**
     * Закрываем поток Scanner.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
