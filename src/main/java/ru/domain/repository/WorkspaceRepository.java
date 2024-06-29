package ru.domain.repository;

import ru.domain.entities.Workspace;
import ru.domain.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления рабочими местами.
 */
public class WorkspaceRepository {

    /**
     * for testing process
     * @param args
     */
    public static void main(String[] args) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            WorkspaceRepository workspaceRepository = new WorkspaceRepository();
            Workspace workspace = new Workspace();
            workspace.setName("Observer");
            workspace.setBookedBy("Joey");
            workspace.setBookingTime(LocalDateTime.now());

            workspaceRepository.addWorkspace(workspace);
            System.out.println("addWorkspace: " + connection.isValid(10));

            workspaceRepository.findAllWorkspaces();
            System.out.println("findAllWorkspaces: " + connection.isValid(10));

            workspace.setName("Content creator");
            workspaceRepository.updateWorkspace(workspace);
            System.out.println("updateWorkspace: " + connection.isValid(10));

            int workspaceId = workspace.getId();
            workspaceRepository.deleteWorkspace(workspaceId);
            System.out.println("deleteWorkspace: " + connection.isValid(10));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    /**
     * Добавляем новое рабочее место в database.
     *
     * @param workspace the workspace
     */
    public void addWorkspace(Workspace workspace) {
        String sql = "INSERT INTO \"workspaces-liquibase\" (name, \"bookedBy\", \"bookingTime\") VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setString(2, workspace.getBookedBy() != null ? workspace.getBookedBy() : "Available for book");
            preparedStatement.setTime(3, Time.valueOf(workspace.getBookingTime().toLocalTime()));
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    workspace.setId(generatedKeys.getInt(1));
                }
            }

            System.out.println("Workspace added successfully: " + workspace.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in adding workspaces; " + e.getMessage());
        }
    }

    /**
     * Получаем список всех рабочих мест из database.
     *
     * @return the workspaces list
     */
    public List<Workspace> findAllWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT id, name, \"bookedBy\", \"bookingTime\" FROM \"workspaces-liquibase\"";

        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Workspace workspace = new Workspace();
                    workspace.setId(resultSet.getInt("id"));
                }

            System.out.println("Workspaces retrieved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in retrieving workspaces: " + e.getMessage());
        }
        return workspaces;
    }

    /**
     * Обновляет информацию о рабочем месте в database.
     *
     * @param workspace the updated values in workspace
     */
    public void updateWorkspace(Workspace workspace) {
        String sql = "UPDATE \"workspaces-liquibase\" SET name = ?, \"bookedBy\" = ?, \"bookingTime\" = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setString(2, workspace.getBookedBy());
            preparedStatement.setTime(3, Time.valueOf(workspace.getBookingTime().toLocalTime()));
            preparedStatement.setInt(4, workspace.getId());
            preparedStatement.executeUpdate();

            System.out.println("Workspace " + workspace.getName() + " updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in updating workspace: " + e.getMessage());
        }
    }

    /**
     * Удаляем рабочее место из database по его ID.
     *
     * @param id the workspace ID
     */
    public void deleteWorkspace(int id) {
        String sql = "DELETE FROM \"workspaces-liquibase\" WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            System.out.println("Workspace with ID: " + id + " has been deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException in deleting workspace: " + e.getMessage());
        }
    }
}
