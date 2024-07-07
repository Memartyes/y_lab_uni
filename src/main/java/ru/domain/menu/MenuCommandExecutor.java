package ru.domain.menu;

import ru.domain.commands.*;
import ru.domain.handlers.ConferenceRoomHandler;
import ru.domain.handlers.UserHandler;
import ru.domain.handlers.WorkspaceHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для обработки запросов пользователя.
 */
public class MenuCommandExecutor {
    private final Map<String, Command> commandMap;

    public MenuCommandExecutor(UserHandler userHandler, ConferenceRoomHandler conferenceRoomHandler, WorkspaceHandler workspaceHandler) {
        commandMap = new HashMap<>();
        initializeCommands(userHandler, conferenceRoomHandler, workspaceHandler);
    }

    /**
     * Инициализирует команды
     */
    private void initializeCommands(UserHandler userHandler, ConferenceRoomHandler conferenceRoomHandler, WorkspaceHandler workspaceHandler) {
        commandMap.put("1", new RegisterUserCommand(userHandler)); //Регистрируем пользователя
        commandMap.put("2", new LoginUserCommand(userHandler)); //Авторизируем пользователя
        commandMap.put("3", new CreateConferenceRoomCommand(conferenceRoomHandler)); //Создаем Конференц-зал
        commandMap.put("4", new ViewConferenceRoomsCommand(conferenceRoomHandler)); //Просматриваем доступные Конференц-залы и рабочие места
        commandMap.put("5", new UpdateConferenceRoomCommand(conferenceRoomHandler)); //Обновляем наименование Конференц-зала
        commandMap.put("6", new DeleteConferenceRoomCommand(conferenceRoomHandler)); //Удаляем конференц зал
        commandMap.put("7", new AddWorkspaceCommand(conferenceRoomHandler)); //Добавляем рабочие места
        commandMap.put("8", new BookWorkspaceCommand(workspaceHandler)); //Бронируем рабочие места
        commandMap.put("9", new CancelWorkspaceBookingCommand(workspaceHandler)); //Отменяем бронирование рабочего места.
        commandMap.put("0", new ExitCommand()); //Выход из консоли
    }

    /**
     * Выполняем компанду на основе выбора пользователя
     *
     * @param choice the user choice
     * @return true if user's input equals '0', false otherwise
     */
    public boolean executeCommand(String choice) {
        Command command = commandMap.get(choice);
        if (command != null) {
            command.execute();
            return choice.equals("0");
        } else {
            System.out.println("Invalid option. Please try again.");
            return false;
        }
    }
}
