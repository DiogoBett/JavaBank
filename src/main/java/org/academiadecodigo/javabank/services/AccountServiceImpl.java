package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * An {@link AccountService} implementation
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    /**
     * Sets the account data access object
     *
     * @param accountDao the account DAO to set
     */
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

    /**
     * @see AccountService#add(Account)
     */
    @Transactional
    @Override
    public Integer add(Account account) {
        return accountDao.saveOrUpdate(account).getId();
    }

    /**
     * @see AccountService#deposit(Integer, double)
     */
    @Transactional
    @Override
    public void deposit(Integer id, double amount) {

        Account account = accountDao.findById(id);

        if (account == null) {
            throw new IllegalArgumentException("invalid account id");
        }

        account.credit(amount);

        accountDao.saveOrUpdate(account);
    }

    /**
     * @see AccountService#withdraw(Integer, double)
     */
    @Transactional
    @Override
    public void withdraw(Integer id, double amount) {

        Account account = accountDao.findById(id);

        if (account == null) {
            throw new IllegalArgumentException("invalid account id");
        }

        if (!account.canWithdraw()) {
            throw new IllegalArgumentException("invalid account type");
        }

        account.debit(amount);

        accountDao.saveOrUpdate(account);
    }

    /**
     * @see AccountService#transfer(Integer, Integer, double)
     */
    @Transactional
    @Override
    public void transfer(Integer srcId, Integer dstId, double amount) {

        Account srcAccount = accountDao.findById(srcId);
        Account dstAccount = accountDao.findById(dstId);

        if (srcAccount == null || dstAccount == null) {
            throw new IllegalArgumentException("invalid account id");
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


