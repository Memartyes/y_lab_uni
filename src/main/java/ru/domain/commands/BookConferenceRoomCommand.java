package ru.domain.commands;

import ru.domain.io.ConsoleConferenceRoomInput;

public class BookConferenceRoomCommand implements Command {
    private ConsoleConferenceRoomInput conferenceRoomInput;

    public BookConferenceRoomCommand(ConsoleConferenceRoomInput conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.bookConferenceRoom();
    }
}
