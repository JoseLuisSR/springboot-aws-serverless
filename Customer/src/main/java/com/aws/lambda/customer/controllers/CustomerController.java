package com.aws.lambda.customer.controllers;

import com.aws.lambda.customer.entities.Customer;
import com.aws.lambda.customer.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequestMapping(path = "${customer.context.path}")
@EnableWebMvc
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void createCustomer(@RequestBody Customer customer){
        customerService.createCustomer(customer);
    }

    @GetMapping(path = "${customer.getByName}")
    public Customer getByName(@PathVariable("key") String name){
        return customerService.getCustomerByName(name);
    }

}
