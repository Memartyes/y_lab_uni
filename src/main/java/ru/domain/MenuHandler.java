package ru.domain;

import ru.domain.commands.*;
import ru.domain.io.ConsoleConferenceRoomInput;
import ru.domain.io.ConsoleUserInput;
import ru.domain.io.ConsoleInput;
import ru.domain.io.ConsoleOutput;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для обработки бизнес-логики запуска приложения
 */
public class MenuHandler {
    private final ConsoleOutput output;
    private final ConsoleUserInput userInput;
    private final ConsoleConferenceRoomInput conferenceRoomInput;
    private final Map<String, Command> commandMap;


    public MenuHandler(ConsoleUserInput userInput, ConsoleConferenceRoomInput conferenceRoomInput, ConsoleOutput output) {
        this.userInput = userInput;
        this.conferenceRoomInput = conferenceRoomInput;
        this.output = output;
        commandMap = new HashMap<>();
        initializeCommands();
    }

    /**
     * Метод обрабатывающий вывод информации на экран,
     * а также обрабатывает непосредственно ввод пользователя в консоль.
     */
    public void showMainMenu() {
        boolean exit = false;

        while (!exit) {
            displayMenu();
            String choice = userInput.readLine();
            exit = handleUserChoice(choice);
        }
    }

    /**
     * Метод отоброжающий и выводящий в консоль информацию по доступному функционалу.
     */
    public void displayMenu() {
        output.println("Choose an option:");
        output.println("1. User Registration");
        output.println("2. User Authorization");
        output.println("3. Create Conference Room");
        output.println("4. Read/View Conference Room");
        output.println("5. Update Conference Room");
        output.println("6. Delete Conference Room");
        output.println("7. Add Workspace");
        output.println("8. Book Workspace");
        output.println("9. View Available Slots in Conference Room");
        output.println("10. Book Conference Room");
        output.println("11. Cancel Workspace Booking");
        output.println("12. Cancel Conference Room Booking");
        output.println("13. Filter Bookings");
        output.println("0. Exit");
    }

    /**
     * Метод обрабатывающий ввод пользователя в консоль.
     *
     * @param choice the user's choice of available functions
     * @return true while user's been using the console with available choices, false otherwise
     */
    private boolean handleUserChoice(String choice) {
        Command command = commandMap.get(choice);
        if (command != null) {
            command.execute();
            return choice.equals("0");
        } else { //Неверный input
            output.println("Invalid option. Please try again.");
            return false;
        }
    }

    /**
     * Инициализируем комманды введенные пользователем в консоль
     */
    public void initializeCommands() {
        commandMap.put("1", new RegisterUserCommand(userInput)); //Регистрируем пользователя
        commandMap.put("2", new LoginUserCommand(userInput)); //Авторизируем пользователя
        commandMap.put("3", new CreateConferenceRoomCommand(conferenceRoomInput)); //Создаем Конференц-зал
        commandMap.put("4", new ViewConferenceRoomsCommand(conferenceRoomInput)); //Просматриваем доступные Конференц-залы и рабочие места
        commandMap.put("5", new UpdateConferenceRoomCommand(conferenceRoomInput)); //Обновляем наименование Конференц-зала
        commandMap.put("6", new DeleteConferenceRoomCommand(conferenceRoomInput)); //Удаляем конференц зал
        commandMap.put("7", new AddWorkspaceCommand(conferenceRoomInput)); //Добавляем рабочие места
        commandMap.put("8", new BookWorkspaceCommand(conferenceRoomInput)); //Бронируем рабочие места
        commandMap.put("9", new ViewAvailableSlotsCommand(conferenceRoomInput)); //Просматриваем свободное время для записи на определенную дату
        commandMap.put("10", new BookConferenceRoomCommand(conferenceRoomInput)); //Бронируем целый Конференц-зал на определенные дату и время
        commandMap.put("11", new CancelWorkspaceBookingCommand(conferenceRoomInput)); //Отменяем бронирование рабочего места.
        commandMap.put("12", new CancelConferenceRoomBookingCommand(conferenceRoomInput)); //Отменяем бронирование Конференц-зала
        commandMap.put("13", new FilterBookingCommand(conferenceRoomInput)); //Фильтруем Конференц-зал по Date, User, Available Workspaces
        commandMap.put("0", new ExitCommand(output)); //Выход из консоли
    }
}
