package com.realtimeeventticketing.simulation;

public abstract class User implements Runnable {
    private final int id;
    private final String name;
    private int rate;
    private volatile boolean running = true;

    protected User(int id, String name, int rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
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

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public abstract void run();
}
