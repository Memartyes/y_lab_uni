package io.coworking.mapper;

import io.coworking.dto.ConferenceRoomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import io.coworking.entities.ConferenceRoom;

import java.util.List;

@Mapper
public interface ConferenceRoomMapper {
    ConferenceRoomMapper INSTANCE = Mappers.getMapper(ConferenceRoomMapper.class);

    ConferenceRoomDTO toDTO(ConferenceRoom conferenceRoom);

    ConferenceRoom toEntity(ConferenceRoomDTO conferenceRoomDTO);

    List<ConferenceRoomDTO> toDTOList(List<ConferenceRoom> conferenceRooms);

    List<ConferenceRoom> toEntitiesList(List<ConferenceRoomDTO> conferenceRoomDTOS);
}
