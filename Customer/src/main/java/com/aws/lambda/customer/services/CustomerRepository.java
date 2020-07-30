package com.aws.lambda.customer.services;

import com.aws.lambda.customer.entities.Customer;

import java.util.List;

public interface CustomerRepository {

    public Customer createCustomer(Customer customer);

    public Customer readCustomer(String uuid);

    public Customer updateCustomer(Customer customer);

    public void deleteCustomer(String uuid);

    public List<Customer> readAllCustomers();
}
