package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.dto.AccountDTO;
import org.academiadecodigo.javabank.dto.CustomerDTO;
import org.academiadecodigo.javabank.dto.converters.AccountConverter;
import org.academiadecodigo.javabank.dto.converters.CustomerConverter;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller responsible for rendering {@link Customer} related views
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;
    private CustomerConverter customerConverter;
    private AccountConverter accountConverter;

    /**
     * Sets the customer service
     *
     * @param customerService the customer service to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setCustomerConverter(CustomerConverter customerConverter) {
        this.customerConverter = customerConverter;
    }

    @Autowired
    public void setAccountConverter(AccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    /**
     * Renders a view with a list of customers
     *
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/list", "/", ""})
    public String listCustomers(Model model) {

        List<CustomerDTO> customerDTOS = new LinkedList<>();

        for (Customer customer : customerService.list()) {
            customerDTOS.add(customerConverter.customerToDTO(customer));
        }

        model.addAttribute("customers", customerDTOS);
        return "customer/list";
    }

    /**
     * Renders a view with customer details
     *
     * @param id    the customer id
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public String showCustomer(@PathVariable Integer id, Model model) {

        Customer customer = customerService.get(id);
        List<AccountDTO> accountDTOS = new LinkedList<>();

        for (Account account : customer.getAccounts()) {
            accountDTOS.add(accountConverter.accountToAccountDTO(account));
        }

        model.addAttribute("customer", customerConverter.customerToDTO(customer));
        model.addAttribute("accounts", accountDTOS);
        model.addAttribute("recipients", customerService.listRecipients(id));

        return "customer/show";
    }

    /**
     * Deletes a customer
     *
     * @param id the customer id
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "{id}/delete")
    public String deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
        return "redirect:/customer/list";
    }

    /**
     * Deletes a recipient from a customer
     *
     * @param cid the customer id
     * @param rid the recipient id
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/{rid}/delete/")
    public String deleteRecipient(@PathVariable Integer cid, @PathVariable Integer rid) {
        customerService.removeRecipient(cid, rid);
        return "redirect:/customer/" + cid;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/add")
    public String addCustomer(Model model) {

        CustomerDTO customerDTO = new CustomerDTO();
        model.addAttribute("customer", customerDTO);

        return "customer/details";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/edit")
    public String editCustomer(Model model, @PathVariable Integer id) {

        model.addAttribute("customer", customerConverter.customerToDTO(customerService.get(id)));

        return "customer/details";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addCustomer")
    public String processData(@ModelAttribute(value = "customer") CustomerDTO customerDTO) {

        Customer customer = null;

        if (customerDTO.getId() == 0) {
            customer = customerService.saveOrUpdate(customerConverter.dtoToCustomer(customerDTO));
            return "redirect:/customer/" + customer.getId();
        }

        customer = customerService.get(customerDTO.getId());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customerService.saveOrUpdate(customer);

        return "redirect:/customer/" + customer.getId();
    }

}
