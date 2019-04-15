package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.TransactionException;
import org.academiadecodigo.javabank.persistence.TransactionManager;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;

/**
 * An {@link AccountService} implementation
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;
    private TransactionManager tx;

    /**
     * Sets the account data access object
     *
     * @param accountDao the account DAO to set
     */
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
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
     * @see AccountService#get(Integer)
     */
    @Override
    public Account get(Integer id) {

        try {

            tx.beginRead();
            return accountDao.findById(id);

        } finally {
            tx.commit();
        }
    }

    /**
     * @see AccountService#add(Account)
     */
    @Override
    public Integer add(Account account) {

        Integer id = null;

        try {

            tx.beginWrite();

            id = accountDao.saveOrUpdate(account).getId();

            tx.commit();


        } catch (TransactionException ex) {

            tx.rollback();
        }

        return id;
    }

    /**
     * @see AccountService#deposit(Integer, double)
     */
    @Override
    public void deposit(Integer id, double amount) {

        try {

            tx.beginWrite();

            Account account = accountDao.findById(id);

            if (account == null) {
                tx.rollback();
                throw new IllegalArgumentException("invalid account id");
            }

            account.credit(amount);

            accountDao.saveOrUpdate(account);

            tx.commit();

        } catch (TransactionException ex) {

            tx.rollback();
        }
    }

    /**
     * @see AccountService#withdraw(Integer, double)
     */
    @Override
    public void withdraw(Integer id, double amount) {

        try {

            tx.beginWrite();

            Account account = accountDao.findById(id);

            if (account == null) {
                tx.rollback();
                throw new IllegalArgumentException("invalid account id");
            }

            if (!account.canWithdraw()) {
                tx.rollback();
                throw new IllegalArgumentException("invalid account type");
            }

            account.debit(amount);

            accountDao.saveOrUpdate(account);

            tx.commit();

        } catch (TransactionException ex) {

            tx.rollback();
        }
    }

    /**
     * @see AccountService#transfer(Integer, Integer, double)
     */
    @Override
    public void transfer(Integer srcId, Integer dstId, double amount) {

        try {

            tx.beginWrite();

            Account srcAccount = accountDao.findById(srcId);
            Account dstAccount = accountDao.findById(dstId);

            if (srcAccount == null || dstAccount == null) {
                tx.rollback();
                throw new IllegalArgumentException("invalid account id");
            }

            // make sure transaction can be performed
            if (srcAccount.canDebit(amount) && dstAccount.canCredit(amount)) {
                srcAccount.debit(amount);
                dstAccount.credit(amount);
            }

            accountDao.saveOrUpdate(srcAccount);
            accountDao.saveOrUpdate(dstAccount);

            tx.commit();

        } catch (TransactionException ex) {

            tx.rollback();
        }
    }
}


