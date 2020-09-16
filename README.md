# Upgrade challenge

This is the repository with the code for the upgrade challenge.

<br />

## Requirements 

* Java 11
* [Maven 3.6.0+](https://maven.apache.org/download.cgi)

<br />

## Installation

You will need to run mvn install inside the common-service-library to get the dependency.

<br />

## Run the application

Run the following service in order to start the application.

1. Go into the eureka-service and run 'mvn spring-boot:run'. This will allow the discovery of the other services.
2. Go into the campsite-service and run 'mvn spring-boot:run'.
3. Go into the user-service and run 'mvn spring-boot:run'.
4. Go into the reservation-service and run 'mvn spring-boot:run'.

<br />

## Architecture

To see the service communication flow look at [Service Communication Flow](docs/diagram/service_communication_flow.png)

<br />

## API

In order to call the different endpoints please referer to the following documentation.

1. [Campsite Service Api](campsite-service/docs/openApiDefinition/CampsiteOpenApiDefinition.yaml)
2. [Reservation Service Api](reservation-service/docs/openApiDefinition/ReservationOpenApiDefinition.yaml)
3. [User Service Api](user-service/docs/openApiDefinition/UserOpenApiDefinition.yaml)

To call the endpoints thru postmant you can use the [Postman Collection](docs/postman/upgrade_challenge_postman_collection.json) that has all the calls.

<br />

## Database

To see the schema of the database please look at the [Database Diagram](docs/database/challenge_database_schema.png).

For specific services refer to the following.

1. [Campsite Service Database](campsite-service/docs/database/campsite_db_schema.png)
2. [Reservation Service Database](reservation-service/docs/database/reservation_db_schema.png)
3. [User Service Database](user-service/docs/database/user_db_schema.png)