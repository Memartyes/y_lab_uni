package ru.domain;

import ru.domain.handlers.WorkspaceHandler;
import ru.domain.io.*;
import ru.domain.managers.ConferenceRoomManager;
import ru.domain.managers.UserAuthenticationManager;
import ru.domain.managers.UserRegistrationManager;
import ru.domain.managers.WorkspaceManager;
import ru.domain.menu.MenuCommandExecutor;
import ru.domain.handlers.UserHandler;
import ru.domain.handlers.ConferenceRoomHandler;
import ru.domain.menu.MenuDisplay;
import ru.domain.menu.MenuHandler;

/**
 * Main класс для запуска приложения
 */
public class Main {
    public static void main(String[] args) {
        ConsoleInput input = new ConsoleInput(); // Объект для ввода данных с консоли
        ConsoleOutput output = new ConsoleOutput(); // Объект для вывода данных в консоль
        ConsoleUserInput userInput = new ConsoleUserInput(input, output); // Объект для ввода пользователя через консоль
        ConsoleUserOutput userOutput = new ConsoleUserOutput(output); // Объект для вывода пользователя через консоль
        ConsoleConferenceRoomInput conferenceRoomInput = new ConsoleConferenceRoomInput(input, output); // Объект для ввода конференц-зала через консоль
        ConsoleConferenceRoomOutput conferenceRoomOutput = new ConsoleConferenceRoomOutput(output); // Объект для вывода конференц-зала через консоль

        UserRegistrationManager registrationManager = new UserRegistrationManager(); // Менеджер для регистрации пользователей
        UserAuthenticationManager userAuthenticationManager = new UserAuthenticationManager(); // Менеджер для аутентификации пользователей
        ConferenceRoomManager conferenceRoomManager = new ConferenceRoomManager(); // Менеджер для управления конференц-залами
        WorkspaceManager workspaceManager = new WorkspaceManager(conferenceRoomManager); // Менеджер для управления рабочими местами

        UserHandler userHandle = new UserHandler(userInput, userOutput, registrationManager, userAuthenticationManager); // Сервис для обработки операций с пользователями
        ConferenceRoomHandler conferenceRoomHandle = new ConferenceRoomHandler(conferenceRoomInput, conferenceRoomOutput, conferenceRoomManager, workspaceManager); // Сервис для обработки операций с конференц-залами
        WorkspaceHandler workspaceHandle = new WorkspaceHandler(conferenceRoomInput, output, workspaceManager); // Сервис для обработки операций с рабочими местами

        MenuCommandExecutor commandExecutor = new MenuCommandExecutor(userHandle, conferenceRoomHandle, workspaceHandle); // Исполнитель команд меню
        MenuDisplay menuDisplay = new MenuDisplay(output); // Отображение меню
        MenuHandler menuHandler = new MenuHandler(userInput, commandExecutor, menuDisplay); // Обработчик ввода пользователя в меню

        try {
            menuHandler.showMainMenu(); // Запуск меню приложения
        } finally {
            input.close(); // Закрываем поток Scanner
        }
    }
}
