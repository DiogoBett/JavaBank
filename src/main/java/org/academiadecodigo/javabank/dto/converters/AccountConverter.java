package org.academiadecodigo.javabank.dto.converters;

import org.academiadecodigo.javabank.dto.AccountDTO;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.model.account.AccountType;
import org.academiadecodigo.javabank.persistence.model.account.CheckingAccount;
import org.academiadecodigo.javabank.persistence.model.account.SavingsAccount;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter {

    public Account accountDTOToAccount(AccountDTO accountDTO) {

        Account account = null;

        if(accountDTO.getAccountType() == AccountType.CHECKING) {
            account = new CheckingAccount();
        }

        if(accountDTO.getAccountType() == AccountType.SAVINGS) {
            account = new SavingsAccount();
        }

        account.setId(accountDTO.getId());
        account.setBalance(accountDTO.getBalance());

        return account;
    }

    public AccountDTO accountToAccountDTO(Account account) {

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setId(account.getId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setAccountType(account.getAccountType());

        return accountDTO;
    }
}
