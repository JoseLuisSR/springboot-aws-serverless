# AWS Serverless & CloudFormation.

One of the outstanding cloud computing services is serverless where you don't worry about the infrastructure needing to deploy software components, you just focus on coding and developing the software.
Serverless manage all administrative tasks to set up, provision and stay up to date the infrastructure and everything required to run and scale your application with high availability.

In this article we will see how to create serverless web Java application with Spring Boot & aws-serverless-java-container library to run in AWS Lambda function, also we are going to use AWS Cloudformation 
to deploy AWS Lambda function, AWS API Gateway, AWS DynamosDB and others services as infrastructure as code.

## Use Case

For this article we are going to cover the use case to build and expose RESTful/JSON API over internet to clients can integrate with our Customer system. We will development the API with Java and Spring Boot, 
run it in AWS Lambda function, integrate with AWS DynamoDB table to execute CRUD operations and expose API over internet using AWS API Gateway. The architecture overview is:

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/serverless-aws.png?raw=true)

You can enhancement this use case using [AWS Cognito](https://aws.amazon.com/cognito/) service to validate access token before consume the API and also use [AWS VPC](https://aws.amazon.com/vpc/) to deploy AWS services inside Virtual Private Cloud and control the access to resources.

## AWS Spring Boot

[AWS](https://github.com/awslabs/aws-serverless-java-container/wiki/Quick-start---Spring-Boot) created a Java library to run Java application in AWS Lambda function, it handle the events 
send by different kind of triggers like API Gateway to start the application and pass the events to RESTful/JSON end-points created with Spring Boot.

The first step to set up Spring Boot project to run in lambda function is add `serverless-java-container-springboot2` library through Gradle, you can also use Maven. For Spring Boot 2.0 version 
and upwards you need use `serverless-java-container-springboot2` library instead of `serverless-java-container-springboot`.

```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compile 'com.amazonaws.serverless:aws-serverless-java-container-springboot2:[1.0,)'
    ...
}
```

As second steps we need to create an handler class that implements `com.amazonaws.services.lambda.runtime.RequestStreamHandler` interface, define static variable `SpringBootLambdaContainerHandler` 
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

The AwsProxyRequest and AwsProxyResponse POJOs are default implementation of the request object from an API Gateway AWS_PROXY integration. These classes are using to get the HTTP Request and pass 
from AWS_PROXY to RESTful/Json end-point and pass the HTTP response to AWS_PROXY.

## Cloudformation

[Cloudformation](https://docs.aws.amazon.com/cloudformation/index.html) is AWS service to deploy infrastructure as a code. The goal is use the advantages of the code like control versions, standard & reusable piece of 
code and automation with DevOps tools to create AWS services like Lambda function, DynamoDB a others dynamically. Cloudformation is integrate with all AWS services and it is free you only pay for the AWS services created and used.

![Screenshot](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/images/create-stack-diagram.png)

Cloudformation use stacks to control the steps and states to create AWS services using templates. The template is a JSON or YAML file that describes resources 
and properties necessary for each AWS services. You can create stack to deploy services in a single AWS account, or you can use Stack Set to deploy resources in multiple aws accounts. 

We are going to use [Nested Stack](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-nested-stacks.html) to create AWS services using more 
that one stack. Each stack has a template with the parameters, resources and outputs for one AWS service, in that way we can create stacks with single responsibility making 
more easy to fix issues and update it, also get specialize stacks per AWS service and can reuse in other projects.

The root stack contains the nested stacks, define the order to create each one and the parameters that each stack needs, also control what outputs of one stack pass to other 
stack as parameters to integration between AWS services.

![Screenshot](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/images/cfn-console-nested-stacks.png)

In this repository you can find master stack (root) and API Gateway, Lambda function and DynamoDB stacks. Below mind map show the nested stack with DynamoDB, Lambda & API Gateway stacks:

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/master-mind-map.jpg?raw=true)

### AWS API Gateway

It is serverless service and is integration middleware between clients and AWS services. API Gateway knows the location of the end-points, can control the access to the 
services using JWT with oauth2.0, define limits like TPS, load balancing and monitoring the requests to services. 

![Screenshot](https://d1.awsstatic.com/serverless/New-API-GW-Diagram.c9fc9835d2a9aa00ef90d0ddc4c6402a2536de0d.png)

[API Gateway](https://aws.amazon.com/api-gateway/?nc1=h_ls) can expose REST API, HTTP API and Web Sockets API and con integrate with Lambda Function, HTTP and AWS services like EC2, Kinesis and others.

Our API Gateway stack template has the code to create REST API and integrate with Lambda Function. Below you can find a mind map with the resources and precedence needed to create API Gateway with Cloudformation.

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/api-gateway-mind-map.jpg?raw=true)

Each node of the mind map is [AWS resource](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html) and these resources are part of AWS service, for AWS API Gateway 
that expose REST API and integrate with Lambda Function we need at less the below resources:

* [AWS::ApiGateway::RestApi](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-restapi.html)
* [AWS::ApiGateway::Resource](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-resource.html)
* [AWS::ApiGateway::Method](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-method.html)
* [AWS::ApiGateway::Deployment](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-deployment.html)
* [AWS::ApiGateway::Stage](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-stage.html)
* [AWS::ApiGateway::Model](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-apigateway-model.html)
* [AWS::Lambda::Permission](https://docs.aws.amazon.com/lambda/latest/dg/lambda-permissions.html)   

One of the clues to know which are the AWS resources necessary to create one AWS service through Cloudformation is first create the service from AWS Web Console and play attention to each screen and data 
needed in that process, most of the times you can find a relation between the screens and AWS Resources and the data with the properties of each resource.

Some preconditions to create AWS API Gateway is set up AWS Lambda function first, so we see a precedence between theses services. You can find the precedence in master stack template (master.yaml) and 
also some of the resources of AWS API Gateway need the ARN of Lambda Function as parameter. Check API Gateway stack template (apigateway.yaml) to see all the details.

### AWS Lambda function

Lambda function is a serverless services where you can deploy software components and AWS cares about the infrastructures and machine resource needed to get high availability and auto scaling of your 
component. It is developer experience functionality where the developers focus to build software and AWS manages administrative tasks to run it component.

Lambda function stack template has the code to deploy and execute Spring Boot application with Java 8. Below you can find a mind map with the parameters and resources needed to create Lambda Function with Cloudformation.

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/lambda-function-mind-map.jpg?raw=true)

The Spring Boot application expose RESTful/JSON end-points to perform CRUD operations over DynamoDB table, you can find the code and details in this repository.

The AWS resources needed to create AWS Lambda Function and set up Spring Boot application are:

* [AWS::Lambda::Function](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-lambda-function.html)
* [AWS::IAM::Role](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-role.html)
* [AWS::Logs::LogGroup](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-logs-loggroup.html)
* [AWS::IAM::ManagedPolicy](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-managedpolicy.html) 

The IAM Role and ManagedPolicy resources are to set up the permissions needed to Lambda Function can use CloudWatch and DynamoDB services, then lambda stack template depends on DynamoDB stack, 
it is a precedence between these two stacks that we need to indicate in our master stack.

### DynamoDB

DynamoDB is not relational database and it is serverless service where all administrative tasks to enable high availability, good performance and auto scaling are performed by AWS.

The main components of DynamoDB are:

* Table: Set of items.
* Item: Set of attributes.
* Attribute: Data of specific data type like String.
* Partition key: Main key that define the partition to store the item and attributes. DynamoDB distribute the data in different partitions to store it. The partition can be in different availability zone of one regions 
or different regions also. Is important that partition key has diversity of values to distribute the data in different partition.
* Sort key: Key used sort the data stored in the partition. It is optional and can use together with partition key.
* Local Secondary Index: Index over an attribute and partition key that improve the performance of the DynamoDB table to process query.request.
* Global Secondary Index: Index over attributes different of partition key and store key to improve the performance of the DynamoDB table.

In this article we are going to create DynamoDB table to store the data of Customer entity (uuid, name and age), use the uuid as partition key and name as sort key:

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/dynamodb-mind-map.jpg?raw=true)

The DynamoDB stack template only has one resource and doesn't need more but you can add more properties to create Local Secondary Index and Global Secondary Index also.

* [AWS::DynamoDB::Table](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-dynamodb-table.html)

The stack use the DynamoDB table name and ARN as outputs, these values are important to set up IAM role with the policies to perform Read/Write operations over it table.