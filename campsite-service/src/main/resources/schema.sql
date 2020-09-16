CREATE SCHEMA IF NOT EXISTS campsite;

CREATE TABLE IF NOT EXISTS campsite.campsite(id varchar(64) PRIMARY KEY, name VARCHAR(255) NOT NULL unique);

DELETE FROM campsite.campsite;

INSERT INTO campsite.campsite VALUES('700001a4-7c8a-47d6-a689-fdf772f82f87', 'UPGRADE_CHALLENGE_CAMP');