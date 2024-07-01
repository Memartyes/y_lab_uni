package ru.domain.commands;

import ru.domain.handlers.ConferenceRoomHandler;

public class BookConferenceRoomCommand implements Command {
    private ConferenceRoomHandler conferenceRoomInput;

    public BookConferenceRoomCommand(ConferenceRoomHandler conferenceRoomInput) {
        this.conferenceRoomInput = conferenceRoomInput;
    }

    @Override
    public void execute() {
        conferenceRoomInput.handleBookConferenceRoom();
    }
}
