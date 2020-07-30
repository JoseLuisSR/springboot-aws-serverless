package com.aws.lambda.customer.services;

import com.aws.lambda.customer.controllers.CustomerService;
import com.aws.lambda.customer.entities.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer){
        return customerRepository.createCustomer(customer);
    }

    public Customer readCustomer(String uuid){
        return customerRepository.readCustomer(uuid);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.updateCustomer(customer);
    }

    @Override
    public void deleteCustomer(String uuid) {
        customerRepository.deleteCustomer(uuid);
    }

    @Override
    public List<Customer> readAllCustomers() {
        return customerRepository.readAllCustomers();
    }

}
