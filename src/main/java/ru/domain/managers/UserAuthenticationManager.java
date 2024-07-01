package ru.domain.managers;

import lombok.Getter;
import ru.domain.entities.User;

import java.util.Map;
import java.util.HashMap;

/**
 * Определяем класс для аутентификации.
 */
@Getter
public class UserAuthenticationManager {
    /**
     * -- GETTER --
     *  Возвращаем список (карту) пользователей
     */
    private Map<String, User> users;

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
    public boolean loginUser(String userName, String password) {
        User user = users.get(userName);
        return user != null && user.getPassword().equals(password);
    }
}
