package com.realtimeeventticketing.core.simulation;

import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.core.tickets.TicketEvent;
import com.realtimeeventticketing.core.tickets.TicketPool;
import com.realtimeeventticketing.core.users.Customer;
import com.realtimeeventticketing.core.users.Vendor;

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

    public SimulationBuilder buildTicketPool(ITicketPoolObserver observer) {
        this.ticketPool = new TicketPool(totalTickets, maxTicketsCapacity);
        this.ticketPool.addObserver(observer); // Register observer
        return this;
    }

    private List<Vendor> buildVendors(int numVendors) {
        List<Vendor> vendors = new ArrayList<>();
        for (int i = 0; i < numVendors; i++) {
            vendors.add(new Vendor(ticketPool, this.ticketReleaseRate));
        }
        return vendors;
    }

    private List<Customer> buildCustomers(int numCustomers) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < numCustomers; i++) {
            customers.add(new Customer(ticketPool, this.customerRetrievalRate));
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
            Customer.resetId();
            Vendor.resetId();
        }
    }

    public boolean isSimulationRunning() {
        return this.simulation != null && this.simulation.isRunning();
    }

    public void updateSimulation(SimulationRequest updateRequest) {
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

        int currentCustomers = this.simulation.getCustomers().size();
        if (currentCustomers > this.numCustomers) {
            this.simulation.removeCustomers(currentCustomers - this.numCustomers);
        } else if (currentCustomers < this.numCustomers) {
            int numOfNewCustomers = this.numCustomers - currentCustomers;
            List<Customer> newCustomers = this.buildCustomers(numOfNewCustomers);
            this.simulation.addCustomers(newCustomers);
        }

        int currentVendors = this.simulation.getVendors().size();
        if (currentVendors > this.numVendors) {
            this.simulation.removeVendors(currentVendors - this.numVendors);
        } else if (currentVendors < this.numVendors) {
            int numOfNewVendors = this.numVendors - currentVendors;
            List<Vendor> newVendors = this.buildVendors(numOfNewVendors);
            this.simulation.addVendors(newVendors);
        }
    }

    public List<TicketEvent> getTicketEvents() {
        return this.ticketPool.getEventStore();
    }
}
