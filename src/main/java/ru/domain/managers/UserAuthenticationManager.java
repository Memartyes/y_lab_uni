package ru.domain.managers;

import lombok.Getter;
import ru.domain.dao.UserDAO;
import ru.domain.entities.User;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/**
 * Определяем класс для аутентификации.
 */
@Getter
public class UserAuthenticationManager {
    private final UserDAO userDAO;

    public UserAuthenticationManager(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Проверяем User на логин, и возвращаем User если username найдено в памяти.
     *
     * @param userName the username
     * @param password the user password
     * @return the logged-in user
     */
    public Optional<User> loginUser(String userName, String password) {
        Optional<User> user = userDAO.findUserByName(userName);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}
