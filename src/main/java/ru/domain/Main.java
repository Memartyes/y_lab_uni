package ru.domain;

import ru.domain.adapters.in.ConsoleConferenceRoomInput;
import ru.domain.adapters.in.ConsoleUserInput;
import ru.domain.adapters.in.ConsoleInput;
import ru.domain.adapters.out.ConsoleOutput;
import ru.domain.usecases.ConferenceRoomManager;
import ru.domain.usecases.UserManager;

/**
 * Main класс для запуска приложения
 */
public class Main {
    public static void main(String[] args) {
        ConsoleInput input = new ConsoleInput(); //Создаем обьекты ввода, вывода консоли.
        ConsoleOutput output = new ConsoleOutput();

        UserManager userManager = new UserManager(); //User регистрация и аутентификация
        ConferenceRoomManager conferenceRoomManager = new ConferenceRoomManager(); //Создаем обьекты управления Конференц-залами и их взаимодейсвтия в консоли

        ConsoleConferenceRoomInput conferenceRoomInput = new ConsoleConferenceRoomInput(input, output, conferenceRoomManager);
        ConsoleUserInput userInput = new ConsoleUserInput(input, output, userManager);

        MenuHandler menuHandler = new MenuHandler(userInput, conferenceRoomInput);
        menuHandler.displayMenu(); //Запуск меню приложения
    }
}
