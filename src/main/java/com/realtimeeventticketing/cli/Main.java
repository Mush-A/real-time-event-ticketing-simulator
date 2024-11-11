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
                .setNumCustomers(userInputHandler.getNumCustomers())
                .buildTicketPool()
                .buildSimulation();

        System.out.println("All threads have finished execution.");
    }
}
