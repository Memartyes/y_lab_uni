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
    private Map<String, User> users;
//    private final UserDAO userDAO;

    public UserAuthenticationManager() {
        this.users = new HashMap<>();
    }

    /**
     * Проверяем User на логин, и возвращаем User если username найдено в памяти.
     *
     * @param userName the username
     * @param password the user password
     * @return the logged-in user
     */
    public Optional<User> loginUser(String userName, String password) {
        User user = users.get(userName);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
