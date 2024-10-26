package com.realtimeeventticketing;

import com.realtimeeventticketing.entities.Customer;
import com.realtimeeventticketing.entities.Vendor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        // Get and validate TicketPool configuration from CLI
        System.out.println("Enter total number of tickets:");
        int totalTickets = getValidIntInput(scanner);

        System.out.println("Enter ticket release rate (milliseconds):");
        int ticketReleaseRate = getValidIntInput(scanner);

        System.out.println("Enter customer retrieval rate (milliseconds):");
        int customerRetrievalRate = getValidIntInput(scanner);

        System.out.println("Enter maximum tickets capacity:");
        int maxTicketsCapacity = getValidIntInput(scanner);

        // Create the TicketPool with user-provided parameters
        TicketPool ticketPool = new TicketPool(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketsCapacity);

        // Get the number of Vendors and Customers from CLI
        System.out.println("Enter number of vendors:");
        int numVendors = getValidIntInput(scanner);

        System.out.println("Enter number of customers:");
        int numCustomers = getValidIntInput(scanner);

        // Create lists to hold vendors and customers
        List<Vendor> vendors = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        // Initialize vendors
        for (int i = 0; i < numVendors; i++) {
            vendors.add(new Vendor("Vendor " + (i + 1), ticketPool));
        }

        // Initialize customers
        for (int i = 0; i < numCustomers; i++) {
            customers.add(new Customer("Customer " + (i + 1), ticketPool));
        }

        // Create an ExecutorService with a fixed thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(numVendors + numCustomers);

        // Submit the Vendor and Customer tasks to the ExecutorService
        vendors.forEach(executorService::submit);
        customers.forEach(executorService::submit);

        // Run the simulation for a specified duration (e.g., 10 seconds)
        System.out.println("Running the simulation for 10 seconds...");
        Thread.sleep(10000);

        // Stop the simulation
        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

        // Shutdown the ExecutorService
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("All threads have finished execution.");
    }

    // Method to validate integer input
    private static int getValidIntInput(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    throw new NumberFormatException();
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a non-negative integer:");
            }
        }
    }
}
