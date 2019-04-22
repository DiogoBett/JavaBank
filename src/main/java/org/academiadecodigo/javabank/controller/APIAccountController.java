package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.converters.AccountToAccountDto;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.AccountService;
import org.academiadecodigo.javabank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class APIAccountController {

    private AccountService accountService;
    private CustomerService customerService;

    private AccountToAccountDto accountToAccountDto;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setAccountToAccountDto(AccountToAccountDto accountToAccountDto) {
        this.accountToAccountDto = accountToAccountDto;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/accounts")
    public ResponseEntity<List<AccountDto>> getCustomerAccounts(@PathVariable Integer id) {

        Customer customer = customerService.get(id);

        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountToAccountDto.convert(customer.getAccounts()), HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/{cid}/accounts/{aid}")
    public ResponseEntity<AccountDto> getCustomerAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        Account account = customerService.getAccount(cid, aid);

        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountToAccountDto.convert(account), HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{cid}/accounts/{aid}")
    public ResponseEntity<String> deleteCustomerAccount(@PathVariable Integer cid, @PathVariable Integer aid) {

        Account account = customerService.getAccount(cid, aid);

        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        accountService.delete(aid);

        return new ResponseEntity<>("Account has been Deleted", HttpStatus.OK);

    }

}
