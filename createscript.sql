DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
  orderID INT NOT NULL AUTO_INCREMENT
  , orderTime DATETIME NOT NULL
  , adressID INT NOT NULL
  , weightInGrams INT NOT NULL
  , orderStatus INT NOT NULL
  , droneID INT
  , PRIMARY KEY (orderID)
);

DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
  adressID INT NOT NULL AUTO_INCREMENT
  , adress char NOT NULL
  , latitude DOUBLE (9,6)
  , longtitude DOUBLE (9,6)
  , PRIMARY KEY (adressID)
);

DROP TABLE IF EXISTS drones;
CREATE TABLE drones (
  droneID INT NOT NULL AUTO_INCREMENT
  , droneTypeID INT NOT NULL
  , droneStatus INT NOT NULL
  , PRIMARY KEY (droneID)
);

DROP TABLE IF EXISTS dronetypes;
CREATE TABLE dronetypes (
  droneTypeID INT NOT NULL AUTO_INCREMENT
  , maxWeightInGrams INT NOT NULL
  , maxPackageCount INT NOT NULL
  , maxRange FLOAT NOT NULL
  , maxSpeed FLOAT NOT NULL
  , PRIMARY KEY (droneTypeID)
);
DROP TABLE IF EXISTS users;
-- auto-generated definition
create table users
(
  userID int auto_increment
    primary key,
  email varchar(250) not null,
  passwordHash varchar(250) not null,
  userType int not null
)
;

