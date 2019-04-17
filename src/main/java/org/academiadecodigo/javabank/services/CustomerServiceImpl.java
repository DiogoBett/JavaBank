package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.academiadecodigo.javabank.persistence.dao.RecipientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.academiadecodigo.javabank.errors.ErrorMessage.*;

/**
 * An {@link CustomerService} implementation
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao;
    private RecipientDao recipientDao;
    private AccountDao accountDao;

    /**
     * Sets the customer data access object
     *
     * @param customerDao the account DAO to set
     */
    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * Sets the recipient data access object
     *
     * @param recipientDao the recipient DAO to set
     */
    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
    }

    /**
     * Sets the account data access object
     *
     * @param accountDao the account DAO to set
     */
    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * @see CustomerService#get(Integer)
     */
    public Customer get(Integer id) {
        return customerDao.findById(id);
    }

    @Override
    @Transactional
    public Customer saveOrUpdate(Customer customer) {
        return customerDao.saveOrUpdate(customer);
    }

    /**
     * @see CustomerService#getBalance(Integer)
     */
    @Override
    public double getBalance(Integer id) {

        Customer customer = customerDao.findById(id);

        if (customer == null) {
            throw new IllegalArgumentException(CUSTOMER_NOT_FOUND);
        }

        List<Account> accounts = customer.getAccounts();

        double balance = 0;
        for (Account account : accounts) {
            balance += account.getBalance();
        }

        return balance;
    }

    /**
     * @see CustomerService#delete(Integer)
     */
    @Transactional
    @Override
    public void delete(Integer id) {
        customerDao.delete(id);
    }

    /**
     * @see CustomerService#list()
     */
    @Override
    public List<Customer> list() {
        return customerDao.findAll();
    }

    /**
     * @see CustomerService#listRecipients(Integer)
     */
    @Transactional(readOnly = true)
    @Override
    public List<Recipient> listRecipients(Integer id) {

        // check then act logic requires transaction,
        // event if read only
        Customer customer = customerDao.findById(id);

        if (customer == null) {
            throw new IllegalArgumentException(CUSTOMER_NOT_FOUND);
        }

        return new ArrayList<>(customerDao.findById(id).getRecipients());
    }

    /**
     * @see CustomerService#removeRecipient(Integer, Integer)
     */
    @Transactional
    @Override
    public void removeRecipient(Integer id, Integer recipientId) {

        Customer customer = customerDao.findById(id);

        Recipient recipient = recipientDao.findById(recipientId);

        if (customer == null) {
            throw new IllegalArgumentException(CUSTOMER_NOT_FOUND);
        }

        if (recipient == null) {
            throw new IllegalArgumentException(RECIPIENT_NOT_FOUND);
        }

        if (!recipient.getCustomer().getId().equals(id)) {
            throw new IllegalArgumentException(CUSTOMER_RECIPIENT_NOT_FOUND);
        }

        customer.removeRecipient(recipient);
        customerDao.saveOrUpdate(customer);
    }
}
