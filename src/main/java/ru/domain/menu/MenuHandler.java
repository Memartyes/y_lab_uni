package ru.domain.menu;

import ru.domain.io.ConsoleUserInput;

/**
 * Класс обрабатыает ввод пользователя из консоли
 */
public class MenuHandler {
    private final ConsoleUserInput userInput;
    private final MenuCommandExecutor commandExecutor;
    private final MenuDisplay menuDisplay;

    /**
     * @param userInput the user's input output
     * @param commandExecutor the command executor
     * @param menuDisplay the menu display
     */
    public MenuHandler(ConsoleUserInput userInput, MenuCommandExecutor commandExecutor, MenuDisplay menuDisplay) {
        this.userInput = userInput;
        this.commandExecutor = commandExecutor;
        this.menuDisplay = menuDisplay;
    }

    public void showMainMenu() {
        boolean exit = false;
        while (!exit) {
            menuDisplay.displayMenu();
            String choice = userInput.readLine();
            exit = commandExecutor.executeCommand(choice);
        }
    }
}
