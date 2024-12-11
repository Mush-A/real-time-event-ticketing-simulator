package com.realtimeeventticketing.web.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final Logger log = LogManager.getLogger(HibernateUtil.class);

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().addAnnotatedClass(SimulationConfigEntity.class).buildSessionFactory();
        } catch (Exception e) {
            log.error("Failed to create session factory", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}