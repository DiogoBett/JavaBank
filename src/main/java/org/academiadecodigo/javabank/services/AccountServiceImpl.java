package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * An {@link AccountService} implementation
 */
@Service
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

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
     * @see AccountService#get(Integer)
     */
    @Override
    public Account get(Integer id) {
        return accountDao.findById(id);
    }

    @Override
    public List<Account> getAccounts(Integer customerId) {

        List<Account> accounts = new LinkedList<>();

        for (Account account : accountDao.findAll()) {
            if (account.getCustomer().getId() == customerId) {
                accounts.add(account);
            }
        }

        if(accounts.isEmpty()) {
            return null;
        }

        return accounts;
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        accountDao.delete(id);
    }

    /**
     * @see AccountService#deposit(Integer, double)
     */
    @Transactional
    @Override
    public void deposit(Integer id, double amount) throws AccountNotFoundException {

        Account account = accountDao.findById(id);

        if (account == null) {
            throw new AccountNotFoundException();
        }

        account.credit(amount);

        accountDao.saveOrUpdate(account);
    }

    /**
     * @see AccountService#withdraw(Integer, double)
     */
    @Transactional
    @Override
    public void withdraw(Integer id, double amount) throws AccountNotFoundException {

        Account account = accountDao.findById(id);

        if (account == null) {
            throw new AccountNotFoundException();
        }

        account.debit(amount);

        accountDao.saveOrUpdate(account);
    }

    /**
     * @see AccountService#transfer(Integer, Integer, double)
     */
    @Transactional
    @Override
    public void transfer(Integer srcId, Integer dstId, double amount) throws AccountNotFoundException {

        Account srcAccount = accountDao.findById(srcId);
        Account dstAccount = accountDao.findById(dstId);

        if (srcAccount == null || dstAccount == null) {
            throw new AccountNotFoundException();
        }

        // make sure transaction can be performed
        if (srcAccount.canDebit(amount) && dstAccount.canCredit(amount)) {
            srcAccount.debit(amount);
            dstAccount.credit(amount);
        }

        accountDao.saveOrUpdate(srcAccount);
        accountDao.saveOrUpdate(dstAccount);
    }
}


