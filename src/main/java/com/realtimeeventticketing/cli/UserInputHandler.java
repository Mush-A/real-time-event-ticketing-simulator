package com.realtimeeventticketing.cli;

import java.util.Scanner;

public class UserInputHandler {
    private final Scanner scanner;

    public UserInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getTotalTickets() {
        System.out.println("Enter total number of tickets:");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getTicketReleaseRate() {
        System.out.println("Enter ticket release rate (milliseconds):");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getCustomerRetrievalRate() {
        System.out.println("Enter customer retrieval rate (milliseconds):");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getMaxTicketsCapacity() {
        System.out.println("Enter maximum tickets capacity:");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getNumVendors() {
        System.out.println("Enter number of vendors:");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getNumCustomers() {
        System.out.println("Enter number of customers:");
        return InputValidator.getValidIntInput(scanner);
    }
}
