package org.academiadecodigo.javabank.exceptions;

/**
 * Thrown to indicate that an association still exists
 */
public class AssociationExistsException extends JavaBankException {

    /**
     * @see JavaBankException#JavaBankException(String)
     */
    public AssociationExistsException() {
        super("Entity contains association with another entity");
    }
}
