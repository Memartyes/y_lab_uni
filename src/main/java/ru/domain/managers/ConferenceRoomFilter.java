package ru.domain.managers;

import ru.domain.entities.ConferenceRoom;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для фильтрации Конференц-залов
 */
public class ConferenceRoomFilter {
    private Map<String, ConferenceRoom> conferenceRooms;

    public ConferenceRoomFilter(Map<String, ConferenceRoom> conferenceRooms) {
        this.conferenceRooms = conferenceRooms;
    }

    /**
     * Метод фильтрует бронирования Конференц-залов по дате.
     *
     * @param date the date
     * @return the list of booked conference rooms which have books on the current date
     */
    public List<String> filterByDate(LocalDate date) {
        return conferenceRooms.values().stream()
                .filter(conferenceRoom -> conferenceRoom.hasBookingOnDate(date))
                .map(conferenceRoom -> conferenceRoom.getName() + " has booking on " + date)
                .collect(Collectors.toList());
    }

    /**
     * Метод фильтрует бронирования Конференц-залов по пользователю.
     *
     * @param userName the username
     * @return the list of conference rooms which have books by user
     */
    public List<String> filterByUser(String userName) {
        return conferenceRooms.values().stream()
                .filter(conferenceRoom -> conferenceRoom.hasBookingByUser(userName))
                .map(conferenceRoom -> conferenceRoom.getName() + " has booking by user " + userName)
                .collect(Collectors.toList());
    }

    /**
     * Метод фильтрует Конференц-залы с доступнымм рабочими местами для бронирования.
     * @return the list of conference rooms with available workspaces
     */
    public List<String> filterByAvailableWorkspaces() {
        return conferenceRooms.values().stream()
                .filter(ConferenceRoom::hasAvailableWorkspaces)
                .map(conferenceRoom -> conferenceRoom.getName() + " has avaialble workspaces")
                .collect(Collectors.toList());
    }
}
