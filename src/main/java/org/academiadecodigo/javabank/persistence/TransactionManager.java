package org.academiadecodigo.javabank.persistence;

/**
 * Common interface for database transactions, provides methods to manage transactions
 */
public interface TransactionManager {

    /**
     * Begin reading
     */
    void beginRead();

    /**
     * Begin writing
     */
    void beginWrite();

    /**
     * Commit the transaction
     */
    void commit();

    /**
     * Rollback the transaction
     */
    void rollback();
}
