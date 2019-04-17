package org.academiadecodigo.javabank.dto;

import org.academiadecodigo.javabank.persistence.model.account.AccountType;

public class AccountDTO {

    private Integer id;
    private double balance;
    private AccountType accountType;

    public AccountDTO() {
        this.id = 0;
        this.balance = 0;
        this.accountType = AccountType.CHECKING;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
