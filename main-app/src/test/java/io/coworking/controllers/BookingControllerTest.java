package io.coworking.controllers;

import io.coworking.entities.Booking;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkspaceManager workspaceManager;

    @Test
    void testGetAllBookings() throws Exception {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setWorkspaceId(1);
        booking.setBookedBy("Benedict Spinoza");
        booking.setBookingTime(LocalDateTime.now());
        booking.setBookingDurationHours(2);

        Mockito.when(workspaceManager.findAllBookings()).thenReturn(Collections.singletonList(booking));

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].workspaceId").value(1))
                .andExpect(jsonPath("$[0].bookedBy").value("Benedict Spinoza"))
                .andExpect(jsonPath("$[0].bookingTime").isNotEmpty())
                .andExpect(jsonPath("$[0].bookingDurationHours").value(2));
    }

    @Test
    void testGetBookingById() throws Exception {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setWorkspaceId(1);
        booking.setBookedBy("Benedict Spinoza");
        booking.setBookingTime(LocalDateTime.now());
        booking.setBookingDurationHours(2);

        Mockito.when(workspaceManager.findBookingById(1)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.workspaceId").value(1))
                .andExpect(jsonPath("$.bookedBy").value("Benedict Spinoza"))
                .andExpect(jsonPath("$.bookingTime").isNotEmpty())
                .andExpect(jsonPath("$.bookingDurationHours").value(2));
    }

    @Test
    void testGetBookingByIdShouldReturnNotFound() throws Exception {
        Mockito.when(workspaceManager.findBookingById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/bookings/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBookingShouldReturnNoContent() throws Exception {
        doNothing().when(workspaceManager).cancelBooking(anyInt());

        mockMvc.perform(delete("/bookings/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBookingShouldReturnNotFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Booking not found")).when(workspaceManager).cancelBooking(anyInt());

        mockMvc.perform(delete("/bookings/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Booking not found"));
    }
}