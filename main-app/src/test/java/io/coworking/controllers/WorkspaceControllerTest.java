package io.coworking.controllers;

import io.coworking.entities.Workspace;
import io.coworking.managers.WorkspaceManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WorkspaceController.class)
class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkspaceManager workspaceManager;

    @Test
    void testGetAllWorkspaces() throws Exception {
        Workspace workspace = new Workspace();
        workspace.setId(1);
        workspace.setName("Workspace 1");
        workspace.setBookings(Collections.emptyList());

        Mockito.when(workspaceManager.findAllWorkspaces()).thenReturn(Collections.singletonList(workspace));
        Mockito.when(workspaceManager.findBookingsByWorkspaceId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/workspaces")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Workspace 1"))
                .andExpect(jsonPath("$[0].bookings").isEmpty());
    }

    @Test
    void testGetWorkspaceById() throws Exception {
        Workspace workspace = new Workspace();
        workspace.setId(1);
        workspace.setName("Workspace 1");
        workspace.setBookings(Collections.emptyList());

        Mockito.when(workspaceManager.findWorkspaceById(1)).thenReturn(Optional.of(workspace));
        Mockito.when(workspaceManager.findBookingsByWorkspaceId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/workspaces/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Workspace 1"))
                .andExpect(jsonPath("$.bookings").isEmpty());
    }

    @Test
    void testCreateWorkspace() throws Exception {
        mockMvc.perform(post("/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Workspace 1\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateWorkspaceShouldReturnConflict() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Workspace conflict")).when(workspaceManager).addWorkspace(any());

        mockMvc.perform(post("/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Workspace 1\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Workspace conflict"));
    }

    @Test
    void testUpdateWorkspace() throws Exception {
        mockMvc.perform(put("/workspaces/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Workspace\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateWorkspaceShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Workspace not found")).when(workspaceManager).updateWorkspace(any());

        mockMvc.perform(put("/workspaces/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Workspace\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Workspace not found"));
    }

    @Test
    void testDeleteWorkspaceShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(workspaceManager).deleteWorkspace(anyInt());

        mockMvc.perform(delete("/workspaces/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteWorkspaceShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Workspace not found")).when(workspaceManager).deleteWorkspace(anyInt());

        mockMvc.perform(delete("/workspaces/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Workspace not found"));
    }
}