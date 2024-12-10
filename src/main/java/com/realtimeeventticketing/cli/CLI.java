package com.realtimeeventticketing.cli;

import com.realtimeeventticketing.core.simulation.SimulationBuilder;
import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.core.tickets.TicketEvent;
import com.realtimeeventticketing.core.tickets.TicketEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class CLI implements ITicketPoolObserver {

    private static final Logger log = LogManager.getLogger(CLI.class);
    private SimulationBuilder simulationBuilder;

    public static void main(String[] args) {
        CLI main = new CLI();
        main.startSimulation();
    }

    public void startSimulation() {
        Scanner scanner = new Scanner(System.in);
        UserInputHandler userInputHandler = new UserInputHandler(scanner);

        simulationBuilder = new SimulationBuilder()
                .setTotalTickets(userInputHandler.getTotalTickets())
                .setTicketReleaseRate(userInputHandler.getTicketReleaseRate())
                .setCustomerRetrievalRate(userInputHandler.getCustomerRetrievalRate())
                .setMaxTicketsCapacity(userInputHandler.getMaxTicketsCapacity())
                .setNumVendors(userInputHandler.getNumVendors())
                .setNumCustomers(userInputHandler.getNumCustomers())
                .buildTicketPool(this)
                .buildSimulation();

        simulationBuilder.startSimulation();

        log.info("Simulation started");
    }

    public void stopSimulation() throws InterruptedException {
        simulationBuilder.stopSimulation();
    }

    @Override
    public void onTicketEvent(TicketEvent ticketEvent) throws InterruptedException {
        if (ticketEvent.getEventType() == TicketEventType.SIMULATION_OVER) {
            stopSimulation();
        }

        log.info(ticketEvent.getMessage());
    }
}
