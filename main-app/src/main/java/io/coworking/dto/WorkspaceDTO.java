package io.coworking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * DTO для рабочих мест.
 */
@Data
public class WorkspaceDTO {

    private int id;

    @NotBlank(message = "Workspace name cannot be blank")
    @Size(min = 3, max = 30, message = "Password must be between 3 and 30 characters")
    private String name;

    private List<BookingDTO> bookings;
}
