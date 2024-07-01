package ru.domain.menu;

import ru.domain.io.out.ConsoleUserOutput;
import ru.domain.io.out.UserOutput;

/**
 * Класс отвечающий за вывод в консоль информации меню.
 */
public class MenuDisplay {
    private final UserOutput output;

    public MenuDisplay(UserOutput output) {
        this.output = output;
    }

    /**
     * Отображаем менюв консоли.
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
}
