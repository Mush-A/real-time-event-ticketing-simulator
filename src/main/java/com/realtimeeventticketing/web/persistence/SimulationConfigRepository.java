package com.realtimeeventticketing.web.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SimulationConfigRepository {

    private static final Logger log = LogManager.getLogger(SimulationConfigRepository.class);

    public void save(SimulationConfigEntity config) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(config);
            transaction.commit();
        } catch (Exception e) {
            log.error("Failed to save simulation config", e);
        }
    }

    public SimulationConfigEntity findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(SimulationConfigEntity.class, id);
        } catch (Exception e) {
            log.error("Failed to find simulation config", e);
            return null;
        }
    }

    public List<SimulationConfigEntity> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from SimulationConfigEntity", SimulationConfigEntity.class).list();
        } catch (Exception e) {
            log.error("Failed to find all simulation configs", e);
            return new ArrayList<>();
        }
    }
}