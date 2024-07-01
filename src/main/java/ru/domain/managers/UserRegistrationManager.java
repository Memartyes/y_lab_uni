package ru.domain.managers;

import ru.domain.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Определяем класс для регистрации пользователя в системе.
 */
public class UserRegistrationManager {
    private Map<String, User> users;

    public UserRegistrationManager() {
        this.users = new HashMap<>();
    }

    /**
     * Регистрируем нового пользователя.
     *
     * @param userName the username
     * @param password the user password
     */
    public void registerUser(String userName, String password) {
        if (users.containsKey(userName)) {
            throw new IllegalArgumentException("User with username " + userName + " already exists");
        }

        User newUser = new User(userName, password);
        users.put(userName, newUser);
    }

    /**
     * Получаем пользователя по имени.
     *
     * @param userName the username
     * @return the user
     */
    public Optional<User> getUser(String userName) {
        return Optional.ofNullable(users.get(userName));
    }
}
