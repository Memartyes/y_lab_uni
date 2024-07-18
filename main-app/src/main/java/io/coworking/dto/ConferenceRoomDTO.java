package io.coworking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * DTO для конференц-залов.
 */
@Data
public class ConferenceRoomDTO {

    private int id;

    @NotBlank(message = "Conference room name cannot be blank")
    private String name;

    @Min(value = 1, message = "Conference room capacity must be at least 1")
    private int capacity;

    private List<WorkspaceDTO> workspaces;
}
