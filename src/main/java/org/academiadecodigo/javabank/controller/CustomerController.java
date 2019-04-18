package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.command.CustomerDto;
import org.academiadecodigo.javabank.converters.AccountToAccountDto;
import org.academiadecodigo.javabank.converters.CustomerDtoToCustomer;
import org.academiadecodigo.javabank.converters.CustomerToCustomerDto;
import org.academiadecodigo.javabank.converters.RecipientToRecipientDto;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller responsible for rendering {@link Customer} related views
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    private CustomerToCustomerDto customerToCustomerDto;
    private CustomerDtoToCustomer customerDtoToCustomer;
    private RecipientToRecipientDto recipientToRecipientDto;
    private AccountToAccountDto accountToAccountDto;

    /**
     * Sets the customer service
     *
     * @param customerService the customer service to set
     */
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Sets the converter for converting between customer model objects and customer dto objects
     *
     * @param customerToCustomerDto the customer to customer dto converter to set
     */
    @Autowired
    public void setCustomerToCustomerDto(CustomerToCustomerDto customerToCustomerDto) {
        this.customerToCustomerDto = customerToCustomerDto;
    }

    /**
     * Sets the converter for converting between customer dto and customer model objects
     *
     * @param customerDtoToCustomer the customer dto to customer converter to set
     */
    @Autowired
    public void setCustomerDtoToCustomer(CustomerDtoToCustomer customerDtoToCustomer) {
        this.customerDtoToCustomer = customerDtoToCustomer;
    }

    /**
     * Sets the converter for converting between recipient model and recipient dto objects
     *
     * @param recipientToRecipientDto the recipient to recipient dto converter to set
     */
    @Autowired
    public void setRecipientToRecipientDto(RecipientToRecipientDto recipientToRecipientDto) {
        this.recipientToRecipientDto = recipientToRecipientDto;
    }

    /**
     * Sets the converter for converting between account model and account dto objects
     *
     * @param accountToAccountDto the account to account dto converter to set
     */
    @Autowired
    public void setAccountToAccountDto(AccountToAccountDto accountToAccountDto) {
        this.accountToAccountDto = accountToAccountDto;
    }

    /**
     * Renders a view with a list of customers
     *
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/list", "/", ""})
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerToCustomerDto.convert(customerService.list()));
        return "customer/list";
    }

    /**
     * Adds a customer
     *
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/add")
    public String addCustomer(Model model) {
        model.addAttribute("customer", new CustomerDto());
        return "customer/add-update";
    }

    /**
     * Edits a customer
     *
     * @param id    the customer id
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/edit")
    public String editCustomer(@PathVariable Integer id, Model model) {
        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(id)));
        return "customer/add-update";
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

        model.addAttribute("customer", customerToCustomerDto.convert(customer));
        model.addAttribute("accounts", accountToAccountDto.convert(customer.getAccounts()));
        model.addAttribute("recipients", recipientToRecipientDto.convert(customerService.listRecipients(id)));
        return "customer/show";
    }

    /**
     * Saves the customer form submission and renders a view with the customer details
     *
     * @param customerDto       the customer form object
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/", ""}, params = "action=save")
    public String saveCustomer(@ModelAttribute("customer") CustomerDto customerDto, RedirectAttributes redirectAttributes) {
        Customer savedCustomer = customerService.save(customerDtoToCustomer.convert(customerDto));
        redirectAttributes.addFlashAttribute("lastAction", "Saved " + savedCustomer.getFirstName() + " " + savedCustomer.getLastName());
        return "redirect:/customer/" + savedCustomer.getId();
    }

    /**
     * Cancels the customer submission and renders the default customer view
     *
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/", ""}, params = "action=cancel")
    public String cancelSaveCustomer() {
        return "redirect:/customer/";
    }

    /**
     * Deletes the customer and renders the default customer view
     *
     * @param id the customer id
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}/delete")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Customer customer = customerService.get(id);
        customerService.delete(id);
        redirectAttributes.addFlashAttribute("lastAction", "Deleted " + customer.getFirstName() + " " + customer.getLastName());
        return "redirect:/customer";
    }

    /* For debugging purposes without a configured logging tool
    @ExceptionHandler(Exception.class)
    public void handleAllException(Exception ex) {
        ex.printStackTrace();
    }
    */
}
