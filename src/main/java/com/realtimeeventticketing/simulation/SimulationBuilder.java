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

    private List<Vendor> buildVendors(int numVendors) {
        List<Vendor> vendors = new ArrayList<>();
        for (int i = 0; i < numVendors; i++) {
            vendors.add(new Vendor("Vendor " + (i + 1), ticketPool, this.ticketReleaseRate));
        }
        return vendors;
    }

    private List<Customer> buildCustomers(int numCustomers) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < numCustomers; i++) {
            customers.add(new Customer("Customer " + (i + 1), ticketPool, this.customerRetrievalRate));
        }
        return customers;
    }

    public SimulationBuilder buildSimulation() {
        List<Vendor> vendors = buildVendors(this.numVendors);
        List<Customer> customers = buildCustomers(this.numCustomers);
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

    public SimulationBuilder updateSimulation(SimulationRequest updateRequest) {
        this.totalTickets = updateRequest.getTotalTickets();
        this.ticketReleaseRate = updateRequest.getTicketReleaseRate();
        this.customerRetrievalRate = updateRequest.getCustomerRetrievalRate();
        this.maxTicketsCapacity = updateRequest.getMaxTicketsCapacity();
        this.numVendors = updateRequest.getNumVendors();
        this.numCustomers = updateRequest.getNumCustomers();

        this.ticketPool.setTotalTickets(this.totalTickets);
        this.ticketPool.setMaxTicketsCapacity(this.maxTicketsCapacity);

        this.simulation.setVendorReleaseRate(this.ticketReleaseRate);
        this.simulation.setCustomerRetrievalRate(this.customerRetrievalRate);

        if (this.simulation.getCustomers().size() > this.numCustomers) {
            this.simulation.removeCustomers(this.simulation.getCustomers().size() - this.numCustomers);
        } else if (this.simulation.getCustomers().size() < this.numCustomers) {
            int numOfNewCustomers = this.numCustomers - this.simulation.getCustomers().size();
            List<Customer> newCustomers = this.buildCustomers(numOfNewCustomers);
            this.simulation.addCustomers(newCustomers);
        }

        if (this.simulation.getVendors().size() > this.numVendors) {
            this.simulation.removeVendors(this.simulation.getVendors().size() - this.numVendors);
        } else if (this.simulation.getVendors().size() < this.numVendors) {
            int numOfNewVendors = this.numVendors - this.simulation.getVendors().size();
            List<Vendor> newVendors = this.buildVendors(numOfNewVendors);
            this.simulation.addVendors(newVendors);
        }

        return this;
    }
}
