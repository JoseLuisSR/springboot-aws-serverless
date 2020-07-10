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
```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compile 'com.amazonaws.serverless:aws-serverless-java-container-springboot2:[1.0,)'
    ...
}
```

As second steps we need to create an handler class that implements `com.amazonaws.services.lambda.runtime.RequestStreamHandler`y interface, define static variable `SpringBootLambdaContainerHandler` 
and override the handleRequest method.  

```
public class StreamLambdaHandler implements RequestStreamHandler {

    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    ...

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        handler.proxyStream(input, output, context);
    }
}
```

The AwsProxyRequest and AwsProxyResponse POJOs are to map API Gateway with Proxy integration with Java objects to process request and response events in Spring Boot. 

## Cloudformation

[Cloudformation](https://docs.aws.amazon.com/cloudformation/index.html) is AWS service to deploy infrastructure as a code. The goal is use the advantages of the code like control versions, standard & reusable piece of 
code and automation with DevOps tools to create AWS resources like Lambda function, DynamoDB a others dynamically. Cloudformation is integrate with all AWS services 
and it is free you only pay for the AWS services created through stacks.

![Screenshot](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/images/create-stack-diagram.png)

Cloudformation use stacks to control the steps and states to create AWS resources using templates. The template is a JSON or YAML file that describes AWS resources 
and properties that are going create. You can create stack to deploy resource in a single aws account, or you can use Stack Set to deploy resources in multiple aws accounts. 

We are going to use [Nested Stack](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/images/cfn-console-nested-stacks.png) to create AWS resources using more 
that one stack. Each stack has a template with the parameters, resources and outputs for one AWS service, in that way we can create stacks with single responsibility making 
more easy to fix issues and update it, also get specialize stacks per AWS service and reuse then in other projects.

The root stack contains the nested stacks, define the order to create each one and the parameters that each stack needs, also control what outputs of one stack pass to other 
stack like parameters to integration between AWS services.

![Screenshot](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/images/cfn-console-nested-stacks.png)

In this repository you can find master stack (root) and API Gateway and Lambda functions stacks. 

### AWS API Gateway

It is serverless service and is integration middleware between clients and AWS services. API Gateway knows the location of the end-points, can control the access to the 
services using JWT with oauth2.0, define limits like TPS and monitoring the requests to services. 

![Screenshot](https://d1.awsstatic.com/serverless/New-API-GW-Diagram.c9fc9835d2a9aa00ef90d0ddc4c6402a2536de0d.png)

[API Gateway](https://aws.amazon.com/api-gateway/?nc1=h_ls) can expose REST API, HTTP API and Web Sockets API and con integrate with Lambda Function, HTTP, VPS and AWS services like EC2, Kinesis and others.

Our API Gateway stack template has the code to create REST API and integrate with Lambda Function. Below you can find a mind map with the resources and precedence needed to create API Gateway with Cloudformation.

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/api-gateway-mind-map.jpg?raw=true)

### AWS Lambda function


This is a project to build Spring Boot application and deploy it on AWS Lambda function  using 
Infrastructure as Code (IaC) with CloudFormation.

Working in progress .....
