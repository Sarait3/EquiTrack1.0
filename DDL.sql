DROP DATABASE IF EXISTS EquiTrack;
DROP USER IF EXISTS 'dbUser'@'localhost';

CREATE DATABASE EquiTrack;

USE EquiTrack;

CREATE TABLE users (
	id INT PRIMARY KEY AUTO_INCREMENT,
    userRole TEXT,
    fName TEXT,
    lName TEXT,
    email TEXT,
    password TEXT
    );
    
CREATE TABLE equipment (
	id VARCHAR(36) PRIMARY KEY,
    itemName TEXT,
    isAvailable TEXT,
    location TEXT,
    imagePath TEXT,
    notes TEXT,
    returnDate DATE
);

CREATE TABLE checkoutLog (
	id int PRIMARY KEY AUTO_INCREMENT,
    itemId TEXT,
    userId INT,
    checkoutDate DATE,
    returnDate DATE
);

CREATE USER 'dbUser'@'localhost' IDENTIFIED BY 'dbPassword';

GRANT ALL PRIVILEGES ON EquiTrack.* TO 'dbUser'@'localhost';