package com.realtimeeventticketing.core.users;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract class representing a user in the ticketing system.
 */
public abstract class User implements Runnable {
    /**
     * Unique ID of the user.
     */
    private final int id;

    /**
     * Name of the user.
     */
    private final String name;

    /**
     * Atomic integer to keep track of the number of tickets the user has.
     */
    private final AtomicInteger numberOfTickets = new AtomicInteger(0);

    /**
     * Type of the user.
     */
    protected UserType type;

    /**
     * Rate at which the user operates.
     */
    private int rate;

    /**
     * Flag indicating whether the user is running.
     */
    private volatile boolean running = true;

    /**
     * Constructs a User with the specified ID, name, and rate.
     *
     * @param id the unique ID of the user
     * @param name the name of the user
     * @param rate the rate at which the user operates
     */
    protected User(int id, String name, int rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    /**
     * Gets the unique ID of the user.
     *
     * @return the unique ID of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Stops the user.
     */
    public void stop() {
        running = false;
    }

    /**
     * Checks if the user is running.
     *
     * @return true if the user is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets the rate at which the user operates.
     *
     * @return the rate at which the user operates
     */
    public int getRate() {
        return rate;
    }

    /**
     * Sets the rate at which the user operates.
     *
     * @param rate the new rate at which the user operates
     */
    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * Gets the type of the user.
     *
     * @return the type of the user
     */
    public UserType getType() {
        return type;
    }

    /**
     * Gets the number of tickets the user has.
     *
     * @return the number of tickets the user has
     */
    public int getNumberOfTickets() {
        return numberOfTickets.get();
    }

    /**
     * Increments the number of tickets the user has by one.
     */
    public void addTicket() {
        numberOfTickets.incrementAndGet();
    }
}