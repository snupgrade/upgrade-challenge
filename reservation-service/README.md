# Reservation Service

This is the reservation service used for the reservation operations.

<br />

## Requirements 

* Java 11
* [Maven 3.6.0+](https://maven.apache.org/download.cgi)

<br />

## Run the application

Run the following command in order to start the application.
    
    mvn spring-boot:run

This application run on the port 8081 by default.

<br />

## Testing

A test is available to test the concurrency. Run the following command and it should start.

    mvn test

<br />

## API

In order to call the different endpoints referer to [Reservation Service Api](docs/openApiDefinition/ReservationOpenApiDefinition.yaml).

<br />

## Database

For the database diagram referer to [Reservation Service Database](docs/database/reservation_db_schema.png).

An H2 database is used by default. To change it update the information in the application.yml file.

## Sequence Diagram

For the sequence diagrams refer to the following.

1. [Create Reservation](docs/diagram/create_reservation.png)