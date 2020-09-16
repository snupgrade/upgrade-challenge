CREATE SCHEMA IF NOT EXISTS users;

CREATE TABLE IF NOT EXISTS users.users(
    id varchar(64) PRIMARY KEY, 
    first_name varchar(128),
    last_name varchar(128),
    email VARCHAR(512) unique);
