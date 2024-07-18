package io.coworking.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Определяем класс Конференц-зала который содержит рабочие места (Workspace).
 */
@Data
public class ConferenceRoom {
    private int id;
    private String name;
    private int capacity;
    private List<Workspace> workspaces;

    public ConferenceRoom() {
        this.workspaces = new ArrayList<>();
    }

    public ConferenceRoom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.workspaces = new ArrayList<>();
    }
}
