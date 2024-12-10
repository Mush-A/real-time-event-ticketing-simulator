package com.realtimeeventticketing.core.simulation;

import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;

public class SimulationBuilder {
    private final SimulationConfig config = new SimulationConfig();

    public SimulationBuilder setTotalTickets(int totalTickets) {
        config.setTotalTickets(totalTickets);
        return this;
    }

    public SimulationBuilder setTicketReleaseRate(int ticketReleaseRate) {
        config.setTicketReleaseRate(ticketReleaseRate);
        return this;
    }

    public SimulationBuilder setCustomerRetrievalRate(int customerRetrievalRate) {
        config.setCustomerRetrievalRate(customerRetrievalRate);
        return this;
    }

    public SimulationBuilder setMaxTicketsCapacity(int maxTicketsCapacity) {
        config.setMaxTicketsCapacity(maxTicketsCapacity);
        return this;
    }

    public SimulationBuilder setNumVendors(int numVendors) {
        config.setNumVendors(numVendors);
        return this;
    }

    public SimulationBuilder setNumCustomers(int numCustomers) {
        config.setNumCustomers(numCustomers);
        return this;
    }

    public Simulation buildSimulation(ITicketPoolObserver observer) {
        return new Simulation(config, observer);
    }

    // Update the simulation
    public void updateSimulation(Simulation simulation) {
        simulation.updateTicketPool(config.getTotalTickets(), config.getMaxTicketsCapacity());
        simulation.updateVendorReleaseRate(config.getTicketReleaseRate());
        simulation.updateCustomerRetrievalRate(config.getCustomerRetrievalRate());
        simulation.updateVendors(config.getNumVendors());
        simulation.updateCustomers(config.getNumCustomers());
    }
}
