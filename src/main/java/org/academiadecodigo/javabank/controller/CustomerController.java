package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/customer/list")
    public String showCustomers(Model model) {

        model.addAttribute("customers", customerService.getCustomers());

        return "list";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/customer/{id}")
    public String getCustomer(Model model, @PathVariable Integer id) {

        model.addAttribute("customer", customerService.get(id));

        return "info";
    }

}
