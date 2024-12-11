package com.realtimeeventticketing.web.persistence;

import com.realtimeeventticketing.core.simulation.SimulationConfig;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "simulation_configs")
public class SimulationConfigEntity {

    @Id
    private UUID id;

    @Column(name = "total_tickets")
    private int totalTickets;

    @Column(name = "ticket_release_rate")
    private int ticketReleaseRate;

    @Column(name = "customer_retrieval_rate")
    private int customerRetrievalRate;

    @Column(name = "max_tickets_capacity")
    private int maxTicketsCapacity;

    @Column(name = "num_vendors")
    private int numVendors;

    @Column(name = "num_customers")
    private int numCustomers;

    public SimulationConfigEntity() {}

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketsCapacity() {
        return maxTicketsCapacity;
    }

    public void setMaxTicketsCapacity(int maxTicketsCapacity) {
        this.maxTicketsCapacity = maxTicketsCapacity;
    }

    public int getNumVendors() {
        return numVendors;
    }

    public void setNumVendors(int numVendors) {
        this.numVendors = numVendors;
    }

    public int getNumCustomers() {
        return numCustomers;
    }

    public void setNumCustomers(int numCustomers) {
        this.numCustomers = numCustomers;
    }
}