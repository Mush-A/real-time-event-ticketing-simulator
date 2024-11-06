package com.realtimeeventticketing.cli;

import com.realtimeeventticketing.simulation.SimulationBuilder;
import com.realtimeeventticketing.simulation.Simulation;
import com.realtimeeventticketing.simulation.TicketPool;
import com.realtimeeventticketing.simulation.Customer;
import com.realtimeeventticketing.simulation.Vendor;


import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        UserInputHandler userInputHandler = new UserInputHandler(scanner);

        SimulationBuilder builder = new SimulationBuilder()
                .setTotalTickets(userInputHandler.getTotalTickets())
                .setTicketReleaseRate(userInputHandler.getTicketReleaseRate())
                .setCustomerRetrievalRate(userInputHandler.getCustomerRetrievalRate())
                .setMaxTicketsCapacity(userInputHandler.getMaxTicketsCapacity())
                .setNumVendors(userInputHandler.getNumVendors())
                .setNumCustomers(userInputHandler.getNumCustomers());

        TicketPool ticketPool = builder.buildTicketPool();
        List<Vendor> vendors = builder.buildVendors(ticketPool);
        List<Customer> customers = builder.buildCustomers(ticketPool);

        // Run the simulation
        Simulation simulationRunner = new Simulation(vendors, customers);
        simulationRunner.run();

        System.out.println("All threads have finished execution.");
    }
}
