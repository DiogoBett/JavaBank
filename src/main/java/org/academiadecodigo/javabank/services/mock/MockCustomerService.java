package org.academiadecodigo.javabank.services.mock;

import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.CustomerService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A mock {@link CustomerService} implementation
 */
public class MockCustomerService extends AbstractMockService<Customer> implements CustomerService {

    /**
     * @see CustomerService#get(Integer)
     */
    @Override
    public Customer get(Integer id) {
        return modelMap.get(id);
    }

    @Override
    public void removeCustomer(Integer id) {
        modelMap.remove(get(id));
    }

    @Override
    public Customer add(Customer customer) {
        modelMap.put(customer.getId(), customer);
        return customer;
    }

    /**
     * @see CustomerService#getBalance(Integer)
     */
    @Override
    public double getBalance(Integer customerId) {

        List<Account> accounts = modelMap.get(customerId).getAccounts();

        double balance = 0;
        for (Account account : accounts) {
            balance += account.getBalance();
        }

        return balance;

    }

    /**
     * @see CustomerService#list()
     */
    @Override
    public List<Customer> list() {
        return new ArrayList<>(modelMap.values());
    }

    /**
     * @see CustomerService#listCustomerAccountIds(Integer)
     */
    @Override
    public Set<Integer> listCustomerAccountIds(Integer id) {

        Set<Integer> accountIds = new HashSet<>();
        List<Account> accounts = modelMap.get(id).getAccounts();

        for (Account account : accounts) {
            accountIds.add(account.getId());
        }

        return accountIds;
    }

    /**
     * @see CustomerService#listRecipients(Integer)
     */
    @Override
    public List<Recipient> listRecipients(Integer id) {
        return modelMap.get(id).getRecipients();
    }
}
