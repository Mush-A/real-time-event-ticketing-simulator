package com.realtimeeventticketing.web.services;

import com.realtimeeventticketing.core.simulation.Simulation;
import com.realtimeeventticketing.core.simulation.SimulationBuilder;
import com.realtimeeventticketing.core.simulation.SimulationRequest;
import com.realtimeeventticketing.core.simulation.SimulationStatusType;
import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.core.tickets.TicketEvent;
import com.realtimeeventticketing.core.tickets.TicketEventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService implements ITicketPoolObserver {

    private static final Logger log = LogManager.getLogger(SimulationService.class);
    private final SimpMessagingTemplate messagingTemplate;
    private Simulation simulation;

    public SimulationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public String startSimulation(SimulationRequest request) {
        if (simulation != null && simulation.isRunning()) {
            return "Simulation is already running.";
        }

        SimulationBuilder builder = new SimulationBuilder()
                .setTotalTickets(request.getTotalTickets())
                .setTicketReleaseRate(request.getTicketReleaseRate())
                .setCustomerRetrievalRate(request.getCustomerRetrievalRate())
                .setMaxTicketsCapacity(request.getMaxTicketsCapacity())
                .setNumVendors(request.getNumVendors())
                .setNumCustomers(request.getNumCustomers());

        simulation = builder.buildSimulation(this); // Pass this service as the observer
        simulation.run();

        log.info("Simulation started.");
        return "Simulation started.";
    }

    public String stopSimulation() {
        if (simulation != null && simulation.isRunning()) {
            try {
                simulation.stop();
                log.info("Simulation stopped.");
                return "Simulation stopped.";
            } catch (InterruptedException e) {
                log.error("Error stopping the simulation: ", e);
                Thread.currentThread().interrupt();
                return "Error stopping the simulation.";
            }
        }
        return "No simulation is running.";
    }

    public SimulationStatusType getSimulationStatus() {
        if (simulation != null && simulation.isRunning()) {
            return SimulationStatusType.RUNNING;
        }
        return SimulationStatusType.NOT_RUNNING;
    }

    public String updateSimulation(SimulationRequest request) {
        if (simulation != null && simulation.isRunning()) {
            SimulationBuilder builder = new SimulationBuilder()
                    .setTotalTickets(request.getTotalTickets())
                    .setTicketReleaseRate(request.getTicketReleaseRate())
                    .setCustomerRetrievalRate(request.getCustomerRetrievalRate())
                    .setMaxTicketsCapacity(request.getMaxTicketsCapacity())
                    .setNumVendors(request.getNumVendors())
                    .setNumCustomers(request.getNumCustomers());

            builder.updateSimulation(this.simulation);
            log.info("Simulation updated.");
            return "Simulation updated.";
        }
        return "No simulation is running.";
    }

    public List<TicketEvent> getTicketEvents() {
        if (simulation != null) {
            return simulation.getTicketPool().getEventStore();
        }
        return new ArrayList<>();
    }

    @Override
    public void onTicketEvent(TicketEvent ticketEvent) {
        sendSimulationUpdate(ticketEvent);

        if (ticketEvent.getEventType() == TicketEventType.SIMULATION_OVER) {
            stopSimulation();
        }

        log.info(ticketEvent.getMessage());
    }

    private void sendSimulationUpdate(TicketEvent ticketEvent) {
        messagingTemplate.convertAndSend("/topic/simulation", ticketEvent);
    }
}
