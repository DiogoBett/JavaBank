package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.model.Customer;
import org.academiadecodigo.javabank.model.Recipient;
import org.academiadecodigo.javabank.services.CustomerService;
import org.academiadecodigo.javabank.view.RecipientsView;

import java.util.List;

/**
 * The {@link RecipientsView} controller
 */
public class RecipientsController extends AbstractController {

    private CustomerService customerService;

    /**
     * Sets the customer service
     *
     * @param customerService the customer service to set
     */
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Gets a list of the customer recipients
     *
     * @return the recipient list
     * @see CustomerService#listRecipients(Integer)
     */
    public List<Recipient> getRecipients() {
        Customer customer = authService.getAccessingCustomer();
        return customerService.listRecipients(customer.getId());
    }
}
