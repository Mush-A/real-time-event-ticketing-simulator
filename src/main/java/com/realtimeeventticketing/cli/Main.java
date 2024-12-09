package com.realtimeeventticketing.cli;

import com.realtimeeventticketing.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.simulation.SimulationBuilder;
import com.realtimeeventticketing.tickets.TicketEvent;
import com.realtimeeventticketing.tickets.TicketEventType;

import java.util.Scanner;

public class Main implements ITicketPoolObserver {

    private SimulationBuilder simulationBuilder;

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        main.startSimulation();
    }

    public void startSimulation() throws InterruptedException {
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

        System.out.println("Simulation started");
    }

    public void stopSimulation() throws InterruptedException {
        simulationBuilder.stopSimulation();
    }

    @Override
    public void onTicketEvent(TicketEvent ticketEvent) throws InterruptedException {
        if (ticketEvent.getEventType() == TicketEventType.SIMULATION_OVER) {
            stopSimulation();
        }

        System.out.println(ticketEvent.getMessage());
    }
}
