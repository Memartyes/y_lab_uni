package ru.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.domain.entities.User;
import ru.domain.dto.UserDTO;

import java.util.List;

/**
 * Mapper для User и UserDTO.
 */
@Mapper
public interface UserMapper {
    /**
     * Запоминаем инстенс объекта.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Маппим User entity (сущность пользователя) в UserDTO (User Data Transfer Object).
     *
     * @param user User
     * @return the UserDTO from User
     */
    UserDTO toDTO(User user);

    /**
     * Маппим UserDTO в User сущности.
     *
     * @param userDTO UserDTO
     * @return the User entity from UserDTO
     */
    User toEntity(UserDTO userDTO);

    /**
     * Маппим список пользователей (List<User>) в список UserDTO List<UserDTO>
     *
     * @param users the List of User
     * @return the List of UserDTO
     */
    List<UserDTO> toDTOList(List<User> users);

    /**
     * Маппим список UserDTO в список User
     *
     * @param usersDTO the List of UserDTO
     * @return the List of User
     */
    List<User> toEntitiesList(List<UserDTO> usersDTO);
}
