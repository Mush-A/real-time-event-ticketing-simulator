package com.realtimeeventticketing.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class UserInputHandler {
    private static final Logger log = LogManager.getLogger(UserInputHandler.class);
    private final Scanner scanner;

    public UserInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getTotalTickets() {
        log.info("Enter total number of tickets: ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getTicketReleaseRate() {
        log.info("Enter ticket release rate (milliseconds): ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getCustomerRetrievalRate() {
        log.info("Enter customer retrieval rate (milliseconds): ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getMaxTicketsCapacity() {
        log.info("Enter maximum tickets capacity: ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getNumVendors() {
        log.info("Enter number of vendors: ");
        return InputValidator.getValidIntInput(scanner);
    }

    public int getNumCustomers() {
        log.info("Enter number of customers: ");
        return InputValidator.getValidIntInput(scanner);
    }
}
