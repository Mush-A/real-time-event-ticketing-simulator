package com.realtimeeventticketing.core.simulation;

import java.util.UUID;

/**
 * Configuration class for the simulation.
 */
public class SimulationConfig {
    private final UUID id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketsCapacity;
    private int numVendors;
    private int numCustomers;

    /**
     * Constructs a SimulationConfig with a unique ID.
     */
    public SimulationConfig() {
        this.id = UUID.randomUUID();
    }

    /**
     * Gets the unique ID of the simulation configuration.
     *
     * @return the unique ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the total number of tickets.
     *
     * @return the total number of tickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Sets the total number of tickets.
     *
     * @param totalTickets the total number of tickets
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    /**
     * Gets the ticket release rate in milliseconds.
     *
     * @return the ticket release rate in milliseconds
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Sets the ticket release rate in milliseconds.
     *
     * @param ticketReleaseRate the ticket release rate in milliseconds
     */
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Gets the customer retrieval rate in milliseconds.
     *
     * @return the customer retrieval rate in milliseconds
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Sets the customer retrieval rate in milliseconds.
     *
     * @param customerRetrievalRate the customer retrieval rate in milliseconds
     */
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    /**
     * Gets the maximum tickets capacity.
     *
     * @return the maximum tickets capacity
     */
    public int getMaxTicketsCapacity() {
        return maxTicketsCapacity;
    }

    /**
     * Sets the maximum tickets capacity.
     *
     * @param maxTicketsCapacity the maximum tickets capacity
     */
    public void setMaxTicketsCapacity(int maxTicketsCapacity) {
        this.maxTicketsCapacity = maxTicketsCapacity;
    }

    /**
     * Gets the number of vendors.
     *
     * @return the number of vendors
     */
    public int getNumVendors() {
        return numVendors;
    }

    /**
     * Sets the number of vendors.
     *
     * @param numVendors the number of vendors
     */
    public void setNumVendors(int numVendors) {
        this.numVendors = numVendors;
    }

    /**
     * Gets the number of customers.
     *
     * @return the number of customers
     */
    public int getNumCustomers() {
        return numCustomers;
    }

    /**
     * Sets the number of customers.
     *
     * @param numCustomers the number of customers
     */
    public void setNumCustomers(int numCustomers) {
        this.numCustomers = numCustomers;
    }
}