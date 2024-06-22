package ru.domain.adapters.out;

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
}
