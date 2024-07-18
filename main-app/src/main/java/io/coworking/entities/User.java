package io.coworking.entities;

import lombok.Data;

/**
 * Класс представляет пользователей.
 */

@Data
public class User {
    private int id;
    private String name;
    private String password;
    private String email;

    /**
     * Контсруктор для класса User.
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User() {}
}
