package com.realtimeeventticketing.cli;

import com.realtimeeventticketing.core.simulation.Simulation;
import com.realtimeeventticketing.core.simulation.SimulationBuilder;
import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.core.tickets.TicketEvent;
import com.realtimeeventticketing.core.tickets.TicketEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class CLI implements ITicketPoolObserver {

    private static final Logger log = LogManager.getLogger(CLI.class);
    private Simulation simulation;

    public static void main(String[] args) {
        CLI main = new CLI();
        main.start();
    }

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

        log.info("Simulation started. Press 'q' to quit.");
        handleUserCommands(scanner);
    }

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

    @Override
    public void onTicketEvent(TicketEvent ticketEvent) {
        log.info(ticketEvent.getMessage());

        if (ticketEvent.getEventType() == TicketEventType.SIMULATION_OVER) {
            log.info("Simulation over event received.");
            stop();
        }
    }
}
