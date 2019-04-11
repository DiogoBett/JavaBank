package org.academiadecodigo.javabank.persistence.jpa;

import org.academiadecodigo.javabank.persistence.TransactionManager;

/**
 * A JPA {@link TransactionManager} implementation
 */
public class JpaTransactionManager implements TransactionManager {

    private JpaSessionManager sm;

    /**
     * Initializes a new {@code JPA Transaction Manager} instance given a session manager
     *
     * @param sm the session manager
     */
    public JpaTransactionManager(JpaSessionManager sm) {
        this.sm = sm;
    }

    /**
     * @see TransactionManager#beginRead()
     */
    public void beginRead() {
       sm.startSession();
    }

    /**
     * @see TransactionManager#beginWrite()
     */
    public void beginWrite() {
        sm.getCurrentSession().getTransaction().begin();
    }

    /**
     * @see TransactionManager#commit()
     */
    public void commit() {

        if (sm.getCurrentSession().getTransaction().isActive()) {
            sm.getCurrentSession().getTransaction().commit();
        }

        sm.stopSession();
    }

    /**
     * @see TransactionManager#rollback()
     */
    public void rollback() {

        if (sm.getCurrentSession().getTransaction().isActive()) {
            sm.getCurrentSession().getTransaction().rollback();
        }

        sm.stopSession();
    }
}
