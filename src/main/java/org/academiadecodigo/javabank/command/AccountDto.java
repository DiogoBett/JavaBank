package org.academiadecodigo.javabank.command;

import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.model.account.AccountType;

/**
 * The {@link Account} data transfer object
 */
public class AccountDto {

    private Integer id;
    private Double balance;
    private AccountType type;
    private Integer customerId;

    /**
     * Gets the id of the account dto
     *
     * @return the account dto id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the account dto
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the initial amount of the account dto
     *
     * @return the initial amount
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Sets the initial amount of the account dto
     *
     * @param balance the amount to set
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * Gets the type of the account dto
     *
     * @return the account type
     */
    public AccountType getType() {
        return type;
    }

    /**
     * Sets the account dto type
     *
     * @param type the account type to set
     */
    public void setType(AccountType type) {
        this.type = type;
    }

    /**
     * Gets the account dto customer id
     *
     * @return the customer id
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Sets the account dto customer id
     *
     * @param customerId the customer id to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
