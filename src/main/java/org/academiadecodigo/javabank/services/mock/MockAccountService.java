package org.academiadecodigo.javabank.services.mock;

import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.AccountService;

/**
 * A mock {@link AccountService} implementation
 */
public class MockAccountService extends AbstractMockService<Account> implements AccountService {

    /**
     * @see AccountService#get(Integer)
     */
    @Override
    public Account get(Integer id) {
        return modelMap.get(id);
    }

    /**
     * @see AccountService#deposit(Integer, double)
     */
    public void deposit(Integer id, double amount) {
        modelMap.get(id).credit(amount);
    }

    /**
     * @see AccountService#withdraw(Integer, double)
     */
    public void withdraw(Integer id, double amount) {

        Account account = modelMap.get(id);

        if (!account.canWithdraw()) {
            return;
        }

        modelMap.get(id).debit(amount);
    }

    /**
     * @see AccountService#transfer(Integer, Integer, double)
     */
    public void transfer(Integer srcId, Integer dstId, double amount) {

        Account srcAccount = modelMap.get(srcId);
        Account dstAccount = modelMap.get(dstId);

        // make sure transaction can be performed
        if (srcAccount.canDebit(amount) && dstAccount.canCredit(amount)) {
            srcAccount.debit(amount);
            dstAccount.credit(amount);
        }
    }
}
