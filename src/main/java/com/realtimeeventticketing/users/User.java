package com.realtimeeventticketing.users;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class User implements Runnable {
    private final int id;
    private final String name;
    private int rate;
    private final AtomicInteger numberOfTickets = new AtomicInteger(0);
    private volatile boolean running = true;
    protected UserType type;

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

    public UserType getType() {
        return type;
    }

    public int getNumberOfTickets() {
        return numberOfTickets.get();
    }

    public void addTicket() {
        numberOfTickets.incrementAndGet();
    }

    @Override
    public abstract void run();
}
