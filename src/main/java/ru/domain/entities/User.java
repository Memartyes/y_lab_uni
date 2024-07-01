package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс представляет пользователей.
 */

@Getter
@Setter
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
