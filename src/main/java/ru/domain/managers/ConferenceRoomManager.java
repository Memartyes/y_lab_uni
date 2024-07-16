package ru.domain.managers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.domain.dao.ConferenceRoomDAO;
import ru.domain.dao.WorkspaceDAO;
import ru.domain.entities.ConferenceRoom;
import ru.domain.entities.Workspace;
import ru.domain.util.WorkingDateTimeUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;

/**
 * Определим класс для управления Конференц-залами.
 */
@Service
@Getter
@Setter
public class ConferenceRoomManager {
    private final ConferenceRoomDAO conferenceRoomDAO;
    private final WorkspaceDAO workspaceDAO;

    @Autowired
    public ConferenceRoomManager(ConferenceRoomDAO conferenceRoomDAO, WorkspaceDAO workspaceDAO) {
        this.conferenceRoomDAO = conferenceRoomDAO;
        this.workspaceDAO = workspaceDAO;
    }

    /**
     * Добавляем новый конференц-зал.
     *
     * @param conferenceRoom the conference room to add
     */
    public void addConferenceRoom(ConferenceRoom conferenceRoom) {
        conferenceRoomDAO.addConferenceRoom(conferenceRoom);
    }

    /**
     * Находим конференц-зал по его ID.
     *
     * @param id the conference room ID
     * @return the conference room if found, empty Optional otherwise
     */
    public Optional<ConferenceRoom> findConferenceRoomById(int id) {
        return conferenceRoomDAO.findConferenceRoomById(id);
    }

    /**
     * Находим все конференц-залы.
     *
     * @return the list of all conference rooms
     */
    public List<ConferenceRoom> findAllConferenceRooms() {
        return conferenceRoomDAO.findAllConferenceRooms();
    }

    /**
     * Обновляем конференц-зал.
     *
     * @param conferenceRoom the conference room to update
     */
    public void updateConferenceRoom(ConferenceRoom conferenceRoom) {
        conferenceRoomDAO.updateConferenceRoom(conferenceRoom);
    }

    /**
     * Удаляем конференц-зал по его ID.
     *
     * @param id the conference room ID
     */
    public void deleteConferenceRoom(int id) {
        conferenceRoomDAO.deleteConferenceRoom(id);
    }

    /**
     * Добавляем рабочее место в конференц-зал.
     *
     * @param conferenceRoomId the conference room ID
     * @param workspace the workspace to add
     */
    public void addWorkspaceToConferenceRoom(int conferenceRoomId, Workspace workspace) {
        conferenceRoomDAO.addWorkspaceToConferenceRoom(conferenceRoomId, workspace);
    }

    /**
     * Находим рабочие места по ID конференц-зала.
     *
     * @param conferenceRoomId the conference room ID
     * @return the list of workspaces by conference room ID
     */
    public List<Workspace> findWorkspacesByConferenceRoomId(int conferenceRoomId) {
        return conferenceRoomDAO.findWorkspacesByConferenceRoomId(conferenceRoomId);
    }

    /**
     * Находим все рабочие места.
     *
     * @return the list of all workspaces
     */
    public List<Workspace> findAllWorkspaces() {
        return workspaceDAO.findAllWorkspaces();
    }

    /**
     * Находим конференц-залы с бронированиями на указанную дату.
     *
     * @param date the date
     * @return the list of conference rooms
     */
    public List<ConferenceRoom> findRoomsByDate(LocalDate date) {
        return conferenceRoomDAO.findRoomsByDate(date);
    }

    /**
     * Находим конференц-залы с бронированиями пользователем.
     *
     * @param userName the user's name
     * @return the list of conference rooms
     */
    public List<ConferenceRoom> findRoomsByUser(String userName) {
        return conferenceRoomDAO.findRoomsByUser(userName);
    }

    /**
     * Находим конференц-залы с доступными рабочими местами.
     *
     * @return the list of conference rooms
     */
    public List<ConferenceRoom> findRoomsWithAvailableWorkspaces() {
        return conferenceRoomDAO.findRoomsWithAvailableWorkspaces();
    }

    /**
     * Проверяем доступность конференц-зала на определенную дату.
     *
     * @param conferenceRoomId the conference room ID
     * @param date the booking date
     * @return true if the conference room is available on the specified date, false otherwise
     */
    public boolean isConferenceRoomAvailable(int conferenceRoomId, LocalDateTime date) {
        return WorkingDateTimeUtil.isWithinWorkingHours(date) && WorkingDateTimeUtil.isWorkingDay(date) && conferenceRoomDAO.findRoomsByDate(date.toLocalDate()).stream()
                .noneMatch(room -> room.getId() == conferenceRoomId);
    }
}
