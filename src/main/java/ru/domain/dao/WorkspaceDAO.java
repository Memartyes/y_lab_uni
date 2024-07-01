package ru.domain.dao;

import ru.domain.entities.Workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceDAO {
    void createWorkspace(Workspace workspace);
    List<Workspace> findAllWorkspaces();
    Optional<Workspace> findWorkspaceById(int id);
    void updateWorkspace(Workspace workspace);
    void deleteWorkspace(int id);
}
