package io.coworking.controllers;

import io.coworking.entities.ConferenceRoom;
import io.coworking.entities.Workspace;
import io.coworking.managers.ConferenceRoomManager;
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
@WebMvcTest(ConferenceRoomController.class)
class ConferenceRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConferenceRoomManager conferenceRoomManager;

    @MockBean
    private WorkspaceManager workspaceManager;

    @Test
    void testGetAllConferenceRooms() throws Exception {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setId(1);
        conferenceRoom.setName("Conference Room 1");
        conferenceRoom.setCapacity(10);

        Workspace workspace = new Workspace();
        workspace.setId(1);
        workspace.setName("Workspace 1");
        workspace.setBookings(Collections.emptyList());

        conferenceRoom.setWorkspaces(Collections.singletonList(workspace));

        Mockito.when(conferenceRoomManager.findAllConferenceRooms()).thenReturn(Collections.singletonList(conferenceRoom));
        Mockito.when(conferenceRoomManager.findWorkspacesByConferenceRoomId(1)).thenReturn(Collections.singletonList(workspace));
        Mockito.when(workspaceManager.findBookingsByWorkspaceId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/conference_rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Conference Room 1"))
                .andExpect(jsonPath("$[0].workspaces[0].id").value(1))
                .andExpect(jsonPath("$[0].workspaces[0].name").value("Workspace 1"))
                .andExpect(jsonPath("$[0].workspaces[0].bookings").isEmpty());
    }

    @Test
    void testGetConferenceRoomById() throws Exception {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setId(1);
        conferenceRoom.setName("Conference Room 1");
        conferenceRoom.setCapacity(10);

        Workspace workspace = new Workspace();
        workspace.setId(1);
        workspace.setName("Workspace 1");
        workspace.setBookings(Collections.emptyList());

        conferenceRoom.setWorkspaces(Collections.singletonList(workspace));

        Mockito.when(conferenceRoomManager.findConferenceRoomById(1)).thenReturn(Optional.of(conferenceRoom));
        Mockito.when(conferenceRoomManager.findWorkspacesByConferenceRoomId(1)).thenReturn(Collections.singletonList(workspace));
        Mockito.when(workspaceManager.findBookingsByWorkspaceId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/conference_rooms/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Conference Room 1"))
                .andExpect(jsonPath("$.workspaces[0].id").value(1))
                .andExpect(jsonPath("$.workspaces[0].name").value("Workspace 1"))
                .andExpect(jsonPath("$.workspaces[0].bookings").isEmpty());
    }

    @Test
    void testCreateConferenceRoom() throws Exception {
        mockMvc.perform(post("/conference_rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Conference Room 1\", \"capacity\":10}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateConferenceRoomShouldReturnConflict() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Conference room conflict")).when(conferenceRoomManager).addConferenceRoom(any());

        mockMvc.perform(post("/conference_rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Conference Room 1\", \"capacity\":10}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Conference room conflict"));
    }

    @Test
    void testUpdateConferenceRoom() throws Exception {
        mockMvc.perform(put("/conference_rooms/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Conference Room\", \"capacity\":20}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateConferenceRoomShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Conference room not found")).when(conferenceRoomManager).updateConferenceRoom(any());

        mockMvc.perform(put("/conference_rooms/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Conference Room\", \"capacity\":20}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Conference room not found"));
    }

    @Test
    void testDeleteConferenceRoomShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(conferenceRoomManager).deleteConferenceRoom(anyInt());

        mockMvc.perform(delete("/conference_rooms/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteConferenceRoomShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Conference room not found")).when(conferenceRoomManager).deleteConferenceRoom(anyInt());

        mockMvc.perform(delete("/conference_rooms/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Conference room not found"));
    }
}