package ru.domain.io.out;

import java.util.List;
import java.util.Scanner;

/**
 * Класс для обработки вывода пользователя через консоль
 */
public class ConsoleUserOutput implements UserOutput {

    /**
     * Выводим указанный message в консоль.
     *
     * @param message the message print to
     */
    @Override
    public void println(String message) {
        System.out.println(message);
    }

    /**
     * Выводим List в текстовом виде в консоль
     * @param list the list of strings to print
     */
    @Override
    public void printList(List<String> list) {
        for (String s : list) {
            System.out.println(s);
        }
    }
}
