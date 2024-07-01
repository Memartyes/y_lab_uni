CREATE SCHEMA IF NOT EXISTS coworking;

CREATE TABLE IF NOT EXISTS coworking."conference_rooms-liquibase"
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    capacity INT NOT NULL
);

INSERT INTO coworking."conference_rooms-liquibase" (name, capacity) VALUES
('Information Technology', '10'),
('Foreign Languages', '20');