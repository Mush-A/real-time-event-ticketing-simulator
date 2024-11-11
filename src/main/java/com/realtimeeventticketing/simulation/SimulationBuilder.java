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
    private TicketPool ticketPool;
    private Simulation simulation;

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

    public SimulationBuilder buildTicketPool(SimulationController simulationController) {
        this.ticketPool = new TicketPool(totalTickets, maxTicketsCapacity, simulationController);
        return this;
    }

    public SimulationBuilder buildTicketPool() {
        this.ticketPool = new TicketPool(totalTickets, maxTicketsCapacity);
        return this;
    }

    private List<Vendor> buildVendors() {
        List<Vendor> vendors = new ArrayList<>();
        for (int i = 0; i < numVendors; i++) {
            vendors.add(new Vendor("Vendor " + (i + 1), ticketPool, this.ticketReleaseRate));
        }
        return vendors;
    }

    private List<Customer> buildCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < numCustomers; i++) {
            customers.add(new Customer("Customer " + (i + 1), ticketPool, this.customerRetrievalRate));
        }
        return customers;
    }

    public SimulationBuilder buildSimulation() {
        List<Vendor> vendors = buildVendors();
        List<Customer> customers = buildCustomers();
        this.simulation = new Simulation(vendors, customers);
        return this;
    }

    public void startSimulation() {
        if (this.simulation != null) {
            this.simulation.run();
        }
    }

    public void stopSimulation() throws InterruptedException {
        if (this.simulation != null) {
            this.simulation.stop();
            this.ticketPool.stopAllWaiting();
        }
    }

    public boolean isSimulationRunning() {
        return this.simulation != null && this.simulation.isRunning();
    }
}
