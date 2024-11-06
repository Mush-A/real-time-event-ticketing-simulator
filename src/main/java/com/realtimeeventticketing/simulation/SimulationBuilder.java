package com.realtimeeventticketing.simulation;

import java.util.ArrayList;
import java.util.List;

public class SimulationBuilder {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketsCapacity;
    private int numVendors;
    private int numCustomers;

    public SimulationBuilder setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
        return this;
    }

    public SimulationBuilder setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
        return this;
    }

    public SimulationBuilder setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
        return this;
    }

    public SimulationBuilder setMaxTicketsCapacity(int maxTicketsCapacity) {
        this.maxTicketsCapacity = maxTicketsCapacity;
        return this;
    }

    public SimulationBuilder setNumVendors(int numVendors) {
        this.numVendors = numVendors;
        return this;
    }

    public SimulationBuilder setNumCustomers(int numCustomers) {
        this.numCustomers = numCustomers;
        return this;
    }

    public TicketPool buildTicketPool() {
        return new TicketPool(totalTickets, maxTicketsCapacity);
    }

    public TicketPool buildTicketPool(SimulationController simulationController) {
        return new TicketPool(totalTickets, maxTicketsCapacity, simulationController);
    }

    public List<Vendor> buildVendors(TicketPool ticketPool) {
        List<Vendor> vendors = new ArrayList<>();
        for (int i = 0; i < numVendors; i++) {
            vendors.add(new Vendor("Vendor " + (i + 1), ticketPool, this.ticketReleaseRate));
        }
        return vendors;
    }

    public List<Customer> buildCustomers(TicketPool ticketPool) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < numCustomers; i++) {
            customers.add(new Customer("Customer " + (i + 1), ticketPool, this.customerRetrievalRate));
        }
        return customers;
    }
}
