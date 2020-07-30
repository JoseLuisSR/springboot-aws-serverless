package com.aws.lambda.customer.controllers;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.aws.lambda.customer.entities.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@RestController
@RequestMapping(path = "${customer.context.path}")
@EnableWebMvc
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createCustomer(@RequestBody Customer customer){
        try {
            Customer response = customerService.createCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (AmazonServiceException e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        }catch (AmazonClientException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    @GetMapping(path = "${customer.uuid}")
    public ResponseEntity readCustomer(@PathVariable("uuid") String uuid){

        try{
            Customer response = customerService.readCustomer(uuid);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (AmazonServiceException e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        }catch (AmazonClientException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCustomer(@RequestBody Customer customer){

        try{
            Customer response = customerService.updateCustomer(customer);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (AmazonServiceException e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        }catch (AmazonClientException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    @DeleteMapping(path = "${customer.uuid}")
    public ResponseEntity deleteCustomer(@PathVariable("uuid") String uuid){

        try{
            customerService.deleteCustomer(uuid);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }catch (AmazonServiceException e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        }catch (AmazonClientException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

    @GetMapping(value = "${customer.all}")
    public ResponseEntity readAllCustomer(){

        try{
            List<Customer> response = customerService.readAllCustomers();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (AmazonServiceException e){
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e);
        }catch (AmazonClientException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),e);
        }
    }

}
