package com.realtimeeventticketing.entities;

public abstract class User implements Runnable {
    private final int id;
    private final String name;
    protected volatile boolean running = true;

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

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public abstract void run();
}
