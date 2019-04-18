package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.model.account.AccountType;
import org.academiadecodigo.javabank.persistence.model.account.CheckingAccount;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class AccountToAccountDtoTest {

    private AccountToAccountDto accountToAccountDto;

    @Before
    public void setup() {
        accountToAccountDto = new AccountToAccountDto();
    }

    @Test
    public void testConvert() {

        // setup
        int fakeAccountId = 9999;
        double fakeAccountBalance = 1000.00;

        Account fakeAccount = spy(CheckingAccount.class);
        fakeAccount.setId(fakeAccountId);
        fakeAccount.credit(fakeAccountBalance);

        // exercise
        AccountDto accountDto = accountToAccountDto.convert(fakeAccount);

        // verify
        verify(fakeAccount).getId();
        verify(fakeAccount).getAccountType();
        verify(fakeAccount).getBalance();

        assertTrue(fakeAccountId == accountDto.getId());
        assertEquals(fakeAccount.getAccountType(), accountDto.getType());
        assertEquals(fakeAccountBalance, accountDto.getBalance());
    }
}
