package org.academiadecodigo.javabank.services.jpa;

import org.academiadecodigo.javabank.model.account.Account;
import org.academiadecodigo.javabank.model.account.AccountType;
import org.academiadecodigo.javabank.model.account.CheckingAccount;
import org.academiadecodigo.javabank.persistence.JpaIntegrationTestHelper;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.*;

public class JpaAccountServiceIntegrationTest extends JpaIntegrationTestHelper {

    private final static Integer INVALID_ID = 9999;
    private final static double DOUBLE_DELTA = 0.1;
    private JpaAccountService as;

    @Before
    public void setup() {
        as = new JpaAccountService(emf);
    }


    @Test
    public void testGet() {

        // setup
        int id = 1;

        // exercise
        Account account = as.get(id);

        // verify
        assertNotNull("Account is null", account);
        assertEquals("Account id is wrong", id, account.getId().intValue());
        assertEquals("Account type is wrong", AccountType.CHECKING.toString(), account.getAccountType().toString());
        assertEquals("Account balance is wrong", 100, account.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testGetFail() {

        // exercise
        Account account = as.get(INVALID_ID);

        // verify
        assertNull("Account should be null", account);
    }

    @Test
    public void testList() {

        // exercise
        List<Account> accounts = as.list();

        // verify
        assertNotNull("Accounts are null", accounts);
        assertEquals("Number of accounts is wrong", 7, accounts.size());

    }

    @Test
    public void testListFail() {

        // setup
        em.getTransaction().begin();
        Query query = em.createQuery("delete from Account ");
        query.executeUpdate();
        query = em.createQuery("delete from Customer");
        query.executeUpdate();
        em.getTransaction().commit();

        // exercise
        List<Account> accounts = as.list();

        // verify
        assertNotNull("Accounts are null", accounts);
        assertEquals("Number of accounts is wrong", 0, accounts.size());

    }

    @Test
    public void testDeleteAccountOwned() {

        // setup
        int id = 1;

        // exercise
        as.delete(id);

        // verify
        Account account = em.find(Account.class, id);
        assertNotNull("Account owned by customer should not be deleted", account);
    }

    @Test
    public void testDeleteOrphanAccount() {

        // setup
        int id = 7;

        // exercise
        as.delete(id);

        // verify
        Account account = em.find(Account.class, id);
        assertNull("Account is not null", account);

    }

    @Test
    public void testSave() {

        // setup
        Account newAccount = new CheckingAccount();

        // exercise
        Account addedAccount = as.save(newAccount);

        // verify
        assertNotNull("Account not added", addedAccount);
        Account account = em.find(Account.class, addedAccount.getId());
        assertNotNull("Account not found", account);

    }

    @Test
    public void testUpdateAccount() {

        // setup
        int id = 1;
        Account account = em.find(Account.class, id);
        account.credit(100);

        // exercise
        as.save(account);

        // verify
        account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", 200, account.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testDepositCheckingAccount() {

        // setup
        int id = 1;

        // exercise
        as.deposit(id, 100);

        // verify
        Account account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", 200, account.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testDepositSavingsAccount() {

        // setup
        int id = 2;

        // exercise
        as.deposit(id, 100);

        // verify
        Account account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", 150.5, account.getBalance(), DOUBLE_DELTA);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testDepositInvalidAccount() {
        as.deposit(INVALID_ID, 100);
    }

    @Test
    public void testWithdrawCheckingAccount() {

        // setup
        int id = 1;
        double amount = 50;

        // exercise
        as.withdraw(id, amount);

        // verify
        Account account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", amount, account.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testEmptyCheckingAccount() {

        // setup
        int id = 1;

        // exercise
        as.withdraw(id, 100);

        // verify
        Account account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", 0, account.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testWithdrawCheckingInsufficientFunds() {

        // setup
        int id = 1;

        // exercise
        as.withdraw(id, 150);

        // verify
        Account account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", 100, account.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testWithdrawSavingsAccount() {

        // setup
        int id = 4;

        // exercise
        as.withdraw(id, 20);

        // verify
        Account account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", 130, account.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testWithdrawMaxSavingsAccount() {

        // setup
        int id = 4;

        // exercise
        as.withdraw(id, 50);

        // verify
        Account account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", 100, account.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testWithdrawSavingsAccountInsufficientFunds() {

        // setup
        int id = 4;

        // exercise
        as.withdraw(id, 100);

        // verify
        Account account = em.find(Account.class, id);
        assertEquals("Account balance is wrong", 150, account.getBalance(), DOUBLE_DELTA);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawInvalidAccount() {
        as.withdraw(INVALID_ID, 100);
    }

    @Test
    public void testTransfer() {

        // exercise
        as.transfer(1, 2, 10.5);

        // verify
        Account srcAccount = em.find(Account.class, 1);
        Account dstAccount = em.find(Account.class, 2);
        assertEquals("Source Account balance is wrong", 89.5, srcAccount.getBalance(), DOUBLE_DELTA);
        assertEquals("Destination Account balance is wrong", 61, dstAccount.getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testTransferInsufficientFunds() {

        // exercise
        as.transfer(1, 2, 200);

        // verify
        Account srcAccount = em.find(Account.class, 1);
        Account dstAccount = em.find(Account.class, 2);
        assertEquals("Source Account balance is wrong", 100, srcAccount.getBalance(), DOUBLE_DELTA);
        assertEquals("Destination Account balance is wrong", 50.5, dstAccount.getBalance(), DOUBLE_DELTA);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferInvalidSrcAccount() {
        as.transfer(INVALID_ID, 2, 200);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferInvalidDstAccount() {
        as.transfer(1, INVALID_ID, 200);
    }
}
