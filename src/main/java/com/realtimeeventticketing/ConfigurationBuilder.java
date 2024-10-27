package com.realtimeeventticketing;

import com.realtimeeventticketing.entities.Customer;
import com.realtimeeventticketing.entities.Vendor;
import com.realtimeeventticketing.util.InputValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigurationBuilder {
    private final Scanner scanner;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketsCapacity;
    private int numVendors;
    private int numCustomers;

    public ConfigurationBuilder(Scanner scanner) {
        this.scanner = scanner;
    }

    public ConfigurationBuilder setTotalTickets() {
        System.out.println("Enter total number of tickets:");
        this.totalTickets = InputValidator.getValidIntInput(scanner);
        return this;
    }

    public ConfigurationBuilder setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
        return this;
    }

    public ConfigurationBuilder setTicketReleaseRate() {
        System.out.println("Enter ticket release rate (milliseconds):");
        this.ticketReleaseRate = InputValidator.getValidIntInput(scanner);
        return this;
    }

    public ConfigurationBuilder setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
        return this;
    }

    public ConfigurationBuilder setCustomerRetrievalRate() {
        System.out.println("Enter customer retrieval rate (milliseconds):");
        this.customerRetrievalRate = InputValidator.getValidIntInput(scanner);
        return this;
    }

    public ConfigurationBuilder setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
        return this;
    }

    public ConfigurationBuilder setMaxTicketsCapacity() {
        System.out.println("Enter maximum tickets capacity:");
        this.maxTicketsCapacity = InputValidator.getValidIntInput(scanner);
        return this;
    }

    public ConfigurationBuilder setMaxTicketsCapacity(int maxTicketsCapacity) {
        this.maxTicketsCapacity = maxTicketsCapacity;
        return this;
    }

    public ConfigurationBuilder setNumVendors() {
        System.out.println("Enter number of vendors:");
        this.numVendors = InputValidator.getValidIntInput(scanner);
        return this;
    }

    public ConfigurationBuilder setNumVendors(int numVendors) {
        this.numVendors = numVendors;
        return this;
    }

    public ConfigurationBuilder setNumCustomers() {
        System.out.println("Enter number of customers:");
        this.numCustomers = InputValidator.getValidIntInput(scanner);
        return this;
    }

    public ConfigurationBuilder setNumCustomers(int numCustomers) {
        this.numCustomers = numCustomers;
        return this;
    }

    public TicketPool buildTicketPool() {
        return new TicketPool(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketsCapacity);
    }

    public List<Vendor> buildVendors(TicketPool ticketPool) {
        List<Vendor> vendors = new ArrayList<>();
        for (int i = 0; i < numVendors; i++) {
            vendors.add(new Vendor("Vendor " + (i + 1), ticketPool));
        }
        return vendors;
    }

    public List<Customer> buildCustomers(TicketPool ticketPool) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < numCustomers; i++) {
            customers.add(new Customer("Customer " + (i + 1), ticketPool));
        }
        return customers;
    }
}
