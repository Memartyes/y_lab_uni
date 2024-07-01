CREATE SCHEMA IF NOT EXISTS coworking;

CREATE TABLE IF NOT EXISTS coworking."workspaces-liquibase"
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    bookedBy VARCHAR(255),
    bookingTime TIMESTAMP
);

INSERT INTO coworking."workspaces-liquibase" (name, bookedBy, bookingTime) VALUES
('Observer', 'admin', '2024-06-30 09:00:00'),
('User', 'user', '2024-06-30 10:00:00');