package com.realtimeeventticketing.entities;

public abstract class User {
    private final int id;
    private final String name;

    protected User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
