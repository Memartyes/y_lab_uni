package ru.domain.io;

/**
 * Класс для обработки ввода информации о конференц-зале через консоль
 */
public class ConsoleConferenceRoomInput {
    private final ConsoleInput input;
    private final ConsoleOutput output;

    public ConsoleConferenceRoomInput(ConsoleInput input, ConsoleOutput output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Считываем ввод ID Конференц-зала с консоли.
     *
     * @return the conference room ID
     */
    public String readRoomId() {
        output.println("Enter Conference Room ID:");
        return input.readLine();
    }

    /**
     * Считываем ввод ID рабочего места с консоли.
     *
     * @return the workspace ID
     */
    public String readWorkspaceId() {
        output.println("Enter Workspace ID:");
        return input.readLine();
    }

    /**
     * Считываем ввод ID пользователя с консоли.
     *
     * @return the user ID
     */
    public String readUserId() {
        output.println("Enter User ID:");
        return input.readLine();
    }

    /**
     * Считываем ввод даты и времени на бронирования с консоли по формату 'yyyy-MM-dd'.
     * @return the booking date string by format 'yyyy-MM-dd'
     */
    public String readBookingDate() {
        output.println("Enter Booking Date (yyyy-MM-dd):");
        return input.readLine();
    }

    /**
     * Считываем ввод времени на бронирования с консоли по формату 'HH:mm'.
     * @return the booking time string by format 'HH:mm'
     */
    public String readBookingTime() {
        output.println("Enter Booking Time (HH:mm):");
        return input.readLine();
    }

    /**
     * Считываем ввод даты и времени на бронирования с консоли по формату 'yyyy-MM-dd HH:mm'.
     * @return the booking date and time string by format 'yyyy-MM-dd HH:mm'
     */
    public String readBookingDateTime() {
        output.println("Enter Booking Date and Time (yyyy-MM-dd HH:mm):");
        return input.readLine();
    }

    /**
     * Считываем ввод пользователем выбора по фильтрации Конференц-залов.
     * (1 - Date, 2 - User, 3 - Available workspaces).
     * @return the user's input choice of filter type
     */
    public String readBookingFilterChoice() {
        output.println("Filter conference rooms by:\n'1' - Date\n'2' - User\n'3' - Available Workspaces\nChoose your option: ");
        return input.readLine();
    }
}
