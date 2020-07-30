package com.aws.lambda.customer.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.aws.lambda.customer.entities.Customer;
import com.aws.lambda.customer.services.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public Customer createCustomer(Customer customer) {
        dynamoDBMapper.save(customer);
        return customer;
    }

    @Override
    public Customer readCustomer(String uuid) {
        return dynamoDBMapper.load(Customer.class,uuid);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        ExpectedAttributeValue expectedAttributeValue = new ExpectedAttributeValue(new AttributeValue().withS(customer.getUuid()));
        expectedAttributeValueMap.put("uuid", expectedAttributeValue);
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression().withExpected(expectedAttributeValueMap);
        dynamoDBMapper.save(customer,saveExpression);
        return customer;
    }

    @Override
    public void deleteCustomer(String uuid) {
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        ExpectedAttributeValue expectedAttributeValue = new ExpectedAttributeValue(new AttributeValue().withS(uuid));
        expectedAttributeValueMap.put("uuid",expectedAttributeValue);
        DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression().withExpected(expectedAttributeValueMap);
        Customer customer = Customer.builder().
                    uuid(uuid).
                    build();
        dynamoDBMapper.delete(customer,deleteExpression);
    }

    @Override
    public List<Customer> readAllCustomers() {
        return dynamoDBMapper.scan(Customer.class, new DynamoDBScanExpression());
    }

}
