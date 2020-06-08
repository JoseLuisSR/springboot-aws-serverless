package com.aws.lambda.customer.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

    private String name;

    private Integer age;

    public Customer(){

    }

    public Customer(String name, Integer age){
        this.name = name;
        this.age = age;
    }
}
