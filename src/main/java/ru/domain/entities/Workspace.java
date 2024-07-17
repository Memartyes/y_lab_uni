package ru.domain.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас представляет рабочие места.
 */
@Data
public class Workspace {
    private int id;
    private String name;
    private List<Booking> bookings;

    /**
     * Конструктор для создания нового рабочего места.
     * @param name the workspace name;
     */
    public Workspace(String name) {
        this.name = name;
        this.bookings = new ArrayList<>();
    }

    public Workspace() {}

    /**
     * Проверяет, доступно ли рабочее место для бронирования.
     *
     * @return true if the workspace is available, false otherwise
     */
    public boolean isAvailable() {
        return bookings.isEmpty();
    }
}
