package ru.domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.domain.io.out.UserOutput;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MenuDisplayTest {
    private UserOutput userOutput;
    private MenuDisplay menuDisplay;

    @BeforeEach
    public void setUp() {
        userOutput = Mockito.mock(UserOutput.class);
        menuDisplay = new MenuDisplay(userOutput);
    }

    @Test
    public void testDisplayMenu() {
        menuDisplay.displayMenu();
        verify(userOutput, times(1)).println("Choose an option:");
        verify(userOutput, times(1)).println("1. User Registration");
        verify(userOutput, times(1)).println("2. User Authorization");
        verify(userOutput, times(1)).println("3. Create Conference Room");
        verify(userOutput, times(1)).println("4. Read/View Conference Room");
        verify(userOutput, times(1)).println("5. Update Conference Room");
        verify(userOutput, times(1)).println("6. Delete Conference Room");
        verify(userOutput, times(1)).println("7. Add Workspace");
        verify(userOutput, times(1)).println("8. Book Workspace");
        verify(userOutput, times(1)).println("9. View Available Slots in Conference Room");
        verify(userOutput, times(1)).println("10. Book Conference Room");
        verify(userOutput, times(1)).println("11. Cancel Workspace Booking");
        verify(userOutput, times(1)).println("12. Cancel Conference Room Booking");
        verify(userOutput, times(1)).println("13. Filter Bookings");
        verify(userOutput, times(1)).println("0. Exit");
    }
}