package com.realtimeeventticketing.core.simulation;

import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;

/**
 * Builder class for creating and configuring a Simulation instance.
 */
public class SimulationBuilder {
    private final SimulationConfig config = new SimulationConfig();

    /**
     * Sets the total number of tickets for the simulation.
     *
     * @param totalTickets the total number of tickets
     * @return the current instance of SimulationBuilder
     */
    public SimulationBuilder setTotalTickets(int totalTickets) {
        config.setTotalTickets(totalTickets);
        return this;
    }

    /**
     * Sets the ticket release rate for the simulation.
     *
     * @param ticketReleaseRate the ticket release rate in milliseconds
     * @return the current instance of SimulationBuilder
     */
    public SimulationBuilder setTicketReleaseRate(int ticketReleaseRate) {
        config.setTicketReleaseRate(ticketReleaseRate);
        return this;
    }

    /**
     * Sets the customer retrieval rate for the simulation.
     *
     * @param customerRetrievalRate the customer retrieval rate in milliseconds
     * @return the current instance of SimulationBuilder
     */
    public SimulationBuilder setCustomerRetrievalRate(int customerRetrievalRate) {
        config.setCustomerRetrievalRate(customerRetrievalRate);
        return this;
    }

    /**
     * Sets the maximum tickets capacity for the simulation.
     *
     * @param maxTicketsCapacity the maximum tickets capacity
     * @return the current instance of SimulationBuilder
     */
    public SimulationBuilder setMaxTicketsCapacity(int maxTicketsCapacity) {
        config.setMaxTicketsCapacity(maxTicketsCapacity);
        return this;
    }

    /**
     * Sets the number of vendors for the simulation.
     *
     * @param numVendors the number of vendors
     * @return the current instance of SimulationBuilder
     */
    public SimulationBuilder setNumVendors(int numVendors) {
        config.setNumVendors(numVendors);
        return this;
    }

    /**
     * Sets the number of customers for the simulation.
     *
     * @param numCustomers the number of customers
     * @return the current instance of SimulationBuilder
     */
    public SimulationBuilder setNumCustomers(int numCustomers) {
        config.setNumCustomers(numCustomers);
        return this;
    }

    /**
     * Builds and returns a Simulation instance with the current configuration.
     *
     * @param observer the observer for ticket pool events
     * @return a new Simulation instance
     */
    public Simulation buildSimulation(ITicketPoolObserver observer) {
        return new Simulation(config, observer);
    }

    /**
     * Updates the given simulation with the current configuration.
     *
     * @param simulation the simulation to update
     */
    public void updateSimulation(Simulation simulation) {
        simulation.updateTicketPool(config.getTotalTickets(), config.getMaxTicketsCapacity());
        simulation.updateVendorReleaseRate(config.getTicketReleaseRate());
        simulation.updateCustomerRetrievalRate(config.getCustomerRetrievalRate());
        simulation.updateVendors(config.getNumVendors());
        simulation.updateCustomers(config.getNumCustomers());
    }
}