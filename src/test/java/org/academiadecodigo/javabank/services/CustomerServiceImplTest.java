package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.model.account.CheckingAccount;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.academiadecodigo.javabank.persistence.dao.RecipientDao;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    private static final double DOUBLE_PRECISION = 0.1;

    private CustomerDao customerDao;
    private CustomerServiceImpl customerService;
    private RecipientDao recipientDao;
    private AccountDao accountDao;

    @Before
    public void setup() {

        customerDao = mock(CustomerDao.class);
        recipientDao = mock(RecipientDao.class);
        accountDao = mock(AccountDao.class);

        customerService = new CustomerServiceImpl();
        customerService.setCustomerDao(customerDao);
        customerService.setRecipientDao(recipientDao);
        customerService.setAccountDao(accountDao);
    }

    @Test
    public void testGet() {

        // setup
        int fakeId = 9999;
        Customer fakeCustomer = new Customer();
        when(customerDao.findById(fakeId)).thenReturn(fakeCustomer);

        // exercise
        Customer customer = customerService.get(fakeId);

        // verify
        assertEquals(fakeCustomer, customer);
    }

    @Test
    public void testGetBalance() {

        // setup
        int fakeId = 9999;
        Account a1 = new CheckingAccount();
        Account a2 = new CheckingAccount();
        a1.credit(100);
        a2.credit(200);
        Customer fakeCustomer = new Customer();
        fakeCustomer.getAccounts().add(0, a1);
        fakeCustomer.getAccounts().add(1, a2);
        when(customerDao.findById(fakeId)).thenReturn(fakeCustomer);

        // exercise
        double result = customerService.getBalance(fakeId);

        // verify
        assertEquals(a1.getBalance() + a2.getBalance(), result, DOUBLE_PRECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBalanceInvalidCustomer() {

        // setup
        when(customerDao.findById(anyInt())).thenReturn(null);

        // exercise
        customerService.getBalance(1);

    }

    @Test
    public void testListRecipients() {

        // setup
        int fakeCustomerId = 9999;
        Customer fakeCustomer = new Customer();
        Recipient fakeRecipient = new Recipient();
        fakeCustomer.getRecipients().add(fakeRecipient);
        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);

        // exercise
        List<Recipient> recipients = customerService.listRecipients(fakeCustomerId);

        // verify
        assertNotNull(recipients);
        assertTrue(recipients.contains(fakeRecipient));
        assertEquals(1, recipients.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListRecipientsInvalidCustomerId() {

        // setup
        when(customerDao.findById(anyInt())).thenReturn(null);

        // exercise
        customerService.listRecipients(1);
    }

    @Test
    public void testRemoveRecipient() {

        // setup
        int fakeCustomerId = 9999;
        int fakeRecipientId = 8888;
        int fakeRecipient2Id = 7777;

        Customer fakeCustomer = spy(new Customer());
        Recipient fakeRecipient = new Recipient();
        Recipient fakeRecipient2 = new Recipient();
        fakeRecipient.setCustomer(fakeCustomer);
        fakeRecipient2.setCustomer(fakeCustomer);
        fakeCustomer.getRecipients().add(fakeRecipient);
        fakeCustomer.getRecipients().add(fakeRecipient2);
        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(fakeCustomer.getId()).thenReturn(fakeCustomerId);
        when(recipientDao.findById(fakeRecipientId)).thenReturn(fakeRecipient);
        when(recipientDao.findById(fakeRecipient2Id)).thenReturn(fakeRecipient2);

        // exercise
        customerService.removeRecipient(fakeCustomerId, fakeRecipientId);
        List<Recipient> list = customerDao.findById(fakeCustomerId).getRecipients();

        // verify
        assertFalse(list.isEmpty());
        assertFalse(list.contains(fakeRecipient));
        assertTrue(list.contains(fakeRecipient2));
        assertEquals(1, list.size());

        verify(customerDao, times(1)).saveOrUpdate(fakeCustomer);
        verify(fakeCustomer, times(1)).removeRecipient(fakeRecipient);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRecipientInvalidCustomerId() {

        // setup
        int fakeRecipientId = 8888;
        Recipient fakeRecipient = new Recipient();
        when(customerDao.findById(anyInt())).thenReturn(null);
        when(recipientDao.findById(fakeRecipientId)).thenReturn(fakeRecipient);

        // exercise
        customerService.removeRecipient(1, fakeRecipientId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRecipientInvalidRecipientId() {

        // setup
        int fakeCustomerId = 9999;
        Customer fakeCustomer = new Customer();
        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(recipientDao.findById(anyInt())).thenReturn(null);

        // exercise
        customerService.removeRecipient(fakeCustomerId, 1);
    }
}
