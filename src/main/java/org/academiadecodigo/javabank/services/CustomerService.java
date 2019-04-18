package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.AssociationExistsException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.RecipientNotFoundException;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;

import java.util.List;

/**
 * Common interface for customer services, provides methods to manage customers
 */
public interface CustomerService {

    /**
     * Gets the customer with the given id
     *
     * @param id the customer id
     * @return the customer
     */
    Customer get(Integer id);

    /**
     * Gets the balance of the customer
     *
     * @param id the customer id
     * @return the balance of the customer with the given id
     * @throws CustomerNotFoundException
     */
    double getBalance(Integer id) throws CustomerNotFoundException;

    /**
     * Saves a customer
     *
     * @param customer the customer to save
     * @return the saved customer
     */
    Customer save(Customer customer);

    /**
     * Deletes a customer
     *
     * @param id the customer id
     * @throws CustomerNotFoundException
     * @throws AssociationExistsException
     */
    void delete(Integer id) throws CustomerNotFoundException, AssociationExistsException;

    /**
     * Gets a list of the customers
     *
     * @return the customers list
     */
    List<Customer> list();

    /**
     * Gets the list of customer recipients
     *
     * @param id the customer id
     * @return the list of recipients of the customer
     * @throws CustomerNotFoundException
     */
    List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException;

    /**
     * Adds a recipient to the customer
     *
     * @param id        the customer id
     * @param recipient the recipient to add
     * @throws CustomerNotFoundException
     * @throws AccountNotFoundException
     */
    void addRecipient(Integer id, Recipient recipient) throws CustomerNotFoundException, AccountNotFoundException;

    /**
     * Removes a recipient from the customer
     *
     * @param id          the customer id
     * @param recipientId the recipient id
     * @throws CustomerNotFoundException
     * @throws RecipientNotFoundException
     */
    void removeRecipient(Integer id, Integer recipientId) throws CustomerNotFoundException, RecipientNotFoundException;
}
