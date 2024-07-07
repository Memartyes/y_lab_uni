package ru.domain.dao;

import ru.domain.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для дальнейшей имплементации User Data Access Object
 */
public interface UserDAO {

    /**
     * Добавляем нового пользователя в базу данных.
     *
     * @param user the user object to create and add
     */
    void addUser(User user);

    /**
     * Находим пользователя по его ID
     *
     * @param id the user's ID
     * @return the Optional with User if the user found, empty Optional otherwise
     */
    Optional<User> findUserById(int id);

    /**
     * Находим пользователя по его имени.
     *
     * @param name the username
     * @return the Optional with User if the user found, empty Optional otherwise
     */
    Optional<User> findUserByName(String name);

    /**
     * Находим пользователя по его email.
     *
     * @param email the user's email
     * @return the Optional with User if the user found, empty Optional otherwise
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Возвращаем список всех пользователей.
     *
     * @return List of users
     */
    List<User> findAllUsers();

    /**
     * Обновляем информацию о пользователе в базе данных.
     *
     * @param user the updated user object to perform update in database
     */
    void updateUser(User user);

    /**
     * Удаляем пользователя по его идентификатору.
     *
     * @param id the user's id
     */
    void deleteUser(int id);
}
