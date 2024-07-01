package ru.domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import ru.domain.commands.Command;
import ru.domain.io.in.UserInput;
import ru.domain.menu.MenuCommandExecutor;
import ru.domain.menu.MenuDisplay;
import ru.domain.menu.MenuHandler;

import static org.mockito.Mockito.*;

class MenuHandlerTest {
    private UserInput userInput;
    private MenuCommandExecutor commandExecutor;
    private MenuDisplay menuDisplay;
    private MenuHandler menuHandler;

    @BeforeEach
    public void setUp() {
        userInput = Mockito.mock(UserInput.class);
        commandExecutor = Mockito.mock(MenuCommandExecutor.class);
        menuDisplay = Mockito.mock(MenuDisplay.class);
        menuHandler = new MenuHandler(userInput, commandExecutor, menuDisplay);
    }

    @Test
    public void testShowMainMenu() {
        when(userInput.readLine()).thenReturn("1", "0");
        when(commandExecutor.executeCommand("1")).thenReturn(false);
        when(commandExecutor.executeCommand("0")).thenReturn(true);

        menuHandler.showMainMenu();

        InOrder inOrder = inOrder(menuDisplay, userInput, commandExecutor);
        inOrder.verify(menuDisplay).displayMenu();
        inOrder.verify(userInput).readLine();
        inOrder.verify(commandExecutor).executeCommand("1");
        inOrder.verify(menuDisplay).displayMenu();
        inOrder.verify(userInput).readLine();
        inOrder.verify(commandExecutor).executeCommand("0");
    }

    @Test
    public void testInvalidOption() {
        when(userInput.readLine()).thenReturn("invalid", "0");
        when(commandExecutor.executeCommand("invalid")).thenReturn(false);
        when(commandExecutor.executeCommand("0")).thenReturn(true);

        menuHandler.showMainMenu();

        InOrder inOrder = inOrder(menuDisplay, userInput, commandExecutor);
        inOrder.verify(menuDisplay).displayMenu();
        inOrder.verify(userInput).readLine();
        inOrder.verify(commandExecutor).executeCommand("invalid");
        inOrder.verify(menuDisplay).displayMenu();
        inOrder.verify(userInput).readLine();
        inOrder.verify(commandExecutor).executeCommand("0");
    }
}