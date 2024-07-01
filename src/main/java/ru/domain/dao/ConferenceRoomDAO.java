package ru.domain.dao;

import ru.domain.entities.ConferenceRoom;

import java.util.List;
import java.util.Optional;

public interface ConferenceRoomDAO {
    void createConferenceRoom(ConferenceRoom conferenceRoom);
    List<ConferenceRoom> findAllConferenceRooms();
    Optional<ConferenceRoom> findConferenceRoomById(int id);
    void updateConferenceRoom(ConferenceRoom conferenceRoom);
    void deleteConferenceRoom(int id);
}
