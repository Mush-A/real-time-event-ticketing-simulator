package com.realtimeeventticketing.web.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository class for managing simulation configurations.
 */
@Component
public class SimulationConfigRepository {

    /**
     * Logger instance for logging events.
     */
    private static final Logger log = LogManager.getLogger(SimulationConfigRepository.class);

    /**
     * Saves a simulation configuration entity to the database.
     *
     * @param config the simulation configuration entity to save
     */
    public void save(SimulationConfigEntity config) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(config);
            transaction.commit();
        } catch (Exception e) {
            log.error("Failed to save simulation config", e);
        }
    }

    /**
     * Finds a simulation configuration entity by its unique identifier.
     *
     * @param id the unique identifier of the simulation configuration entity
     * @return the found simulation configuration entity, or null if not found
     */
    public SimulationConfigEntity findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SimulationConfigEntity.class, id);
        } catch (Exception e) {
            log.error("Failed to find simulation config", e);
            return null;
        }
    }

    /**
     * Finds all simulation configuration entities in the database.
     *
     * @return a list of all simulation configuration entities
     */
    public List<SimulationConfigEntity> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from SimulationConfigEntity", SimulationConfigEntity.class).list();
        } catch (Exception e) {
            log.error("Failed to find all simulation configs", e);
            return new ArrayList<>();
        }
    }
}