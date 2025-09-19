CREATE DATABASE floraartistry;
USE floraartistry;

CREATE TABLE MsUser (
    UserID CHAR(5) PRIMARY KEY CHECK(UserID REGEXP '^US[0-9][0-9][0-9]$'),
    UserName VARCHAR(50),
    UserEmail VARCHAR(50) UNIQUE,
    UserPassword VARCHAR(50),
    UserAddress VARCHAR(255),
    UserPhoneNumber VARCHAR(20),
    UserRole VARCHAR(20)
);

CREATE TABLE MsFlower (
    FlowerID CHAR(5) PRIMARY KEY CHECK(FlowerID REGEXP '^FL[0-9][0-9][0-9]$'),
    FlowerName VARCHAR(50),
    FlowerType VARCHAR(50),
    FlowerPrice INTEGER(10)
);

CREATE TABLE MsCart (
    UserID CHAR(5),
    FlowerID CHAR(5),
    Quantity INTEGER(10),
    PRIMARY KEY (UserID, FlowerID),
    FOREIGN KEY (UserID) REFERENCES MsUser(UserID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (FlowerID) REFERENCES MsFlower(FlowerID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE TransactionHeader (
    TransactionID CHAR(5) PRIMARY KEY CHECK(TransactionID REGEXP '^TR[0-9][0-9][0-9]$'),
    UserID CHAR(5),
    FOREIGN KEY (UserID) REFERENCES MsUser(UserID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE TransactionDetail (
    TransactionID CHAR(5),
    FlowerID CHAR(5),
    Quantity INTEGER(10),
    PRIMARY KEY (TransactionID, FlowerID),
    FOREIGN KEY (TransactionID) REFERENCES TransactionHeader(TransactionID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (FlowerID) REFERENCES MsFlower(FlowerID) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO MsUser (UserID, UserName, UserEmail, UserPassword, UserAddress, UserPhoneNumber, UserRole) VALUES
('US001', 'Admin', 'admin@gmail.com', 'admin123', '123 Main St', '1234567890', 'Admin'),
('US002', 'Customer', 'customer@gmail.com', 'customer123', '456 Oak St', '0987654321', 'Customer'),
('US003', 'Charlie Brown', 'charlie@gmail.com', 'customer123', '789 Pine St', '1112223333', 'Customer'),
('US004', 'David Wilson', 'david@gmail.com', 'customer123', '321 Elm St', '4445556666', 'Customer'),
('US005', 'Eva Adams', 'eva@gmail.com', 'customer123', '654 Maple St', '7778889999', 'Customer'),
('US006', 'Frank White', 'frank@gmail.com', 'customer123', '987 Cedar St', '2223334444', 'Customer'),
('US007', 'Grace Black', 'grace@gmail.com', 'customer123', '111 Birch St', '5556667777', 'Customer'),
('US008', 'Hannah Green', 'hannah@gmail.com', 'customer123', '222 Walnut St', '8889990000', 'Customer');

INSERT INTO MsFlower (FlowerID, FlowerName, FlowerType, FlowerPrice) VALUES
('FL001', 'Rose', 'Perennial', 1000),
('FL002', 'Tulip', 'Bulb', 1500),
('FL003', 'Lily', 'Perennial', 1200),
('FL004', 'Daffodil', 'Bulb', 1100),
('FL005', 'Sunflower', 'Annual', 1300),
('FL006', 'Orchid', 'Epiphyte', 2000),
('FL007', 'Daisy', 'Perennial', 900),
('FL008', 'Lavender', 'Perennial', 1400);

INSERT INTO MsCart (UserID, FlowerID, Quantity) VALUES
('US002', 'FL001', 10),
('US002', 'FL002', 15),
('US003', 'FL003', 12),
('US004', 'FL004', 11),
('US005', 'FL005', 13),
('US006', 'FL006', 20),
('US007', 'FL007', 9),
('US008', 'FL008', 14);

INSERT INTO TransactionHeader (TransactionID, UserID) VALUES
('TR001', 'US001'),
('TR002', 'US002'),
('TR003', 'US003'),
('TR004', 'US004'),
('TR005', 'US005'),
('TR006', 'US006'),
('TR007', 'US007'),
('TR008', 'US008'),
('TR009', 'US001'),
('TR010', 'US002'),
('TR011', 'US003'),
('TR012', 'US004');

INSERT INTO TransactionDetail (TransactionID, FlowerID, Quantity) VALUES
('TR001', 'FL001', 5),
('TR001', 'FL002', 3),
('TR002', 'FL003', 4),
('TR002', 'FL004', 2),
('TR003', 'FL005', 6),
('TR003', 'FL006', 7),
('TR004', 'FL007', 1),
('TR004', 'FL008', 8),
('TR005', 'FL001', 3),
('TR005', 'FL002', 4),
('TR006', 'FL003', 2),
('TR006', 'FL004', 3),
('TR007', 'FL005', 4),
('TR007', 'FL006', 5),
('TR008', 'FL007', 6),
('TR008', 'FL008', 7),
('TR009', 'FL001', 2),
('TR009', 'FL002', 3),
('TR010', 'FL003', 4),
('TR010', 'FL004', 5),
('TR011', 'FL005', 6),
('TR011', 'FL006', 7),
('TR012', 'FL007', 8),
('TR012', 'FL008', 9);