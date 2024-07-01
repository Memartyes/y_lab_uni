package ru.domain.io;

import java.util.List;

/**
 * Класс для обработки вывода информации о конференц-зале через консоль
 */
public class ConsoleConferenceRoomOutput {
    private final ConsoleOutput output;

    public ConsoleConferenceRoomOutput(ConsoleOutput output) {
        this.output = output;
    }

    /**
     * Выводим указанный message в консоль.
     *
     * @param message the message
     */
    public void printMessage(String message) {
        output.println(message);
    }

    /**
     * Выводим List в текстовом виде в консоль
     * @param list the list of strings to print
     */
    public void printList(List<String> list) {
        for (String s : list) {
            output.println(s);
        }
    }

    public void displayConferenceRoomName(String name) {
        output.println("Conference room name: " + name);
    }
}
