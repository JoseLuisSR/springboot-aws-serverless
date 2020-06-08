package com.aws.lambda.customer.services;

import com.aws.lambda.customer.entities.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    public void createCustomer(Customer customer){
        log.info("Create customer: " + customer.getName() + " age: " + customer.getAge());
    }

    public Customer getCustomerByName(String name){
        Customer customer = new Customer();
        customer.setAge(33);
        customer.setName(name);
        log.info("Get customer: " + customer.getName() + " age: " + customer.getAge());
        return customer;
    }

}
