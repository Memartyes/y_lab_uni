package ru.domain.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.domain.entities.Workspace;
import ru.domain.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class WorkspaceRepositoryTest {
    private final static DockerImageName postgres= DockerImageName.parse("postgres:latest");

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(postgres)
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("password")
            .withExposedPorts(5433, 5432)
            .withInitScript("db/changelog/changeset/init_workspace.sql");

    private WorkspaceRepository workspaceRepository;

    @BeforeEach
    void setUp() {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();
        DatabaseUtil.setUrl(jdbcUrl);
        DatabaseUtil.setUser(username);
        DatabaseUtil.setPassword(password);
        workspaceRepository = new WorkspaceRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE coworking.\"workspaces-liquibase\";");
        }
    }

    @Test
    void testAddAndFindWorkspace() throws SQLException {
        Workspace workspace = new Workspace();
        workspace.setName("addWorkspace");
        workspace.setBookedBy("addAdmin");
        workspace.setBookingTime(LocalDateTime.now());
        workspaceRepository.addWorkspace(workspace);

        Workspace foundWorkspace = workspaceRepository.findWorkspaceByName("addWorkspace").orElseThrow();
        assertNotNull(foundWorkspace);
        assertEquals("addWorkspace", foundWorkspace.getName());
        assertEquals("addAdmin", foundWorkspace.getBookedBy());
    }

    @Test
    void testFindAllWorkspaces() throws SQLException {
        Workspace workspace1 = new Workspace();
        workspace1.setName("addWorkspace1");
        workspace1.setBookedBy("addAdmin1");
        workspace1.setBookingTime(LocalDateTime.now());
        Workspace workspace2 = new Workspace();
        workspace2.setName("addWorkspace2");
        workspace2.setBookedBy("addAdmin2");
        workspace2.setBookingTime(LocalDateTime.now());

        workspaceRepository.addWorkspace(workspace1);
        workspaceRepository.addWorkspace(workspace2);
        List<Workspace> workspaces = workspaceRepository.findAllWorkspaces();
        assertTrue(workspaces.size() >= 2);
    }

    @Test
    void testUpdateWorkspace() throws SQLException {
        Workspace workspace = new Workspace();
        workspace.setName("addWorkspace");
        workspace.setBookedBy("addAdmin");
        workspace.setBookingTime(LocalDateTime.now());
        workspaceRepository.addWorkspace(workspace);
        workspace.setBookedBy("addAdmin2");
        workspaceRepository.updateWorkspace(workspace);

        Workspace updatedWorkspace = workspaceRepository.findWorkspaceByName("addWorkspace").orElseThrow();
        assertEquals("addAdmin2", updatedWorkspace.getBookedBy());
    }

    @Test
    void testDeleteWorkspace() throws SQLException {
        Workspace workspace = new Workspace();
        workspace.setName("addWorkspace");
        workspace.setBookedBy("addAdmin");
        workspace.setBookingTime(LocalDateTime.now());
        workspaceRepository.addWorkspace(workspace);
        workspaceRepository.deleteWorkspace(workspace.getId());
        assertTrue(workspaceRepository.findWorkspaceByName("addWorkspace").isEmpty());
    }
}