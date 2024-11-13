package com.realtimeeventticketing.simulation;

import java.util.ArrayList;

public abstract class User implements Runnable {
    private final int id;
    private final String name;
    private int rate;
    private final ArrayList<Ticket> tickets;
    private volatile boolean running = true;

    private int totalTickets;

    protected User(int id, String name, int rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.tickets = new ArrayList<>();
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

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        this.totalTickets++;
    }

    @Override
    public abstract void run();
}
