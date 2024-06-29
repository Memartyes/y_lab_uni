package ru.domain.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Определяем класс для создания обьектов User.
 */

public class User {
    @Setter
    @Getter
    private int id;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String password;
    @Getter
    @Setter
    private String email;

    /**
     * Контсруктор для класса User.
     */
    public User() {} // Дефолтный конструктор для работы с database

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
