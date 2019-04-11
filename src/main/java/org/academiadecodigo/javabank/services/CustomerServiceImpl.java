package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.model.Customer;
import org.academiadecodigo.javabank.model.Recipient;
import org.academiadecodigo.javabank.model.account.Account;
import org.academiadecodigo.javabank.persistence.TransactionManager;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An {@link CustomerService} implementation
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao;
    private TransactionManager tx;

    /**
     * Sets the customer data access object
     *
     * @param customerDao the account DAO to set
     */
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * Sets the transaction manager
     *
     * @param tx the transaction manager to set
     */
    public void setTransactionManager(TransactionManager tx) {
        this.tx = tx;
    }

    /**
     * @see CustomerService#get(Integer)
     */
    @Override
    public Customer get(Integer id) {

        try {

            tx.beginRead();
            return customerDao.findById(id);

        } finally {
            tx.commit();
        }
    }

    /**
     * @see CustomerService#getBalance(Integer)
     */
    @Override
    public double getBalance(Integer id) {

        try {

            tx.beginRead();

            Customer customer = customerDao.findById(id);

            if (customer == null) {
                throw new IllegalArgumentException("Customer does not exists");
            }

            List<Account> accounts = customer.getAccounts();

            double balance = 0;
            for (Account account : accounts) {
                balance += account.getBalance();
            }

            return balance;

        } finally {
            tx.commit();
        }
    }

    /**
     * @see CustomerService#listCustomerAccountIds(Integer)
     */
    @Override
    public Set<Integer> listCustomerAccountIds(Integer id) {

        try {

            tx.beginRead();

            Customer customer = customerDao.findById(id);

            if (customer == null) {
                throw new IllegalArgumentException("Customer does not exists");
            }

            Set<Integer> accountIds = new HashSet<>();
            List<Account> accounts = customer.getAccounts();

            for (Account account : accounts) {
                accountIds.add(account.getId());
            }

            return accountIds;

        } finally {
            tx.commit();
        }
    }

    /**
     * @see CustomerService#listRecipients(Integer)
     */
    @Override
    public List<Recipient> listRecipients(Integer id) {

        try {

            tx.beginRead();

            Customer customer = customerDao.findById(id);
            if (customer == null) {
                throw new IllegalArgumentException("Customer does not exists");
            }

            return new ArrayList<>(customerDao.findById(id).getRecipients());

        } finally {
            tx.commit();
        }
    }
}
