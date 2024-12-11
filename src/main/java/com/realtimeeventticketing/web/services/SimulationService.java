package com.realtimeeventticketing.web.services;

import com.realtimeeventticketing.core.simulation.*;
import com.realtimeeventticketing.core.tickets.ITicketPoolObserver;
import com.realtimeeventticketing.core.tickets.TicketEvent;
import com.realtimeeventticketing.core.tickets.TicketEventType;
import com.realtimeeventticketing.web.persistence.SimulationConfigEntity;
import com.realtimeeventticketing.web.persistence.SimulationConfigRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing simulation operations.
 */
@Service
public class SimulationService implements ITicketPoolObserver {

    /**
     * Logger instance for logging events.
     */
    private static final Logger log = LogManager.getLogger(SimulationService.class);

    /**
     * Template for sending messages to WebSocket clients.
     */
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Repository for managing simulation configurations.
     */
    private final SimulationConfigRepository simulationConfigRepository;

    /**
     * The current simulation instance.
     */
    private Simulation simulation;

    /**
     * Constructs a SimulationService with the specified repository and messaging template.
     *
     * @param simulationConfigRepository the simulation configuration repository
     * @param messagingTemplate the messaging template
     */
    public SimulationService(SimulationConfigRepository simulationConfigRepository, SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.simulationConfigRepository = simulationConfigRepository;
    }

    /**
     * Starts a new simulation with the given request.
     *
     * @param request the simulation request
     * @return a message indicating the result of the operation
     */
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

        simulation = builder.buildSimulation(this);
        simulation.run();

        SimulationConfigEntity simulationConfigEntity = new SimulationConfigEntity(simulation.getConfig());
        this.simulationConfigRepository.save(simulationConfigEntity);

        log.info("Simulation started.");
        return "Simulation started.";
    }

    /**
     * Stops the currently running simulation.
     *
     * @return a message indicating the result of the operation
     */
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

    /**
     * Gets the current status of the simulation.
     *
     * @return the current simulation status
     */
    public SimulationStatusType getSimulationStatus() {
        if (simulation != null && simulation.isRunning()) {
            return SimulationStatusType.RUNNING;
        }
        return SimulationStatusType.NOT_RUNNING;
    }

    /**
     * Updates the simulation with the given request.
     *
     * @param request the simulation request
     * @return a message indicating the result of the operation
     */
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

    /**
     * Gets the list of ticket events.
     *
     * @return the list of ticket events
     */
    public List<TicketEvent> getTicketEvents() {
        if (simulation != null) {
            return simulation.getTicketPool().getEventStore();
        }
        return new ArrayList<>();
    }

    /**
     * Handles ticket events and sends updates to WebSocket clients.
     *
     * @param ticketEvent the ticket event
     */
    @Override
    public void onTicketEvent(TicketEvent ticketEvent) {
        sendSimulationUpdate(ticketEvent);

        if (ticketEvent.getEventType() == TicketEventType.SIMULATION_OVER) {
            stopSimulation();
        }

        log.info(ticketEvent.getMessage());
    }

    /**
     * Sends a simulation update to WebSocket clients.
     *
     * @param ticketEvent the ticket event
     */
    private void sendSimulationUpdate(TicketEvent ticketEvent) {
        messagingTemplate.convertAndSend("/topic/simulation", ticketEvent);
    }

    /**
     * Gets all simulation configurations.
     *
     * @return the list of all simulation configurations
     */
    public List<SimulationConfigEntity> getAllSimulationConfig() {
        return simulationConfigRepository.findAll();
    }
}