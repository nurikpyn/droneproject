CREATE USER 'reekind_dronepr'@'%' IDENTIFIED WITH mysql_native_password;GRANT USAGE ON *.* TO 'reekind_dronepr'@'%' REQUIRE NONE WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;
SET PASSWORD FOR 'reekind_dronepr'@'%' = '***';
CREATE DATABASE IF NOT EXISTS `reekind_dronepr`;
GRANT ALL PRIVILEGES ON `reekind\_dronepr`.* TO 'reekind_dronepr'@'%';



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

DROP TABLE IF EXISTS adresses;
CREATE TABLE adresses (
  adressID INT NOT NULL AUTO_INCREMENT
  , adress char NOT NULL
  , latitude DOUBLE (9,6)
  , longitude DOUBLE (9,6)
  , PRIMARY KEY (adressID)
);

INSERT INTO `adresses` (`adressID`, `adress`, `latitude`, `longitude`) VALUES
  (1, 'Strete', 50.309861, -3.629856),
  (2, 'Thurlestone', 50.270653, -3.862912);

DROP TABLE IF EXISTS drones;
CREATE TABLE drones (
  droneID INT NOT NULL AUTO_INCREMENT
  , droneTypeID INT NOT NULL
  , droneStatus INT NOT NULL
  , droneDepotID INT NOT NULL
  , PRIMARY KEY (droneID)
);

INSERT INTO `drones` (`droneID`, `droneTypeID`, `droneStatus`, `droneDepotID`) VALUES
  (1, 1, 0, 1),
  (2, 1, 0, 1),
  (3, 1, 0, 1),
  (4, 1, 0, 1);

DROP TABLE IF EXISTS dronetypes;
CREATE TABLE dronetypes (
  droneTypeID INT NOT NULL AUTO_INCREMENT
  , maxWeightInGrams INT NOT NULL
  , maxPackageCount INT NOT NULL
  , maxRange FLOAT NOT NULL
  , PRIMARY KEY (droneTypeID)
);

INSERT INTO `dronetypes` (`droneTypeID`,  `maxWeightInGrams`,`maxPackageCount`, `maxRange`) VALUES
  (1, 4000, 4, 60);


DROP TABLE IF EXISTS users;
-- auto-generated definition
create table users
(
  userID int auto_increment
    primary key,
  email varchar(250) not null,
  passwordHash varchar(250) not null,
  userType int not null
);

DROP TABLE IF EXISTS depots;
CREATE TABLE `depots` (
  `depotID` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `latitude` double(9,6) NOT NULL,
  `longitude` double(9,6) NOT NULL,
  PRIMARY KEY(depotID)
);

INSERT INTO `depots` (`depotID`, `name`, `latitude`, `longitude`) VALUES
(1, 'Salcombe', 50.237000, -3.782000);
