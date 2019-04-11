package org.academiadecodigo.javabank.model.account;

import javax.persistence.Entity;

@Entity
public class SavingsAccount extends Account {

    public static final double MIN_BALANCE = 100;

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

    @Override
    public boolean canDebit(double amount) {
        return super.canDebit(amount) && (getBalance() - amount) >= MIN_BALANCE;
    }

    @Override
    public boolean canWithdraw() {
        return false;
    }
}
