package ru.domain.adapters.out;

import java.util.List;

/**
 * Определяем класс для оброаботки вывода через консоль
 */
public class ConsoleOutput {

    /**
     * Выводим сообщение через консоль
     *
     * @param message the message to print with a new line
     */
    public void println(String message) {
        System.out.println(message);
    }

    /**
     * Выводим List в текстовом виде в консоль
     * @param list the list of strings to print
     */
    public void printList(List<String> list) {
        for (String s : list) {
            System.out.println(s);
        }
    }
}
