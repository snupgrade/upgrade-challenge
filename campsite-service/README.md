# Campsite Service

This is the campsite service used for the campsite operations.

<br />

## Requirements 

* Java 11
* [Maven 3.6.0+](https://maven.apache.org/download.cgi)

<br />

## Run the application

Run the following command in order to start the application.
    
    mvn spring-boot:run

This application run on the port 8080 by default.

<br />

## API

In order to call the different endpoints referer to [Campsite Service Api](docs/openApiDefinition/CampsiteOpenApiDefinition.yaml).

<br />

## Database

For the database diagram referer to [Campsite Service Database](docs/database/campsite_db_schema.png).

An H2 database is used by default. To change it update the information in the application.yml file.
