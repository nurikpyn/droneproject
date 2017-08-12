CREATE TABLE orders (
  orderID INT NOT NULL AUTO_INCREMENT
  , orderTime DATETIME NOT NULL
  , adressID INT NOT NULL
  , weight FLOAT NOT NULL
  , orderStatus INT NOT NULL
  , droneID INT
  , PRIMARY KEY (orderID)
);

CREATE TABLE addresses (
  adressID INT NOT NULL AUTO_INCREMENT
  , adress char NOT NULL
  , latitude DOUBLE (9,6)
  , longtitude DOUBLE (9,6)
  , PRIMARY KEY (adressID)
);


CREATE TABLE drones (
  droneID INT NOT NULL AUTO_INCREMENT
  , droneTypeID INT NOT NULL
  , droneStatus INT NOT NULL
  , PRIMARY KEY (droneID)
);

CREATE TABLE dronetypes (
  droneTypeID INT NOT NULL AUTO_INCREMENT
  , maxWeight FLOAT NOT NULL
  , maxPackageCount INT NOT NULL
  , maxRange FLOAT NOT NULL
  , maxSpeed FLOAT NOT NULL
  , PRIMARY KEY (droneTypeID)
);