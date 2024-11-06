package com.realtimeeventticketing.cli;

import com.realtimeeventticketing.ConfigurationBuilder;
import com.realtimeeventticketing.Simulation;
import com.realtimeeventticketing.TicketPool;
import com.realtimeeventticketing.Customer;
import com.realtimeeventticketing.Vendor;


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
