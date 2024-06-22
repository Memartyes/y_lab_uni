package ru.domain;

import ru.domain.adapters.in.ConsoleConferenceRoomInput;
import ru.domain.adapters.in.ConsoleUserInput;
import ru.domain.adapters.in.ConsoleInput;
import ru.domain.adapters.out.ConsoleOutput;
import ru.domain.usecases.ConferenceRoomManager;
import ru.domain.usecases.UserManager;

//"Menu: 1 - Register, 2 - Login, 3 - Create Conference Room, 4 - View Conference Rooms, 5 - Add Workspace
//       6 - Book Workspace, 7 - Update Conference Room, 8 - Delete Conference Room, 9 - View Available Slots, 10 - Book Conference Room, 11 - Exit

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

//        conferenceRoomManager.initializeConferenceRooms(); //Генерируем Конференц-залы по умолчанию.

        output.println("Coworking-Management System"); //Прописываем читабельное сопровождения пользователя по консоли.
        output.println("Please, follow the following instructions");

        label:
        while (true) {
            output.println(
                    "Choose an option:\n" +
                    "1. User Registration\n" +
                    "2. User Authorization\n" +
                    "3. Create Conference Room\n" +
                    "4. Read/View Conference Room\n" +
                    "5. Update Conference Room\n" +
                    "6. Delete Conference Room\n" +
                    "7. Add Workspace\n" +
                    "8. Book Workspace\n" +
                    "9. View Available Slots in Conference Room\n" +
                    "10. Book Conference Room\n" +
                    "0. Exit"
            );
            String choice = input.readLine(); //Считываем введенный пользователем номер.

            switch (choice) {
                case "1": //Регистрируем пользователя
                    userInput.handleRegisterUser();
                    break;
                case "2":  //Авторизируем пользователя
                    userInput.handleLoginUser();
                    break;
                case "3":  //Создаем Конференц-зал
                    conferenceRoomInput.handleCreateConferenceRoom();
                    break;
                case "4":  //Просматриваем доступные Конференц-залы и рабочие места
                    conferenceRoomInput.handleViewConferenceRooms();
                    break;
                case "5": //Обновляем конференц зал
                    conferenceRoomInput.handleUpdateConferenceRoom();
                    break;
                case "6": //Удаляем конференц зал
                    conferenceRoomInput.handleDeleteConferenceRoom();
                    break;
                case "7"://Добавляем рабочие места
                    conferenceRoomInput.handleAddWorkspace();
                    break;
                case "8": //Бронируем рабочие места
                    conferenceRoomInput.handleBookWorkspace();
                    break;
                case "9": //Просматриваем свободное время для записи на определенную дату
                    conferenceRoomInput.handleViewAvailableSlots();
                    break;
                case "10": //Бронируем целый Конференц-зал на определенные дату и время
                    conferenceRoomInput.handleBookConferenceRoom();
                    break;
                case "0":  //Выход из консоли
                    output.println("Goodbye!");
                    break label;
                default:  //Неверный input
                    output.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
