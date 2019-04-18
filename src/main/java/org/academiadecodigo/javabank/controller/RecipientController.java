package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.converters.CustomerToCustomerDto;
import org.academiadecodigo.javabank.converters.RecipientDtoToRecipient;
import org.academiadecodigo.javabank.converters.RecipientToRecipientDto;
import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.RecipientNotFoundException;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.services.CustomerService;
import org.academiadecodigo.javabank.services.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/customer")
public class RecipientController {

    private RecipientService recipientService;
    private CustomerService customerService;

    private RecipientToRecipientDto recipientToRecipientDto;
    private RecipientDtoToRecipient recipientDtoToRecipient;
    private CustomerToCustomerDto customerToCustomerDto;

    /**
     * Sets the recipient service
     *
     * @param recipientService the recipient service to set
     */
    @Autowired
    public void setRecipientService(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

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
     * Sets the converter for converting between recipient model objects and recipient form objects
     *
     * @param recipientToRecipientDto the recipient to recipient form converter to set
     */
    @Autowired
    public void setRecipientToRecipientDto(RecipientToRecipientDto recipientToRecipientDto) {
        this.recipientToRecipientDto = recipientToRecipientDto;
    }

    /**
     * Sets the converter for converting between recipient form objects and recipient model objects
     *
     * @param recipientDtoToRecipient the recipient form to recipient converter to set
     */
    @Autowired
    public void setRecipientDtoToRecipient(RecipientDtoToRecipient recipientDtoToRecipient) {
        this.recipientDtoToRecipient = recipientDtoToRecipient;
    }

    /**
     * Sets the customer to customer form converter
     *
     * @param customerToCustomerDto the customer form to customer converter
     */
    @Autowired
    public void setCustomerToCustomerDto(CustomerToCustomerDto customerToCustomerDto) {
        this.customerToCustomerDto = customerToCustomerDto;
    }

    /**
     * Adds a recipient
     *
     * @param cid   the customer id
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/add")
    public String addRecipient(@PathVariable Integer cid, Model model) {
        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(cid)));
        model.addAttribute("recipient", new RecipientDto());
        return "recipient/add-update";
    }

    /**
     * Edits a recipient from a customer
     *
     * @param cid   the customer id
     * @param id    the recipient id
     * @param model the model object
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/{id}/edit")
    public String editRecipient(@PathVariable Integer cid, @PathVariable Integer id, Model model) {
        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(cid)));
        model.addAttribute("recipient", recipientToRecipientDto.convert(recipientService.get(id)));
        return "recipient/add-update";
    }

    /**
     * Saves the recipient form submission
     *
     * @param cid                the customer id
     * @param recipientDto      the recipient form
     * @param bindingResult      the binding result object
     * @param redirectAttributes the redirect attribute object
     * @param model              the model object
     * @return the view to render
     * @throws CustomerNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient/", "/{cid}/recipient"}, params = "action=save")
    public String saveRecipient(@PathVariable Integer cid, @Valid @ModelAttribute("recipient") RecipientDto recipientDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws CustomerNotFoundException {

        model.addAttribute("customer", customerToCustomerDto.convert(customerService.get(cid)));

        if (bindingResult.hasErrors()) {
            return "recipient/add-update";
        }

        try {

            Recipient recipient = recipientDtoToRecipient.convert(recipientDto);
            customerService.addRecipient(cid, recipient);

            redirectAttributes.addFlashAttribute("lastAction", "Saved " + recipient.getName());
            return "redirect:/customer/" + cid;

        } catch (AccountNotFoundException ex) {

            bindingResult.rejectValue("accountNumber", "invalid.account", "invalid account number");
            return "recipient/add-update";
        }
    }

    /**
     * Cancels the recipient form submission
     *
     * @param cid the customer id
     * @return the view to render
     */
    @RequestMapping(method = RequestMethod.POST, path = {"/{cid}/recipient/", "/{cid}/recipient"}, params = "action=cancel")
    public String cancelSaveRecipient(@PathVariable Integer cid) {
        return "redirect:/customer/" + cid;
    }

    /**
     * Deletes the recipient
     *
     * @param cid                the customer id
     * @param id                 the recipient id
     * @param redirectAttributes the redirect attributes object
     * @return the view to render
     * @throws CustomerNotFoundException
     * @throws RecipientNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{cid}/recipient/{id}/delete")
    public String deleteRecipient(@PathVariable Integer cid, @PathVariable Integer id, RedirectAttributes redirectAttributes) throws CustomerNotFoundException, RecipientNotFoundException {
        Recipient recipient = recipientService.get(id);
        customerService.removeRecipient(cid, id);
        redirectAttributes.addFlashAttribute("lastAction", "Deleted " + recipient.getName());
        return "redirect:/customer/" + cid;
    }
}
