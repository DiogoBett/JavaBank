package org.academiadecodigo.javabank.persistence;

/**
 * A {@link RuntimeException} used to wrap database transactions errors
 */
public class TransactionException extends RuntimeException {

    /**
     * @see RuntimeException#RuntimeException(Throwable)
     */
    public TransactionException(Throwable cause) {
        super(cause);
    }
}
