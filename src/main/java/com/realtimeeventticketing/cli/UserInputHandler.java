package com.realtimeeventticketing.cli;

import java.util.Scanner;

public class UserInputHandler {
    private final Scanner scanner;

    public UserInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getTotalTickets() {
        System.out.print("Enter total number of tickets: ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getTicketReleaseRate() {
        System.out.print("Enter ticket release rate (milliseconds): ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getCustomerRetrievalRate() {
        System.out.print("Enter customer retrieval rate (milliseconds): ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getMaxTicketsCapacity() {
        System.out.print("Enter maximum tickets capacity: ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getNumVendors() {
        System.out.print("Enter number of vendors: ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getNumCustomers() {
        System.out.print("Enter number of customers: ");
        return InputValidator.getValidIntInput(scanner);
    }
}
