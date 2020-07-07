# AWS Serverless & CloudFormation.

One of the outstanding cloud computing services is serverless where you don't worry about the infrastructure needing to deploy software components, you just focus on coding and developing the software.
Serverless manage all administrative tasks to set up, provision and stay up to date the infrastructure and everything required to run and scale your application with high availability.

In this article we will see how to create serverless web application with Spring Boot & aws-serverless-java-container library  to run in AWS Lambda function, also we are going to use AWS Cloudformation 
to deploy AWS Lambda function, AWS API Gateway and others services as infrastructure as code.

## Use Case

For this article we are going to build RESTful API with Spring Boot, run in AWS Lambda function, expose the end-points over internet using API Gateway and record the logs events on CloudWatch. 
The architecture overview is:

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/serverless-aws.png?raw=true)


## AWS Spring Boot

[AWS](https://github.com/awslabs/aws-serverless-java-container/wiki/Quick-start---Spring-Boot) created a Java library to run Spring Boot application in AWS Lambda function, it handle the events 
send by different kind of triggers like API Gateway to start the application and pass the events to RESTful end-points.

The first step to set up Spring Boot project to run in lambda function is add `serverless-java-container-springboot2` library through Gradle, you can also use Maven. For Spring Boot 2.0 version 
and upwards you need use `serverless-java-container-springboot2` library instead of `serverless-java-container-springboot`.

    dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-web'
        compile 'com.amazonaws.serverless:aws-serverless-java-container-springboot2:[1.0,)'
        ...
        }
    }

As second steps we need to create an handler class that implements `com.amazonaws.services.lambda.runtime.RequestStreamHandler`y interface, define class attribute SpringBootLambdaContainerHandler 
and override the handleRequest method. 

## Cloudformation

### AWS Lambda function

### AWS API Gateway

### Stack

### Nested Stack


This is a project to build Spring Boot application and deploy it on AWS Lambda function  using 
Infrastructure as Code (IaC) with CloudFormation.

Working in progress .....
