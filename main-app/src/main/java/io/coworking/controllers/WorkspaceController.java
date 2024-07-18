package io.coworking.controllers;

import io.coworking.dto.WorkspaceDTO;
import io.coworking.managers.WorkspaceManager;
import io.coworking.mapper.WorkspaceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.coworking.entities.Workspace;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workspaces")
@Tag(name = "Workspace Controller Management", description = "APIs for managing workspaces")
public class WorkspaceController {

    private final WorkspaceManager workspaceManager;

    @Autowired
    public WorkspaceController(WorkspaceManager workspaceManager) {
        this.workspaceManager = workspaceManager;
    }

    /**
     * Получаем все рабочие места.
     *
     * @return the workspace list
     */
    @GetMapping
    @Operation(summary = "Get all workspaces", description = "Retrieve a list of all workspaces")
    public ResponseEntity<List<WorkspaceDTO>> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceManager.findAllWorkspaces();
        for (Workspace workspace : workspaces) {
            workspace.setBookings(workspaceManager.findBookingsByWorkspaceId(workspace.getId()));
        }
        List<WorkspaceDTO> workspaceDTOS = WorkspaceMapper.INSTANCE.toDTOList(workspaces);
        return ResponseEntity.ok(workspaceDTOS);
    }

    /**
     * Получаем рабочее место по ID
     * @param id the workspace ID
     * @return the workspace
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get workspace by ID", description = "Retrieve workspace by ID")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable int id) {
        Optional<Workspace> workspace = workspaceManager.findWorkspaceById(id);
        workspace.ifPresent(value -> value.setBookings(workspaceManager.findBookingsByWorkspaceId(value.getId())));
        return workspace.map(value -> ResponseEntity.ok(WorkspaceMapper.INSTANCE.toDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Создаем рабочее место.
     *
     * @param workspaceDTO
     * @return the HTTP-status
     */
    @PostMapping
    @Operation(summary = "Create a new workspace", description = "Add a new workspace")
    public ResponseEntity<String> createWorkspace(@Valid @RequestBody WorkspaceDTO workspaceDTO) {
        try {
            Workspace workspace = WorkspaceMapper.INSTANCE.toEntity(workspaceDTO);
            workspaceManager.addWorkspace(workspace);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Обновляем рабочее место.
     *
     * @param id the workspace ID
     * @param workspaceDTO workspace
     * @return the HTTP-status
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update existing workspace", description = "Update workspace by ID")
    public ResponseEntity<String> updateWorkspace(@PathVariable int id, @Valid @RequestBody WorkspaceDTO workspaceDTO) {
        try {
            Workspace workspace = WorkspaceMapper.INSTANCE.toEntity(workspaceDTO);
            workspace.setId(id);
            workspaceManager.updateWorkspace(workspace);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Удаляем рабочее место.
     *
     * @param id the workspace ID
     * @return the HTTP-status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete workspace", description = "Remove a workspace by ID")
    public ResponseEntity<String> deleteWorkspace(@PathVariable int id) {
        try {
            workspaceManager.deleteWorkspace(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
