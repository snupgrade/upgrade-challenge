# User Service

This is the user service used for user operations.

<br />

## Requirements 

* Java 11
* [Maven 3.6.0+](https://maven.apache.org/download.cgi)

<br />

## Run the application

Run the following command in order to start the application.
    
    mvn spring-boot:run

This application run on the port 8082 by default.

<br />

## API

In order to call the different endpoints referer to [User Service Api](docs/openApiDefinition/UserOpenApiDefinition.yaml).

<br />

## Database Diagram

For the database diagram referer to [User Service Database](docs/database/user_db_schema.png).

An H2 database is used by default. To change it update the information in the application.yml file.
