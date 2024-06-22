package ru.domain.entities;

/**
 * Определяем класс для создания обьектов User.
 */
public class User {
    /**
     * Обьявляем три instance переменные класса User.
     */
    private String id;
    private String password;

    /**
     * Контсруктор для класса User.
     */
    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * Возвращаем пользовательский ID.
     *
     * @return the user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Возвращаем пользовательский password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }
}
