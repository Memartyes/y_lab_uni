package ru.domain.io.in;

public interface UserInput {
    String readLine(String userInput);

    String readLine();

    int readInt(String prompt);

    void close();
}
