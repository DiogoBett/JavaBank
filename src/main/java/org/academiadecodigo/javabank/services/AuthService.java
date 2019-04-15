package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.model.Customer;

/**
 * Common interface for authentication services, provides method for customer authentication
 */
public interface AuthService {

    /**
     * Authenticates the accessing customer
     *
     * @param id the customer id
     * @return {@code true} if authentication was successful
     */
    boolean authenticate(Integer id);

    /**
     * Gets the accessing customer
     *
     * @return the customer
     */
    Customer getAccessingCustomer();
}
