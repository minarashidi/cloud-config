## Centralized configuation with Spring Cloud Config
when dealing with Microservices, centralizing something may seem a bit off since Microservices is all about decomposing our system into separate independent pieces. Howover, what we want is isolation of processes. other aspects of microservice opertions should be dealt with in a centralized way.
For example, logs should end up in our logging solution such as **elk stack**, monitoring goes into a dedicated monitoring - in this poc, I am showing how we can centralized configuation using Spring Cloud Config and git as a backing service.

Here are the reason why we should care about configuration:
* Externalization: we should keep our configuration outside our code in property or yml file, so it's very easy-located without any struggle to find any particular config.
* Environment Independent: externalization should enable us to deploy our application in whatever environment that we want, build once run anywhere. For this we can use spring profiles. For example, we could have a set of configuration for dev, staging and prod environments. 
* Deployment bundle: without centralized configuration, if we change anything in our configuration since configuration is bundled with application, application needs to be rebuilt and redeployed, but with centralized approach, Each Microservice can be run on any desired environment: dev, test, prod, etc with the need for rebuild and redeployment.

* Operational Scalability: what we need is to quickly apply changes to all instances that are currently running. Without centralized configuration this could be a tough task to redeploy them one by one(for example what happens if we have 20 instances of each service).
* Traceability: who changed what and when.


### Spring Cloud Config

The Spring Cloud Config server can be viewed as a proxy between your services and their configuration, providing useful features such as:

* Support for several different configuration backends such as git (default), SVN, or distributed file system. You can even have more than one storage at the same time.
* Transparent decryption of encrypted properties.
* Pluggable security.
* Push mechanism using git hooks / REST API and Spring Cloud Bus (e.g. kafka) to propagate changes in config files to services, making live reload of configuration.


### Overview
In this poc, I show how to manage configuration in a distributed environment: Project consists of following services:
* Discovery service ## TODO
* Merchant service
* Currency rate service
* Gateway
* Config server
 
I have two simple microservices: merchant and currency rate and they're available through API gateway. 
Each application gets registered to service registry.  

#### Config server:
On startup, it gets registered to service registry and connects to git config repository. 
1. We need to add the following dependency: ``` org.springframework.cloud:spring-cloud-config-server ```
2. Mark the main class with @EnableConfigServer  annotation:
3. Define all necessary properties in application.yml (where is the config git repo, and set port number to 8888)
also we can provide the ``` search-paths: 'config/{application}'``` which tells us where the config files for each service is. Here I can also provide {profile} and {label} alongside of {application} placeholder.

#### Config client: 
In order to connect to the config server, we need to add the following dependency: ```org.springframework.cloud:spring-cloud-starter-config```.
Here in our services we need to define bootstrap.yml which is a parent for main application context and it's used for the purpose of loading and decrypting(if needed) properties from external sources.
Here are information we need to put here: config server uri, application name, profile and label (those placeholder that I mentioned)

```spring:
  application:
    name: { microservice name}

  cloud:
    config:
      uri: http://localhost:8888 TODO which I should get it through service discovery later 
```
since we have actuator, after changing a property we should commit and push it, then we have two approaches:
 1. Manual refresh: curl -X POST http://localhost:8085/refresh which tells the service to reload the bootstrap context to refresh all beans annotated with @RefreshScope
 but it has some drawbacks consider we have several instances of this service we need to call this endpoint for all of them which is terrible.
 
 2. Propagating changes dynamically: for this we can use Spring Cloud Bus, which uses both actuator and apache kafka as a message broker.
 so to enable Spring cloud bus and connect to kafka, in gateway and our services we should add this dependency ``` org.springframework.cloud:spring-cloud-starter-bus-kafka ```
 
 
 
 
 add the following dependency in config server,  spring-cloud-config-monitor - the  /monitor endpoint that accepts POST requests with information about what service needs to be refreshed.
 
 ``` curl -X POST localhost:8888/monitor -d 'path=merchant-service' ```
 
 Furthermore, we could add Webhook to our github platform, so we don't need to call the POST endpoint anymore and it works automatically on git commit push.  
  
      
 ## Build

In order to build all services, run following command in project's
root directory:

``` mvn clean install ```

## Run

We could run all services on your local machine by performing one of the following commands in every subdirectory. 

``` mvn spring-boot:run ```
or
``` java -jar {application}-{version}.jar ```

## TODO : Docker

There is also a _Dockerfile_ prepared for each service and a
_docker-compose.yml_ file in a root directory. In order to setup
whole environment, run:

``` docker-compose up```
  


  


  