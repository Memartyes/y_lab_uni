package ru.domain;

import ru.domain.adapters.in.ConsoleConferenceRoomInput;
import ru.domain.adapters.in.ConsoleUserInput;
import ru.domain.adapters.in.ConsoleInput;
import ru.domain.adapters.out.ConsoleOutput;

import java.util.Scanner;

/**
 * Класс для обработки бизнес-логики запуска приложения
 */
public class MenuHandler {
    private final ConsoleUserInput userInput;
    private final ConsoleConferenceRoomInput conferenceRoomInput;

    public MenuHandler(ConsoleUserInput userInput, ConsoleConferenceRoomInput conferenceRoomInput) {
        this.userInput = userInput;
        this.conferenceRoomInput = conferenceRoomInput;
    }

    public void displayMenu() {
        ConsoleInput input = new ConsoleInput();
        ConsoleOutput output = new ConsoleOutput();

        output.println("Coworking-Management System"); //Прописываем читабельное сопровождения пользователя по консоли.
        output.println("Please, follow the following instructions");

        outer:
        while (true) {
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

            String choice = input.readLine();

            switch (choice) {
                case "1": //Регистрируем пользователя
                    userInput.handleRegisterUser();
                    break;
                case "2":  //Авторизируем пользователя
                    userInput.handleLoginUser();
                    break;
                case "3":  //Создаем Конференц-зал
                    conferenceRoomInput.createConferenceRoom();
                    break;
                case "4":  //Просматриваем доступные Конференц-залы и рабочие места
                    conferenceRoomInput.viewConferenceRooms();
                    break;
                case "5": //Обновляем конференц зал
                    conferenceRoomInput.updateConferenceRoom();
                    break;
                case "6": //Удаляем конференц зал
                    conferenceRoomInput.deleteConferenceRoom();
                    break;
                case "7"://Добавляем рабочие места
                    conferenceRoomInput.addWorkspace();
                    break;
                case "8": //Бронируем рабочие места
                    conferenceRoomInput.bookWorkspace();
                    break;
                case "9": //Просматриваем свободное время для записи на определенную дату
                    conferenceRoomInput.viewAvailableSlots();
                    break;
                case "10": //Бронируем целый Конференц-зал на определенные дату и время
                    conferenceRoomInput.bookConferenceRoom();
                    break;
                case "11":
                    conferenceRoomInput.cancelWorkspaceBooking();
                    break;
                case "12":
                    conferenceRoomInput.cancelConferenceRoomBooking();
                    break;
                case "13":
                    conferenceRoomInput.filterBooking();
                    break;
                case "0":  //Выход из консоли
                    output.println("Goodbye!");
                    break outer;
                default:  //Неверный input
                    output.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
