package org.academiadecodigo.javabank.persistence;

import org.academiadecodigo.javabank.Config;
import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Embedded Web interface for managing H2 databases, available in the development environment.
 */
public class H2WebServer {

    private Server server;

    /**
     * Creates a new {@code H2WebServer}
     *
     * @throws SQLException if the server cannot be created
     */
    public H2WebServer() throws SQLException {
        server = Server.createWebServer("-web", "-webAllowOthers", "-webDaemon", "-webPort", Config.H2_PORT);
    }

    /**
     * Starts the server
     *
     * @throws SQLException if the server cannot be started
     */
    public void start() throws SQLException {

        if (server != null) {
            server.start();
        }
    }

    /**
     * Stops the server
     */
    public void stop() {

        if (server != null) {
            server.stop();
        }
    }
}
