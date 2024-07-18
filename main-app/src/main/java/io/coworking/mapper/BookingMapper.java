package io.coworking.mapper;

import io.coworking.dto.BookingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import io.coworking.entities.Booking;

import java.util.List;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDTO toDTO(Booking booking);

    Booking toEntity(BookingDTO bookingDTO);

    List<BookingDTO> toDTOList(List<Booking> bookings);

    List<Booking> toEntitiesList(List<BookingDTO> bookingDTOS);
}
