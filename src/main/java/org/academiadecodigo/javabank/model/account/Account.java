package org.academiadecodigo.javabank.model.account;

import org.academiadecodigo.javabank.model.AbstractModel;
import org.academiadecodigo.javabank.model.Customer;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
public abstract class Account extends AbstractModel {

    private double balance = 0;

    @ManyToOne
    private Customer customer;

    public void credit(double amount) {
        if (canCredit(amount)) {
            balance += amount;
        }
    }

    public void debit(double amount) {
        if (canDebit(amount)) {
            balance -= amount;
        }
    }

    public double getBalance() {
        return balance;
    }

    public abstract AccountType getAccountType();

    public boolean canDebit(double amount) {
        return amount > 0 && amount <= balance;
    }

    public boolean canCredit(double amount) {
        return amount > 0;
    }

    public boolean canWithdraw() {
        return true;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", customerId=" + (customer != null ? customer.getId() : null) +
                "} " + super.toString();
    }
}
