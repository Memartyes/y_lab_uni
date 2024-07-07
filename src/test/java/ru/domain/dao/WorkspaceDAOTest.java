//package ru.domain.dao;
//
//import liquibase.Contexts;
//import liquibase.Liquibase;
//import liquibase.database.Database;
//import liquibase.database.DatabaseFactory;
//import liquibase.database.jvm.JdbcConnection;
//import liquibase.resource.ClassLoaderResourceAccessor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.domain.dao.impl.WorkspaceDAOImpl;
//import ru.domain.entities.Workspace;
//import ru.domain.util.jdbc.DatabaseUtil;
//
//import java.sql.Connection;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@Testcontainers
//class WorkspaceDAOTest {
//    @Container
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass");
//
//    private WorkspaceDAO workspaceDAO;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        DatabaseUtil.setUrl(postgresContainer.getJdbcUrl());
//        DatabaseUtil.setUser(postgresContainer.getUsername());
//        DatabaseUtil.setPassword(postgresContainer.getPassword());
//
//        try (Connection connection = DatabaseUtil.getConnection()) {
//            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
//            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
//            liquibase.update(new Contexts());
//        }
//
//        workspaceDAO = new WorkspaceDAOImpl();
//    }
//
//    @Test
//    @DisplayName("Add a new workspace")
//    void testAddWorkspace() {
//        Workspace workspace = new Workspace("Workspace1");
//        workspaceDAO.addWorkspace(workspace);
//
//        Optional<Workspace> retrievedWorkspace = workspaceDAO.findWorkspaceById(workspace.getId());
//        assertTrue(retrievedWorkspace.isPresent());
//        assertThat(retrievedWorkspace.get().getName()).isEqualTo("Workspace1");
//    }
//
//    @Test
//    @DisplayName("Find a workspace by ID")
//    void testFindWorkspaceById() {
//        Workspace workspace = new Workspace("Workspace2");
//        workspaceDAO.addWorkspace(workspace);
//
//        Optional<Workspace> retrievedWorkspace = workspaceDAO.findWorkspaceById(workspace.getId());
//        assertTrue(retrievedWorkspace.isPresent());
//        assertThat(retrievedWorkspace.get().getName()).isEqualTo("Workspace2");
//    }
//
//    @Test
//    @DisplayName("Find all workspaces")
//    void testFindAllWorkspaces() {
//        workspaceDAO.addWorkspace(new Workspace("Workspace3"));
//        workspaceDAO.addWorkspace(new Workspace("Workspace4"));
//
//        List<Workspace> workspaces = workspaceDAO.findAllWorkspaces();
//        assertThat(workspaces).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("Update a workspace")
//    void testUpdateWorkspace() {
//        Workspace workspace = new Workspace("Workspace5");
//        workspaceDAO.addWorkspace(workspace);
//
//        workspace.setName("UpdatedWorkspace");
//        workspaceDAO.updateWorkspace(workspace);
//
//        Optional<Workspace> updatedWorkspace = workspaceDAO.findWorkspaceById(workspace.getId());
//        assertTrue(updatedWorkspace.isPresent());
//        assertThat(updatedWorkspace.get().getName()).isEqualTo("UpdatedWorkspace");
//    }
//
//    @Test
//    @DisplayName("Delete a workspace")
//    void testDeleteWorkspace() {
//        Workspace workspace = new Workspace("Workspace6");
//        workspaceDAO.addWorkspace(workspace);
//
//        workspaceDAO.deleteWorkspace(workspace.getId());
//
//        Optional<Workspace> deletedWorkspace = workspaceDAO.findWorkspaceById(workspace.getId());
//        assertThat(deletedWorkspace).isEmpty();
//    }
//}