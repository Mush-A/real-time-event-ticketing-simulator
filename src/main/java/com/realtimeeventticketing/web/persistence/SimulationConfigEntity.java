package com.realtimeeventticketing.web.persistence;

import com.realtimeeventticketing.core.simulation.SimulationConfig;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Entity class representing a simulation configuration in the database.
 */
@Entity
@Table(name = "simulation_configs")
public class SimulationConfigEntity {

    /**
     * Unique identifier for the simulation configuration.
     */
    @Id
    private UUID id;

    /**
     * Total number of tickets available in the simulation.
     */
    @Column(name = "total_tickets")
    private int totalTickets;

    /**
     * Rate at which tickets are released in the simulation.
     */
    @Column(name = "ticket_release_rate")
    private int ticketReleaseRate;

    /**
     * Rate at which customers retrieve tickets in the simulation.
     */
    @Column(name = "customer_retrieval_rate")
    private int customerRetrievalRate;

    /**
     * Maximum capacity of tickets in the simulation.
     */
    @Column(name = "max_tickets_capacity")
    private int maxTicketsCapacity;

    /**
     * Number of vendors in the simulation.
     */
    @Column(name = "num_vendors")
    private int numVendors;

    /**
     * Number of customers in the simulation.
     */
    @Column(name = "num_customers")
    private int numCustomers;

    /**
     * Default constructor for JPA.
     */
    public SimulationConfigEntity() {}

    /**
     * Constructs a SimulationConfigEntity from a SimulationConfig object.
     *
     * @param config the SimulationConfig object
     */
    public SimulationConfigEntity(SimulationConfig config) {
        this.id = config.getId();
        this.totalTickets = config.getTotalTickets();
        this.ticketReleaseRate = config.getTicketReleaseRate();
        this.customerRetrievalRate = config.getCustomerRetrievalRate();
        this.maxTicketsCapacity = config.getMaxTicketsCapacity();
        this.numVendors = config.getNumVendors();
        this.numCustomers = config.getNumCustomers();
    }

    // Getters and Setters

    /**
     * Gets the unique identifier of the simulation configuration.
     *
     * @return the unique identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the simulation configuration.
     *
     * @param id the unique identifier
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the total number of tickets available in the simulation.
     *
     * @return the total number of tickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Sets the total number of tickets available in the simulation.
     *
     * @param totalTickets the total number of tickets
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    /**
     * Gets the rate at which tickets are released in the simulation.
     *
     * @return the ticket release rate
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Sets the rate at which tickets are released in the simulation.
     *
     * @param ticketReleaseRate the ticket release rate
     */
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Gets the rate at which customers retrieve tickets in the simulation.
     *
     * @return the customer retrieval rate
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Sets the rate at which customers retrieve tickets in the simulation.
     *
     * @param customerRetrievalRate the customer retrieval rate
     */
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    /**
     * Gets the maximum capacity of tickets in the simulation.
     *
     * @return the maximum tickets capacity
     */
    public int getMaxTicketsCapacity() {
        return maxTicketsCapacity;
    }

    /**
     * Sets the maximum capacity of tickets in the simulation.
     *
     * @param maxTicketsCapacity the maximum tickets capacity
     */
    public void setMaxTicketsCapacity(int maxTicketsCapacity) {
        this.maxTicketsCapacity = maxTicketsCapacity;
    }

    /**
     * Gets the number of vendors in the simulation.
     *
     * @return the number of vendors
     */
    public int getNumVendors() {
        return numVendors;
    }

    /**
     * Sets the number of vendors in the simulation.
     *
     * @param numVendors the number of vendors
     */
    public void setNumVendors(int numVendors) {
        this.numVendors = numVendors;
    }

    /**
     * Gets the number of customers in the simulation.
     *
     * @return the number of customers
     */
    public int getNumCustomers() {
        return numCustomers;
    }

    /**
     * Sets the number of customers in the simulation.
     *
     * @param numCustomers the number of customers
     */
    public void setNumCustomers(int numCustomers) {
        this.numCustomers = numCustomers;
    }
}