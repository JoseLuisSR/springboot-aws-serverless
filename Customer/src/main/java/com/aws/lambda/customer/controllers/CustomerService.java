package com.aws.lambda.customer.controllers;

import com.aws.lambda.customer.entities.Customer;

import java.util.List;

public interface CustomerService {

    public Customer createCustomer(Customer customer);

    public Customer readCustomer(String uuid);

    public Customer updateCustomer(Customer customer);

    public void deleteCustomer(String uuid);

    public List<Customer> readAllCustomers();

}
