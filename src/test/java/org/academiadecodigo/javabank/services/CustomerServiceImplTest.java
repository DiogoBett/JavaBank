package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.model.Customer;
import org.academiadecodigo.javabank.model.Recipient;
import org.academiadecodigo.javabank.model.account.Account;
import org.academiadecodigo.javabank.model.account.CheckingAccount;
import org.academiadecodigo.javabank.persistence.TransactionException;
import org.academiadecodigo.javabank.persistence.TransactionManager;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    private static final double DOUBLE_PRECISION = 0.1;

    private TransactionManager tx;
    private CustomerDao customerDao;
    private CustomerServiceImpl customerService;

    @Before
    public void setup() {

        tx = mock(TransactionManager.class);
        customerDao = mock(CustomerDao.class);

        customerService = new CustomerServiceImpl();
        customerService.setTransactionManager(tx);
        customerService.setCustomerDao(customerDao);

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
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();
        assertEquals(fakeCustomer, customer);
    }

    @Test(expected = TransactionException.class)
    public void testGetFail() {

        // setup
        doThrow(new TransactionException(new RuntimeException())).when(customerDao).findById(anyInt());

        // exercise
        customerService.get(1);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();

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
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();
        assertEquals(a1.getBalance() + a2.getBalance(), result, DOUBLE_PRECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBalanceInvalidCustomer() {

        // setup
        when(customerDao.findById(anyInt())).thenReturn(null);

        // exercise
        customerService.getBalance(1);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();
    }

    @Test(expected = TransactionException.class)
    public void testGetBalanceFail() {

        // setup
        doThrow(new TransactionException(new RuntimeException())).when(customerDao).findById(anyInt());

        // exercise
        customerService.getBalance(1);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();

    }

    @Test
    public void testListCustomerAccountIds() {

        // setup
        int fakeId = 9999;
        Account a1 = new CheckingAccount();
        Account a2 = new CheckingAccount();
        a1.credit(100);
        a1.setId(1);
        a2.credit(200);
        a2.setId(2);
        Customer fakeCustomer = new Customer();
        fakeCustomer.getAccounts().add(a1);
        fakeCustomer.getAccounts().add(a2);
        when(customerDao.findById(fakeId)).thenReturn(fakeCustomer);

        // exercise
        Set<Integer> accountIds = customerService.listCustomerAccountIds(fakeId);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();
        assertNotNull(accountIds);
        assertEquals(fakeCustomer.getAccounts().size(), accountIds.size());
        assertTrue(accountIds.contains(a1.getId()));
        assertTrue(accountIds.contains(a2.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCustomerAccountIdsInvalidId() {

        // setup
        when(customerDao.findById(anyInt())).thenReturn(null);

        // exercise
        customerService.listCustomerAccountIds(1);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();

    }

    @Test(expected = TransactionException.class)
    public void testListCustomerAccountIdsFail() {

        // setup
        doThrow(new TransactionException(new RuntimeException())).when(customerDao).findById(anyInt());

        // exercise
        customerService.listCustomerAccountIds(1);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();

    }

    @Test
    public void testListRecipients() {

        // setup
        int fakeId = 9999;
        Recipient r1 = new Recipient();
        Recipient r2 = new Recipient();
        Customer fakeCustomer = new Customer();
        fakeCustomer.addRecipient(r1);
        fakeCustomer.addRecipient(r2);
        when(customerDao.findById(fakeId)).thenReturn(fakeCustomer);

        // exercise
        List<Recipient> recipients = customerService.listRecipients(fakeId);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();
        assertNotNull(recipients);
        assertEquals(fakeCustomer.getRecipients().size(), recipients.size());
        assertTrue(recipients.contains(r1));
        assertTrue(recipients.contains(r2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListRecipientsInvalidId() {

        // setup
        when(customerDao.findById(anyInt())).thenReturn(null);

        // exercise
        customerService.listRecipients(1);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();
    }

    @Test(expected = TransactionException.class)
    public void testListRecipientsFails() {

        // setup
        doThrow(new TransactionException(new RuntimeException())).when(customerDao).findById(anyInt());

        // exercise
        customerService.listRecipients(1);

        // verify
        verify(tx, times(1)).beginRead();
        verify(tx, times(1)).commit();
    }
}
