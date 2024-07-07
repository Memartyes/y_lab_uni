//package ru.domain.managers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.domain.entities.Workspace;
//import ru.domain.managers.WorkspaceManager;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//class WorkspaceManagerTest {
//    private WorkspaceManager workspaceManager;
//
//    @BeforeEach
//    public void setUp() {
//        workspaceManager = new WorkspaceManager();
//    }
//
//    @Test
//    public void testInitializeWorkspaces() {
//        int capacity = 8;
//        List<Workspace> workspaces = workspaceManager.initializeWorkspaces(capacity);
//
//        assertThat(workspaces).hasSize(capacity);
//        for (int i = 1; i <= capacity; i++) {
//            int iter = i;
//            assertThat(workspaces).anyMatch(workspace -> workspace.getName().equals(String.valueOf(iter)));
//        }
//    }
//
//    @Test
//    public void testGetWorkspace() {
//        Workspace workspace = new Workspace("Test Workspace");
//        workspaceManager.addWorkspace(workspace);
//
//        Optional<Workspace> foundWorkspace = workspaceManager.getWorkspace("Test Workspace");
//
//        assertThat(foundWorkspace).isPresent();
//        assertThat(foundWorkspace.get().getName()).isEqualTo("Test Workspace");
//    }
//
//    @Test
//    public void testGetWorkspaceNotFound() {
//        Optional<Workspace> foundWorkspace = workspaceManager.getWorkspace("Nonexistent Workspace");
//
//        assertThat(foundWorkspace).isNotPresent();
//    }
//
//    @Test
//    public void testAddWorkspace() {
//        Workspace workspace = new Workspace("Test Workspace");
//        workspaceManager.addWorkspace(workspace);
//
//        Optional<Workspace> foundWorkspace = workspaceManager.getWorkspace("Test Workspace");
//
//        assertThat(foundWorkspace).isPresent();
//        assertThat(foundWorkspace.get().getName()).isEqualTo("Test Workspace");
//    }
//
//    @Test
//    public void testAddWorkspaceAlreadyExists() {
//        Workspace workspace = new Workspace("Test Workspace");
//        workspaceManager.addWorkspace(workspace);
//
//        assertThatThrownBy(() -> workspaceManager.addWorkspace(workspace))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("already exists");
//    }
//
//    @Test
//    public void testCancelBookingWorkspace() {
//        Workspace workspace = new Workspace("Test Workspace");
//        workspace.book("Test User", LocalDateTime.now(), 1);
//        workspaceManager.addWorkspace(workspace);
//
//        workspaceManager.cancelBookingWorkspace("Test Workspace");
//
//        assertThat(workspace.isBooked()).isFalse();
//    }
//
//    @Test
//    public void testCancelBookingWorkspaceNotFound() {
//        assertThatThrownBy(() -> workspaceManager.cancelBookingWorkspace("Nonexistent Workspace"))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("not found");
//    }
//
//    @Test
//    public void testCancelBookingForAllWorkspaces() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspace1.book("Test User", LocalDateTime.now(), 1);
//        workspace2.book("Test User", LocalDateTime.now(), 1);
//        workspaceManager.addWorkspace(workspace1);
//        workspaceManager.addWorkspace(workspace2);
//
//        workspaceManager.cancelBookingForAllWorkspaces();
//
//        assertThat(workspace1.isBooked()).isFalse();
//        assertThat(workspace2.isBooked()).isFalse();
//    }
//
//    @Test
//    public void testGetAvailableWorkspaceCount() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspace1.book("Test User", LocalDateTime.now(), 1);
//        workspaceManager.addWorkspace(workspace1);
//        workspaceManager.addWorkspace(workspace2);
//
//        int availableCount = workspaceManager.getAvailableWorkspaceCount();
//
//        assertThat(availableCount).isEqualTo(1);
//    }
//
//    @Test
//    public void testHasAvailableWorkspaces() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspace1.book("Test User", LocalDateTime.now(), 1);
//        workspaceManager.addWorkspace(workspace1);
//        workspaceManager.addWorkspace(workspace2);
//
//        boolean hasAvailable = workspaceManager.hasAvailableWorkspaces();
//
//        assertThat(hasAvailable).isTrue();
//    }
//
//    @Test
//    public void testHasNoAvailableWorkspaces() {
//        Workspace workspace1 = new Workspace("Workspace 1");
//        Workspace workspace2 = new Workspace("Workspace 2");
//        workspace1.book("Test User", LocalDateTime.now(), 1);
//        workspace2.book("Test User", LocalDateTime.now(), 1);
//        workspaceManager.addWorkspace(workspace1);
//        workspaceManager.addWorkspace(workspace2);
//
//        boolean hasAvailable = workspaceManager.hasAvailableWorkspaces();
//
//        assertThat(hasAvailable).isFalse();
//    }
//}