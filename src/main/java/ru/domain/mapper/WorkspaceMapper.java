package ru.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.domain.entities.Workspace;
import ru.domain.dto.WorkspaceDTO;

import java.util.List;

@Mapper
public interface WorkspaceMapper {
    WorkspaceMapper INSTANCE = Mappers.getMapper(WorkspaceMapper.class);

    WorkspaceDTO toDTO(Workspace workspace);

    Workspace toEntity(WorkspaceDTO workspaceDTO);

    List<WorkspaceDTO> toDTOList(List<Workspace> workspaces);

    List<Workspace> toEntitiesList(List<WorkspaceDTO> workspaceDTOS);
}
