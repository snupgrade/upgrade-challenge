CREATE SCHEMA IF NOT EXISTS reservation;

CREATE TABLE IF NOT EXISTS reservation.reservation_status(reservation_status VARCHAR(64) unique);

CREATE TABLE IF NOT EXISTS reservation.reservation(
    id varchar(64) PRIMARY KEY, 
    campsite_id varchar(64),
    user_id varchar(64),
    base_36_id VARCHAR(10) unique,
    arrival_date TIMESTAMP WITH TIME ZONE,
    departure_date TIMESTAMP WITH TIME ZONE,
    reservation_status VARCHAR(64),
    foreign key (reservation_status) references reservation_status(reservation_status));

CREATE TABLE IF NOT EXISTS reservation.reservation_date(
    id varchar(64) PRIMARY KEY, 
    reservation_id varchar(64),
    reservation_date DATE unique,
    foreign key (reservation_id) references reservation(id));
    
DELETE FROM reservation.reservation_status;

INSERT INTO reservation.reservation_status VALUES('CANCELED');
INSERT INTO reservation.reservation_status VALUES('PENDING');
INSERT INTO reservation.reservation_status VALUES('RESERVED');