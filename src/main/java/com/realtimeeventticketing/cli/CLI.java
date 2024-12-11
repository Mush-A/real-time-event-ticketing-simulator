package com.realtimeeventticketing.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtimeeventticketing.core.simulation.Simulation;
import com.realtimeeventticketing.core.simulation.SimulationBuilder;
import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.core.tickets.TicketEvent;
import com.realtimeeventticketing.core.tickets.TicketEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Command Line Interface for the simulation.
 */
public class CLI implements ITicketPoolObserver {

    private static final Logger log = LogManager.getLogger(CLI.class);
    private Simulation simulation;

    /**
     * Main method to start the CLI.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        CLI main = new CLI();
        main.start();
    }

    /**
     * Starts the CLI and initializes the simulation.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        UserInputHandler userInputHandler = new UserInputHandler(scanner);

        SimulationBuilder simulationBuilder = new SimulationBuilder()
                .setTotalTickets(userInputHandler.getTotalTickets())
                .setTicketReleaseRate(userInputHandler.getTicketReleaseRate())
                .setCustomerRetrievalRate(userInputHandler.getCustomerRetrievalRate())
                .setMaxTicketsCapacity(userInputHandler.getMaxTicketsCapacity())
                .setNumVendors(userInputHandler.getNumVendors())
                .setNumCustomers(userInputHandler.getNumCustomers());

        simulation = simulationBuilder.buildSimulation(this);

        simulation.run();

        saveConfigToFile();

        log.info("Simulation started. Press 'q' to quit.");
        handleUserCommands(scanner);
    }

    /**
     * Saves the current simulation configuration to a file.
     */
    private void saveConfigToFile() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object config = simulation.getConfig();

            File directory = new File("cli_simulation_config");
            if (!directory.exists() && !directory.mkdirs()) {
                log.error("Failed to create configuration directory: {}", directory.getAbsolutePath());
                return;
            }

            String baseFileName = "simulation_config";
            String fileExtension = ".json";
            File file;
            int counter = 0;

            do {
                String fileName = baseFileName + (counter == 0 ? "" : counter) + fileExtension;
                file = new File(directory, fileName);
                counter++;
            } while (file.exists());

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config);
            log.info("Simulation configuration saved to {}", file.getAbsolutePath());

        } catch (IOException e) {
            log.error("Failed to save simulation configuration: ", e);
        }
    }

    /**
     * Handles user commands from the CLI.
     *
     * @param scanner the scanner to read user input
     */
    private void handleUserCommands(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("q")) {
                log.info("Stopping simulation...");
                stop();
                break;
            } else {
                log.warn("Invalid input. Type 'q' to stop the simulation.");
            }
        }
    }

    /**
     * Stops the simulation.
     */
    public void stop() {
        try {
            if (simulation != null && simulation.isRunning()) {
                simulation.stop();
                log.info("Simulation stopped.");
            }
        } catch (InterruptedException e) {
            log.error("Error stopping the simulation: ", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Handles ticket events from the simulation.
     *
     * @param ticketEvent the ticket event
     */
    @Override
    public void onTicketEvent(TicketEvent ticketEvent) {
        log.info(ticketEvent.getMessage());

        if (ticketEvent.getEventType() == TicketEventType.SIMULATION_OVER) {
            log.info("Simulation over event received.");
            stop();
        }
    }
}