# Weather Application

### Assumptions and conventions

   - Assuming that pressure and temperature are both double type, instead of int type.

### How to run tests and application locally

  To run all tests from command line, just issue the following command.
  
```
mvn test
```

  To start the application, run the following command, assuming maven and jdk8+ are installed.
    
```
mvn spring-boot:run 
```

  In order to query data from the running application, you can send HTTP request to the server by CURL like below to get current weather data for the city specified. 

## End points:
## 1. For current weather:
```
curl --location --request GET 'localhost:8080/current?location=Berlin'
```

### 2. For History
  
```
curl --location --request GET 'localhost:8080/history?location=Berlin'
```
## Exceptions thrown
* Exception handling is done, using a framework. @ExceptionHandler deals with  all the exceptions thrown from application.

Handle Scenarios 
`Blank Location
`History not present
`Invalid City,
`Invalid API key

# Approach for the solution:
  
https://api.openweathermap.org/data/2.5/weather?q=Berlin&APPID=726441bd682ecf1be35712107a98cfd0

From this response, the required fields are used and the rest are ignored.
`Current weather response for Berlin
{
    "temp": 285.55,
    "pressure": 1026.0,
    "umbrella": false
}
`Weather History for Berlin
       {
    "avg_temp": 285.55,
    "avg_pressure": 1026.0,
    "history": [
        {
            "temp": 285.55,
            "pressure": 1026.0,
            "umbrella": false
        },
        {
            "temp": 285.55,
            "pressure": 1026.0,
            "umbrella": false
        },
        {
            "temp": 285.55,
            "pressure": 1026.0,
            "umbrella": false
        },
        {
            "temp": 285.55,
            "pressure": 1026.0,
            "umbrella": false
        },
        {
            "temp": 285.55,
            "pressure": 1026.0,
            "umbrella": false
        }
    ]
}

* The response before sending back to the client, is persisted in a H2 Database. 
* The H2 database is used to store the history of the API calls. 
* The top 5 records for a city are returned to the client with the avg of temp and pressure.
* The various responsibilities are separated into layers. 
  
Rest Template --> Controller --> Service layer --> Validation at service Layer --> DB Repository layer

* The properties can be used to customise a information without changing the code. 

`#OpenWeather Properties
api.key = 726441bd682ecf1be35712107a98cfd0
current.weather.url = http://api.openweathermap.org/data/2.5/weather

`#DataSource Properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

* Unit tests have been used for all the layers.


# Things that can be improved:
* The logging can be improved
* The Exception handling can be added more, by more testing the edge cases. 
* The **Clean-up** of the Database can be done using a **Scheduler**. This Scheduler can run in periodically and delete the records for each city from the table, that are older than the top 5 records.
  
* A Distributed cache could be used instead of a Database. That will add to the performance for sure.
* The API key for the open API, is a Base64 encoded string right now in the yaml file. This could be put into a vault, Ex Hashi vault.



### Tasks besides development

# Give us an indication of what you think a production ready micro service should look like.

* In order to deploy our application to production environment. There are a couple of things we need to do for it.

* A **persistent DB** like postgress or mysql or even MongoDB should be   used, instead of an in memory DB.
  
* The in-memory DB can be used for local testing and for some low level environments.
  
* Vault should be configured for storing the Open weather API key.
  
* Sonar configuration has to be added for the quality gate. In this the JUnits coverage, Integration tests coverage, the style checks can be checked as a hard stop.
 
* The **Security scan** step like the Fortify Scan  has to be added.

* A step for **Pushing** the image or the jar to the nexus has to be added. 
* SSL can be added to make it a secure application.
* Integration tests using **Apache Karate** should be written.
* The logging level for the hibernate needs to be disabled for the production level code. This will act as both performance enhancement and unnecessary use of resources. 

   
*we should have a dedicated configuration file, say `application-production.properties` for it, which could be used to configure third party services URLs, and make sure things like API keys are configured through environment variables, instead of putting them in the configuration file.




#Describe how you would deploy the application in an AWS environment (we are using Terraform).

The deployment in AWS can be one in various ways, but the containerised one is what I feel is the best.
* CircleCI can be used for the CI/CD. The yaml config files can of the circleCI will be added in the code. 
*The CircleCI can be configured to listen to the gitrepo. The build will be triggered for each push.
  
* The image can be push to a private docker repository. 
* The Infrastructure can be provisioned in the AWS environment using Terraform.
*The AWS env will need **VPC**, **security groups** and an **EKS cluster** . All these can be configured via terraform configurations.
  
* Once the resources are ready, the deployment in the EKS cluster can be done. 
       
* The whole process can be fully automated by the CircleCI. 
*And for scaling up our application, we can either use AWS Elastic Beanstalk or maybe Kubernetes.



