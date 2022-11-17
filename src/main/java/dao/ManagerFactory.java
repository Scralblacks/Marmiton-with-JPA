package dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ManagerFactory {

    private static EntityManagerFactory FACTORY_INSTANCE;

    private ManagerFactory(){

    }

    public static EntityManagerFactory getFactoryInstance() throws SQLException {
        if (FACTORY_INSTANCE == null || !FACTORY_INSTANCE.isOpen() ) {
            FACTORY_INSTANCE = Persistence.createEntityManagerFactory("myPU");
        }
        return FACTORY_INSTANCE;
    }
}

