package ru.domain.managers;

import ru.domain.entities.User;

import java.util.Map;
import java.util.HashMap;

/**
 * Определяем класс для управления пользователями в системе, включая регистрацию и аутентификацию.
 */
public class UserManager {
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    /**
     * Регистрируем нового пользователя.
     *
     * @param userId the user ID
     * @param password the user password
     * @return the registered user
     */
    public void registerUser(String userId, String password) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("User with id " + userId + " already exists");
        }
        User newUser = new User(userId, password);
        users.put(userId, newUser);
    }

    /**
     * Проверяем User на логин, и возвращаем User если ID найдено в памяти.
     *
     * @param userId the user ID
     * @param password the user password
     * @return the logged-in user
     */
    public boolean loginUser(String userId, String password) {
        User user = users.get(userId);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    /**
     * Возвращаем карту пользователей
     * @return Map of users
     */
    public Map<String, User> getUsers() {
        return users;
    }
}
