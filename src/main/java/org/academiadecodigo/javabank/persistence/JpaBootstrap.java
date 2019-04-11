package org.academiadecodigo.javabank.persistence;

import org.academiadecodigo.javabank.Config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;

public class JpaBootstrap {

    private boolean isH2;
    private EntityManagerFactory emf;
    private H2WebServer h2WebServer;

    public EntityManagerFactory start() {

        try {

            emf = Persistence.createEntityManagerFactory(Config.PERSISTENCE_UNIT);

            isH2 = emf.getProperties().get("hibernate.connection.driver_class").equals("org.h2.Driver");

            if (isH2) {
                h2WebServer = new H2WebServer();
                h2WebServer.start();
            }

        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.err.println("ERROR: JPA Failure - " + ex.getMessage());
        }

        return emf;
    }

    public void stop() {

        emf.close();

        if (isH2) {
            h2WebServer.stop();
        }
    }
}