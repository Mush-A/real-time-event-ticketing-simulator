package com.realtimeeventticketing.web.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for managing Hibernate SessionFactory.
 */
public class HibernateUtil {
    /**
     * The singleton instance of SessionFactory.
     */
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Logger instance for logging events.
     */
    private static final Logger log = LogManager.getLogger(HibernateUtil.class);

    /**
     * Builds the SessionFactory from the Hibernate configuration.
     *
     * @return the built SessionFactory
     * @throws ExceptionInInitializerError if there is an error during the creation of the SessionFactory
     */
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().addAnnotatedClass(SimulationConfigEntity.class).buildSessionFactory();
        } catch (Exception e) {
            log.error("Failed to create session factory", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Gets the singleton instance of SessionFactory.
     *
     * @return the SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Shuts down the SessionFactory, closing all connections.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}