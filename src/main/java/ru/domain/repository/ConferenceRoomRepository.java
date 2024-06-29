package ru.domain.repository;

import ru.domain.entities.ConferenceRoom;
import ru.domain.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomRepository {

    /**
     * for testing process
     * @param args
     */
    public static void main(String[] args) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            ConferenceRoomRepository conferenceRoomRepository = new ConferenceRoomRepository();
            ConferenceRoom conferenceRoom = new ConferenceRoom();
            conferenceRoom.setName("Information Technology");
            conferenceRoom.setCapacity(8);

            conferenceRoomRepository.addConferenceRoom(conferenceRoom);
            System.out.println("addConferenceRoom: " + connection.isValid(10));

            conferenceRoomRepository.findAllConferenceRooms();
            System.out.println("finaAllConferenceRooms: " + connection.isValid(10));

            conferenceRoom.setCapacity(2);
            conferenceRoomRepository.updateConferenceRoom(conferenceRoom);
            System.out.println("updateConferenceRoom: " + connection.isValid(10));

            int conferenceRoomId = conferenceRoom.getId();
            conferenceRoomRepository.deleteConferenceRoom(conferenceRoomId);
            System.out.println("deleteConferenceRoom: " + connection.isValid(10));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Добавляем новый конференц-зал в database.
     *
     * @param conferenceRoom the conference room
     */
    public void addConferenceRoom(ConferenceRoom conferenceRoom) {
        String sql = "INSERT INTO coworking.\"conference_rooms-liquibase\" (name, capacity) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, conferenceRoom.getName());
            preparedStatement.setInt(2, conferenceRoom.getCapacity());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    conferenceRoom.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("Conference room added successfully: " + conferenceRoom.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in adding conference rooms; " + e.getMessage());
        }
    }

    /**
     * Получаем список всех Конференц-залов из базы данных.
     *
     * @return the conference room list
     */
    public List<ConferenceRoom> findAllConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = new ArrayList<>();
        String sql = "SELECT id, name, capacity FROM coworking.\"conference_rooms-liquibase\"";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ConferenceRoom room = new ConferenceRoom();
                room.setId(resultSet.getInt("id"));
                room.setName(resultSet.getString("name"));
                room.setCapacity(resultSet.getInt("capacity"));
                conferenceRooms.add(room);
            }

            System.out.println("Conferences room retrieved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in finding all conference rooms: " + e.getMessage());
        }
        return conferenceRooms;
    }

    /**
     * Обновляем информацию о конференц-зале в database.
     * @param conferenceRoom
     */
    public void updateConferenceRoom(ConferenceRoom conferenceRoom) {
        String sql = "UPDATE coworking.\"conference_rooms-liquibase\" SET name = ?, capacity = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, conferenceRoom.getName());
            preparedStatement.setInt(2, conferenceRoom.getCapacity());
            preparedStatement.setInt(3, conferenceRoom.getId());
            preparedStatement.executeUpdate();

            System.out.println("Conference room updated successfully: " + conferenceRoom.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in updating conference room: " + e.getMessage());
        }
    }

    /**
     * Удаляем конференц зал из базы данных по ID.
     *
     * @param id the conference room ID
     */
    public void deleteConferenceRoom(int id) {
        String sql = "DELETE FROM coworking.\"conference_rooms-liquibase\" WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            System.out.println("Conference room with ID: " + id + " has been deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in deleting conference room: " + e.getMessage());
        }
    }
}
