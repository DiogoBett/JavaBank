package org.academiadecodigo.javabank.controller;

import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testListCustomers() throws Exception {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        when(customerService.list()).thenReturn(customers);

        mockMvc.perform(get("/customer/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        mockMvc.perform(get("/customer/"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        verify(customerService, times(3)).list();
    }

    @Test
    public void testShowCustomer() throws Exception {

        int fakeId = 999;
        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName("Rui");
        customer.setLastName("Ferrao");
        customer.setEmail("mail@gmail.com");
        customer.setPhone("99999914143");

        when(customerService.get(fakeId)).thenReturn(customer);

        mockMvc.perform(get("/customer/" + fakeId))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/show"))
                .andExpect(model().attribute("customer", equalTo(customer)));

        verify(customerService, times(1)).get(fakeId);

    }

    @Test
    public void testDeleteCustomer() throws Exception {

        int fakeId = 9999;

        mockMvc.perform(get("/customer/" + fakeId + "/delete/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/list"));

        verify(customerService, times(1)).delete(fakeId);
    }

    @Test
    public void testDeleteRecipient() throws Exception {

        int fakeCustomerId = 9998;
        int fakeRecipientId = 9999;

        mockMvc.perform(get("/customer/" + fakeCustomerId + "/recipient/" + fakeRecipientId + "/delete/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId));

        verify(customerService, times(1)).removeRecipient(fakeCustomerId, fakeRecipientId);
    }
}
