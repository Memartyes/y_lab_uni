package io.coworking.managers;

import io.coworking.entities.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.coworking.dao.UserDAO;

import java.util.Optional;

/**
 * Определяем класс для аутентификации.
 */
@Getter
@Service
public class UserAuthenticationManager {
    private final UserDAO userDAO;

    @Autowired
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
