package ru.domain.entities;

import lombok.Getter;

/**
 * Определяем класс для создания обьектов User.
 */
@Getter
public class User {
    /**
     * Обьявляем три instance переменные класса User.
     * -- GETTER --
     *  Возвращаем пользовательский ID.
     *
     * @return the user ID

     */
    private String id;
    /**
     * -- GETTER --
     *  Возвращаем пользовательский password.
     *
     * @return the user's password
     */
    private String password;

    /**
     * Контсруктор для класса User.
     */
    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

}
