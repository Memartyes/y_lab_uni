package ru.domain.io;

/**
 * Класс для обработки ввода пользователя через консоль
 */
public class ConsoleUserInput {
    private final ConsoleInput input;
    private final ConsoleOutput output;

    public ConsoleUserInput(ConsoleInput input, ConsoleOutput output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Считываем ввод пользователя с консоли
     *
     * @return user's input
     */
    public String readLine() {
        return input.readLine();
    }

    public String readLine(String prompt) {
        output.println(prompt);
        return input.readLine();
    }

    public int readInt(String prompt) {
        output.println(prompt);
        return input.readInt();
    }

    /**
     * Закрываем поток Scanner.
     */
    public void close() {
        input.close();
    }
}
