package org.academiadecodigo.javabank.controller;

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
@RequestMapping("/customer")
@Controller
public class CustomerController {

    private CustomerService customerService;

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
     * Renders a view with a list of customers
     *
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/list", "/", ""})
    public String listCustomers(Model model) {

        model.addAttribute("customers", customerService.list());
        return "customer/list";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public String getCustomer(Model model, @PathVariable Integer id) {

        Customer customer = customerService.get(id);

        model.addAttribute("customer", customer);
        model.addAttribute("accounts", customer.getAccounts());
        model.addAttribute("recipients", customer.getRecipients());

        return "customer/info";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}/delete")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        customerService.removeCustomer(id);
        redirectAttributes.addFlashAttribute("lastAction", "Deleted Customer Sucessfully");
        return "redirect:/customer/list";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/add")
    public String addCustomer(Model model) {

        Customer newCustomer = new Customer();
        newCustomer.setFirstName("");
        newCustomer.setLastName("");
        newCustomer.setEmail("");
        newCustomer.setPhone("");

        model.addAttribute("customer", newCustomer);
        return "customer/add";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addCustomer")
    public String processCustomer(@ModelAttribute(value = "customer") Customer customer, RedirectAttributes redirectAttributes) {

        Customer editedCustomer = customerService.add(customer);
        redirectAttributes.addFlashAttribute("lastAction", "Customer Added Sucesfully");
        return "redirect:/customer/list" + editedCustomer.getId();
    }

}
