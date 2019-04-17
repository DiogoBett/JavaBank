package org.academiadecodigo.javabank.dto.converters;

import org.academiadecodigo.javabank.dto.CustomerDTO;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter {

    public CustomerDTO customerToDTO(Customer customer) {

        CustomerDTO newCustomerDTO = new CustomerDTO();

        newCustomerDTO.setId(customer.getId());
        newCustomerDTO.setFirstName(customer.getFirstName());
        newCustomerDTO.setLastName(customer.getLastName());
        newCustomerDTO.setEmail(customer.getEmail());
        newCustomerDTO.setPhone(customer.getPhone());

        return newCustomerDTO;
    }

    public Customer dtoToCustomer(CustomerDTO customerDTO) {

        Customer newCustomer = new Customer();

        newCustomer.setId(customerDTO.getId());
        newCustomer.setFirstName(customerDTO.getFirstName());
        newCustomer.setLastName(customerDTO.getLastName());
        newCustomer.setEmail(customerDTO.getEmail());
        newCustomer.setPhone(customerDTO.getPhone());

        return newCustomer;
    }
}
