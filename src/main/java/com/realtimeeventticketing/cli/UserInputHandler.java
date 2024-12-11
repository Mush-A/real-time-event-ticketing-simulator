package com.realtimeeventticketing.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Handles user input for the CLI.
 */
public class UserInputHandler {
    private static final Logger log = LogManager.getLogger(UserInputHandler.class);
    private final Scanner scanner;

    /**
     * Constructs a UserInputHandler with the specified Scanner.
     *
     * @param scanner the Scanner to read user input
     */
    public UserInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prompts the user to enter the total number of tickets and returns the input.
     *
     * @return the total number of tickets
     */
    public int getTotalTickets() {
        log.info("Enter total number of tickets: ");
        return InputValidator.getValidIntInput(scanner);
    }

    /**
     * Prompts the user to enter the ticket release rate in milliseconds and returns the input.
     *
     * @return the ticket release rate in milliseconds
     */
    public int getTicketReleaseRate() {
        log.info("Enter ticket release rate (milliseconds): ");
        return InputValidator.getValidIntInput(scanner);
    }

    /**
     * Prompts the user to enter the customer retrieval rate in milliseconds and returns the input.
     *
     * @return the customer retrieval rate in milliseconds
     */
    public int getCustomerRetrievalRate() {
        log.info("Enter customer retrieval rate (milliseconds): ");
        return InputValidator.getValidIntInput(scanner);
    }

    /**
     * Prompts the user to enter the maximum tickets capacity and returns the input.
     *
     * @return the maximum tickets capacity
     */
    public int getMaxTicketsCapacity() {
        log.info("Enter maximum tickets capacity: ");
        return InputValidator.getValidIntInput(scanner);
    }

    /**
     * Prompts the user to enter the number of vendors and returns the input.
     *
     * @return the number of vendors
     */
    public int getNumVendors() {
        log.info("Enter number of vendors: ");
        return InputValidator.getValidIntInput(scanner);
    }

    /**
     * Prompts the user to enter the number of customers and returns the input.
     *
     * @return the number of customers
     */
    public int getNumCustomers() {
        log.info("Enter number of customers: ");
        return InputValidator.getValidIntInput(scanner);
    }
}