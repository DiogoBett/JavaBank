package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.persistence.model.account.Account;

import java.util.List;

/**
 * Common interface for account services, provides methods to manage accounts and perform account transactions
 */
public interface AccountService {

    /**
     * Gets the account with the given id
     *
     * @param id the account id
     * @return the account
     */
    Account get(Integer id);

    List<Account> getAccounts(Integer customerId);

    void delete(Integer id);
    /**
     * Perform an {@link Account} deposit
     *
     * @param id     the id of the account
     * @param amount the amount to deposit
     * @throws AccountNotFoundException
     */
    void deposit(Integer id, double amount) throws AccountNotFoundException;

    /**
     * Perform an {@link Account} withdrawal
     *
     * @param id     the id of the account
     * @param amount the amount to withdraw
     * @throws AccountNotFoundException
     */
    void withdraw(Integer id, double amount) throws AccountNotFoundException;

    /**
     * Performs a transfer between two {@link Account} if possible
     *
     * @param srcId  the source account id
     * @param dstId  the destination account id
     * @param amount the amount to transfer
     * @throws AccountNotFoundException
     */
    void transfer(Integer srcId, Integer dstId, double amount) throws AccountNotFoundException;
}
