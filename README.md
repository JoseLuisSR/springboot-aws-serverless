# AWS Serverless & CloudFormation.

One of the outstanding cloud computing services is serverless where you don't worry about the infrastructure needing to deploy software components, you just focus on coding and developing the software.
Serverless manage all administrative tasks to set up, provision and stay up to date the infrastructure and everything required to run and scale your application with high availability.

## Use Case

In this article we are going to cover the use case to integration between 3rd party and our Customers system through RESTful/JSON API expose over internet. We will development the API with Java and Spring Boot, 
run it in AWS Lambda function, integrate with AWS DynamoDB table to execute CRUD operations and expose API over internet using AWS API Gateway. The architecture overview is:

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/serverless-aws.png?raw=true)

Also we can use this architecture to handle Big Data use cases to ingest, process and store data at near real time. API Gateway can supports high volume of request per second, Lambda Function can auto scaling to process and convert high volume of data 
and DynamoDB with low latency for Read and Write operations and auto scaling to store the data.

You can enhancement this use case using [AWS Cognito](https://aws.amazon.com/cognito/) service to validate access token before consume the API and also use [AWS VPC](https://aws.amazon.com/vpc/) to deploy AWS services inside Virtual Private Cloud and control the access to resources.

## AWS Spring Boot

![Screenshot](https://d2908q01vomqb2.cloudfront.net/ca3512f4dfa95a03169c5a670a4c91a19b3077b4/2018/02/28/serverless-java-container-process-1-1024x472.png)

[AWS](https://github.com/awslabs) created [aws-serverless-java-container](https://github.com/awslabs/aws-serverless-java-container) serverless Java Container library acts as a proxy between the Lambda runtime and the Java Spring Boot application, translates incoming events from 
API Gateway to request objects that Spring Boot can understand, and transforms responses from your application into a format that API Gateway understands. Also there is a library [com.amazonaws:aws-java-sdk-dynamodb](https://github.com/aws/aws-sdk-java-v2) to integration between Java 
application and DynamoDB, find more details about how to use these libraries with Java application in this [repository](https://github.com/JoseLuisSR/springboot-aws-serverless/tree/master/Customer). 

You can create a quickly Spring Boot 2 application to run in AWS Lambda function through Maven archetype, and with others Java frameworks like Spring, Apache Struts, Jersey & Spark, got to [AWS Labs](https://github.com/awslabs/aws-serverless-java-container) for more details. 

## Serverless

* Serverless applications / Function as a Service (FaaS)

I thought serverless is fit to deploy microservices architecture but both are architectures and connect to them in different way, for microservices is common use HTTP API interface and serverless is all about events.

Both architectures are for separate the concerns of the application in many piece of software with high cohesion (single responsibility) and low coupling, so you can build a series of services that scale separately from each other to attend pikes of request and when some 
service going dow don't impact the others.

Serverless is an event driven architecture that response to events, in the other hand microservices response for API calls. The API expose by API Gateway is not the only mechanism to generate events, there are other triggers that can generate events and 
[invoke lambda function](https://docs.aws.amazon.com/lambda/latest/dg/lambda-services.html#intro-core-components-event-sources) synchronous and asynchronous like S3, CloudFront, SNS, IoT and others. 

I recommend [Serverless Best Practices](https://medium.com/@PaulDJohnston/serverless-best-practices-b3c97d551535) to know more about serverless applications. 

* Serverless service

When we talk about serverless service we meaning AWS services like DynamoDB and Lambda Function where AWS manage all administrative task to get high availability, auto scaling, replication and more. We are going to see these services in detail below.

## Cloudformation

[Cloudformation](https://docs.aws.amazon.com/cloudformation/index.html) is AWS service to deploy infrastructure as a code. The goal is use the advantages of the code like control versions, standardize and reusable piece of 
code and automation with DevOps tools to create AWS services like Lambda function, DynamoDB a others dynamically. Cloudformation is integrate with all AWS services and it is free you only pay for the AWS services used.

![Screenshot](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/images/create-stack-diagram.png)

Cloudformation stacks control the steps and states of create AWS services using templates. The template is a JSON or YAML file that describes resources 
and properties necessary for each AWS services. You can create stack to deploy services in a single AWS account, or you can use Stack Set to deploy resources in multiple aws accounts. 

We are going to use [Nested Stack](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-nested-stacks.html) to create AWS services using more 
that one stack. Each stack has a template with the parameters, resources and outputs for one AWS service, in that way we can create stacks with single responsibility making 
more easy to fix issues and update it, also get specialize stacks per AWS service and can reuse in other projects.

The root stack contains the nested stacks, define the order to create each one and the parameters that each stack needs, also control what outputs of one stack pass to other 
stack as parameters to integration between AWS services.

![Screenshot](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/images/cfn-console-nested-stacks.png)

In this repository you can find master stack, API Gateway, Lambda function and DynamoDB stacks. Below the mind map shows you the relation between stacks.

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

The Spring Boot application expose RESTful/JSON end-points to perform CRUD operations over DynamoDB table, you can find the code and details in this [repository](https://github.com/JoseLuisSR/springboot-aws-serverless/tree/master/Customer).

* [AWS::Lambda::Function](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-lambda-function.html)
* [AWS::IAM::Role](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-role.html)
* [AWS::Logs::LogGroup](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-logs-loggroup.html)
* [AWS::IAM::ManagedPolicy](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-managedpolicy.html) 

The IAM Role and ManagedPolicy resources are to set up the permissions needed to Lambda Function can use CloudWatch and DynamoDB services, then lambda stack template depends on DynamoDB stack, 
it is a precedence between these two stacks that we need to indicate in our master stack.

### DynamoDB

[DynamoDB](https://aws.amazon.com/es/dynamodb/) is not relational database and it is serverless service where all administrative tasks to enable high availability, good performance and auto scaling are manage by AWS.

The main components of DynamoDB are:

* **Table**: Set of items.
* **Item**: Set of attributes.
* **Attribute**: Data of a data type like String.
* **Partition key**: Main key that define the partition to store the item and attributes. DynamoDB distribute the data in different partitions to store it. The partition can be in different availability zone of one regions 
or different regions also. Is important that partition key has diversity of values to distribute the data in different partition.
* **Sort key**: Key used sort the data stored in the partition. It is optional and can use together with partition key.
* **Local Secondary Index**: Index over an attribute and partition key that improve the performance of the DynamoDB table to process query request.
* **Global Secondary Index**: Index over attributes different of partition key and store key to improve the performance of the DynamoDB table.

In this article we are going to create DynamoDB table to store the data of Customer entity (uuid, name and age), use the uuid as partition key and name as sort key:

![Screenshot](https://github.com/JoseLuisSR/springboot-aws-serverless/blob/master/doc/img/dynamodb-mind-map.jpg?raw=true)

The DynamoDB stack template only has one resource and doesn't need more but you can add more properties to create Local Secondary Index and Global Secondary Index also.

* [AWS::DynamoDB::Table](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-dynamodb-table.html)

The stack use the DynamoDB table name and ARN as outputs, these values are important to set up IAM role with the policies to perform Read/Write operations over it table.

## References

* https://medium.com/@PaulDJohnston/serverless-and-microservices-a-match-made-in-heaven-9964f329a3bc

* https://medium.com/@PaulDJohnston/serverless-best-practices-b3c97d551535

* https://medium.com/@nuozhoux/get-started-guide-spring-boot-crud-with-dynamodb-991e6341844c