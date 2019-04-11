package org.academiadecodigo.javabank.persistence;

/**
 * Common interface for session managers, provides methods to manage database sessions
 * @param <T> the session type
 */
public interface SessionManager<T> {

    /**
     * Starts the session
     */
    void startSession();

    /**
     * Stops the session
     */
    void stopSession();

    /**
     * Gets the current session
     *
     * @return the current session
     */
    T getCurrentSession();
}
