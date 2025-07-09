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

DELIMITER //
CREATE TRIGGER hash_pass_insert BEFORE INSERT ON users FOR EACH ROW
BEGIN
    SET NEW.password = sha2(new.password, 256);
END;//
DELIMITER ;

DELIMITER //
CREATE TRIGGER hash_pass_update BEFORE UPDATE ON users FOR EACH ROW
BEGIN
    SET NEW.password = sha2(new.password, 256);
END;//
DELIMITER ;

CREATE TABLE equipment (
	id VARCHAR(36) PRIMARY KEY,
    itemName TEXT,
    isOperational TEXT,
    location TEXT,
    imagePath TEXT,
    notes TEXT,
    returnDate DATE
);

CREATE TABLE requests (
    id VARCHAR(36) PRIMARY KEY,
    userId INT NOT NULL,
    equipmentId VARCHAR(36) NOT NULL,
    status TEXT NOT NULL,
    location TEXT NOT NULL,
    notes TEXT,
    requestDate DATE NOT NULL,
    checkoutDate DATE NOT NULL,
    returnDate DATE NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (equipmentId) REFERENCES equipment(id)
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

INSERT INTO users (userRole, fName, lName, email, password) VALUES
('Admin', 'Sara', 'Matt', 'admin@example.com', 'adminpass'),
('Regular', 'Bob', 'Smith', 'bob@example.com', 'bobpass'),
('Regular', 'Charlie', 'Weasley', 'charlie@example.com', 'charliepass');

INSERT INTO equipment (id, itemName, isOperational, location, imagePath, notes, returnDate) VALUES
(UUID(), 'Crane', 'operational', 'Warehouse A', 'images/crane1.jpg', 'Large crane for high-reach tasks.', NULL),
(UUID(), 'Excavator', 'out of service', 'Site A', 'images/excavator1.jpg', 'Currently in use for trenching.', '2025-06-30'),
(UUID(), 'Loader', 'operational', 'Warehouse B', 'images/loader1.jpg', 'Compact loader with new tires.', NULL),
(UUID(), 'Concrete Mixer', 'operational', 'Warehouse A', 'images/mixer1.jpg', 'Ready for concrete pour.', NULL),
(UUID(), 'Welding Machine', 'out of service', 'Site B', 'images/welder1.jpg', 'Currently in use for steel frame welding.', '2025-07-02');
