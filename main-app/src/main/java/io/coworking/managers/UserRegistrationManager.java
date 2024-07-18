package io.coworking.managers;

import io.coworking.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.coworking.dao.UserDAO;

import java.util.List;
import java.util.Optional;

/**
 * Определяем класс для регистрации пользователя в системе.
 */
@Service
public class UserRegistrationManager {
    private final UserDAO userDAO;

    @Autowired
    public UserRegistrationManager(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Регистрируем нового пользователя.
     *
     * @param userName the username
     * @param email the user email
     * @param password the user password
     */
    public void registerUser(String userName, String email, String password) {
        if (userDAO.findUserByName(userName).isPresent()) {
            throw new IllegalArgumentException("User with username " + userName + " already exists");
        }
        if (userDAO.findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }

        User newUser = new User(userName, email, password);
        userDAO.addUser(newUser);
    }

    /**
     * Получаем пользователя по имени.
     *
     * @param userName the username
     * @return the user
     */
    public Optional<User> getUser(String userName) {
        return userDAO.findUserByName(userName);
    }

    public Optional<User> getUserById(int id) {
        return userDAO.findUserById(id);
    }

    /**
     * Получаем всех пользователей.
     * @return the list of users
     */
    public List<User> getAllUsers() {
        return userDAO.findAllUsers();
    }

    /**
     * Удаляем пользователя по его идентификатору.
     *
     * @param userId the user ID
     */
    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);
    }

    /**
     * Обновляем информацию о пользователе.
     *
     * @param user the updated user object to perform update in database
     */
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }
}
