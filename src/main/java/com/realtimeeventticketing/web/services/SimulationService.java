package com.realtimeeventticketing.web.services;

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
    private SimulationBuilder simulationBuilder;

    public SimulationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public String startSimulation(SimulationRequest request) {
        if (simulationBuilder != null && simulationBuilder.isSimulationRunning()) {
            return "Simulation is already running.";
        }

        simulationBuilder = new SimulationBuilder()
                .setTotalTickets(request.getTotalTickets())
                .setTicketReleaseRate(request.getTicketReleaseRate())
                .setCustomerRetrievalRate(request.getCustomerRetrievalRate())
                .setMaxTicketsCapacity(request.getMaxTicketsCapacity())
                .setNumVendors(request.getNumVendors())
                .setNumCustomers(request.getNumCustomers())
                .buildTicketPool(this)
                .buildSimulation();

        simulationBuilder.startSimulation();
        return "Simulation started";
    }

    public String stopSimulation() throws InterruptedException {
        if (simulationBuilder != null) {
            simulationBuilder.stopSimulation();
            return "Simulation stopped.";
        }
        return "No simulation is running.";
    }

    public SimulationStatusType getSimulationStatus() {
        if (simulationBuilder != null && simulationBuilder.isSimulationRunning()) {
            return SimulationStatusType.RUNNING;
        }
        return SimulationStatusType.NOT_RUNNING;
    }

    public String updateSimulation(SimulationRequest request) {
        if (simulationBuilder != null) {
            simulationBuilder.updateSimulation(request);
            return "Simulation updated.";
        }
        return "No simulation is running.";
    }

    public List<TicketEvent> getTicketEvents() {
        if (simulationBuilder != null) {
            return simulationBuilder.getTicketEvents();
        }
        return new ArrayList<>();
    }

    @Override
    public void onTicketEvent(TicketEvent ticketEvent) throws InterruptedException {
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