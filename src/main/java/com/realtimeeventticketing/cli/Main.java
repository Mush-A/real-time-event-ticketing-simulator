package com.realtimeeventticketing.cli;

import com.realtimeeventticketing.simulation.ConfigurationBuilder;
import com.realtimeeventticketing.simulation.Simulation;
import com.realtimeeventticketing.simulation.TicketPool;
import com.realtimeeventticketing.simulation.Customer;
import com.realtimeeventticketing.simulation.Vendor;


import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        // Build configuration using the builder pattern
        ConfigurationBuilder configBuilder = new ConfigurationBuilder(scanner)
                .setTotalTickets()
                .setTicketReleaseRate()
                .setCustomerRetrievalRate()
                .setMaxTicketsCapacity()
                .setNumVendors()
                .setNumCustomers();

        TicketPool ticketPool = configBuilder.buildTicketPool();
        List<Vendor> vendors = configBuilder.buildVendors(ticketPool);
        List<Customer> customers = configBuilder.buildCustomers(ticketPool);

        // Run the simulation
        Simulation simulationRunner = new Simulation(vendors, customers);
        simulationRunner.run();

        System.out.println("All threads have finished execution.");
    }
}
