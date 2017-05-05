package com.appbar.util;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtil {
	
	private static final SessionFactory defaultSessionFactory;

    static {
        try {      
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            defaultSessionFactory = new Configuration().configure().buildSessionFactory();       		
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed ++++." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getDeafaultSessionFactory() {
        return defaultSessionFactory;
    }
    
    public static Session getSession() throws HibernateException{
        return defaultSessionFactory.openSession();
    }

}
