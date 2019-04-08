package org.academiadecodigo.javabank.services.jpa;

import org.academiadecodigo.javabank.model.Customer;
import org.academiadecodigo.javabank.model.account.Account;
import org.academiadecodigo.javabank.model.account.CheckingAccount;
import org.academiadecodigo.javabank.model.account.SavingsAccount;
import org.academiadecodigo.javabank.persistence.JpaIntegrationTestHelper;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class JpaCustomerServiceIntegrationTest extends JpaIntegrationTestHelper {

    private final static Integer INVALID_ID = 9999;
    private final static double DOUBLE_DELTA = 0.1;

    private JpaCustomerService cs;

    @Before
    public void setUp() {

        cs = new JpaCustomerService(emf);
    }

    @Test
    public void testGetBalance() {

        // exercise
        double result = cs.getBalance(1);

        // verify
        assertEquals("The balance is different from what was expected", 150.5, result, DOUBLE_DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBalanceInvalidCustomer() {

        cs.getBalance(INVALID_ID);

    }

    @Test
    public void testListCustomerAccountIds() {

        // exercise
        Set<Integer> ids = cs.listCustomerAccountIds(1);

        // verify
        assertNotNull("Set is null", ids);
        assertEquals("Not the number of users expected", 2, ids.size());
        assertEquals("It should not be empty", false, ids.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCustomerAccountIdsInvalidCustomer() {

        cs.listCustomerAccountIds(INVALID_ID);
    }

    @Test
    public void testGet() {

        // setup
        int id = 1;

        // exercise
        Customer customer = cs.get(id);

        // verify
        assertNotNull("Customer is null", customer);
        assertEquals("Customer id is wrong", id, customer.getId().intValue());
        assertEquals("Customer name is wrong", "Rui", customer.getName());

    }

    @Test()
    public void testGetInvalidCustomer() {
        Customer customer = cs.get(INVALID_ID);
        assertNull("invalid customer should not be found", customer);
    }

    @Test
    public void testList() {

        // exercise
        List<Customer> customers = cs.list();

        // verify
        assertNotNull("customers are null", customers);
        assertEquals("Number of customer is wrong", 4, customers.size());

    }

    @Test
    public void testDelete() {

        // setup
        int id = 1;

        // exercise
        cs.delete(id);

        // verify
        Customer customer = em.find(Customer.class, id);
        assertNull("should be null", customer);
    }

    @Test
    public void testDeleteNoAccounts() {

        // setup
        int id = 4;

        // exercise
        cs.delete(id);

        // verify
        Customer customer = em.find(Customer.class, id);
        assertNull("should be null", customer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteCustomerInvalidCustomer() {

        cs.delete(INVALID_ID);
    }

    @Test
    public void testAddNoAccounts() {

        // setup
        String name = "New Customer name";
        Customer newCustomer = new Customer();
        newCustomer.setName(name);

        // exercise
        Customer addedCustomer = cs.save(newCustomer);

        // verify
        assertNotNull("customer not added", addedCustomer);
        Customer customer = em.find(Customer.class, addedCustomer.getId());
        assertNotNull("Customer not found", customer);

    }

    @Test
    public void testAddWithAccounts() {

        // setup
        double caBalance = 100;
        double saBalance = 101;
        Account ca = new CheckingAccount();
        Account sa = new SavingsAccount();
        ca.credit(caBalance);
        sa.credit(saBalance);

        Customer newCustomer = new Customer();
        newCustomer.addAccount(ca);
        newCustomer.addAccount(sa);

        // exercise
        Customer addedCustomer = cs.save(newCustomer);

        // verify
        assertNotNull("customer not added", addedCustomer);
        Customer customer = em.find(Customer.class, addedCustomer.getId());
        assertNotNull("customer not found", addedCustomer);
        assertNotNull("customer accounts not found", customer.getAccounts());
        assertEquals("customer number of accounts wrong", newCustomer.getAccounts().size(), customer.getAccounts().size());
        assertEquals("first account balance is wrong", caBalance, customer.getAccounts().get(0).getBalance(), DOUBLE_DELTA);
        assertEquals("second account balance is wrong", saBalance, customer.getAccounts().get(1).getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testUpdate() {

        // setup
        int id = 1;
        String name = "updated customer";
        Customer customer = em.find(Customer.class, id);
        customer.setName(name);

        // exercise
        cs.save(customer);

        // verify
        customer = em.find(Customer.class, id);
        assertEquals("customer name is wrong", name, customer.getName());

    }

    @Test
    public void testUpdateWithAccounts() {

        // setup
        int id = 1;
        String name = "updated customer";
        Customer existingCustomer = em.find(Customer.class, id);
        existingCustomer.setName(name);
        existingCustomer.getAccounts().get(0).canCredit(100);

        // exercise
        cs.save(existingCustomer);

        // verify
        Customer customer = em.find(Customer.class, id);
        assertEquals("customer name is wrong", name, customer.getName());
        assertEquals("number of accounts is wrong", 2, customer.getAccounts().size());
        assertEquals("account balance is wrong", 100, customer.getAccounts().get(0).getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testUpdateOrphanAccountDelete() {

        // setup
        int id = 1;
        String name = "updated customer";
        Customer existingCustomer = em.find(Customer.class, id);
        existingCustomer.setName(name);
        existingCustomer.removeAccount(existingCustomer.getAccounts().get(1));

        // exercise
        cs.save(existingCustomer);

        // verify
        Customer customer = em.find(Customer.class, id);
        assertEquals("customer name is wrong", name, customer.getName());
        assertEquals("number of accounts is wrong", 1, customer.getAccounts().size());
        assertEquals("account balance is wrong", 100, customer.getAccounts().get(0).getBalance(), DOUBLE_DELTA);

    }
}
