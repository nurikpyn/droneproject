CREATE USER 'reekind_dronepr'@'%' IDENTIFIED WITH mysql_native_password;GRANT USAGE ON *.* TO 'reekind_dronepr'@'%' REQUIRE NONE WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;
SET PASSWORD FOR 'reekind_dronepr'@'%' = '***';
CREATE DATABASE IF NOT EXISTS `reekind_dronepr`;
GRANT ALL PRIVILEGES ON `reekind\_dronepr`.* TO 'reekind_dronepr'@'%';



DROP TABLE IF EXISTS orderHistory;
CREATE TABLE orderHistory (
  orderHistoryId INT NOT NULL,
  orderId INT NOT NULL,
  time TIMESTAMP NOT NULL,
  orderHistoryType INT NOT NULL,
  PRIMARY KEY(orderHistoryId)
);

DROP TABLE IF EXISTS depots;
create table depots
(
  depotId int auto_increment
    primary key,
  depotName varchar(150) not null,
  latitude double(9,6) not null,
  longitude double(9,6) not null
)
;
DROP TABLE IF EXISTS drones;
create table drones
(
  droneId int auto_increment
    primary key,
  droneTypeId int not null,
  droneStatus int not null,
  droneDepotId int not null,
  droneName varchar(150) null
)
;
DROP TABLE IF EXISTS dronetypes;
create table dronetypes
(
  droneTypeId int auto_increment
    primary key,
  maxWeight float not null,
  maxPackageCount int not null,
  maxRange float not null,
  maxWeightInGrams int not null,
  droneTypeName varchar(150) null,
  speed int null
)
;
DROP TABLE IF EXISTS locations;
create table locations
(
  locationId int auto_increment
    primary key,
  name varchar(250) null,
  latitude double(9,6) null,
  longitude double(9,6) null
)
;
DROP TABLE IF EXISTS orders;
create table orders
(
  orderId int auto_increment
    primary key,
  orderTime datetime default CURRENT_TIMESTAMP not null,
  adressId int null,
  orderStatus int not null,
  orderStopId int null,
  weight int null
)
;
DROP TABLE IF EXISTS routes;
create table routes
(
  routeID int auto_increment
    primary key,
  creationTime timestamp default CURRENT_TIMESTAMP not null,
  startTime timestamp default '0000-00-00 00:00:00' not null,
  endTime timestamp default '0000-00-00 00:00:00' not null,
  droneID int not null
)
;
DROP TABLE IF EXISTS routestops;
create table routestops
(
  routeStopId int auto_increment
    primary key,
  routeId int not null,
  locationId int not null
)
;
DROP TABLE IF EXISTS sample_orders;
create table sample_orders
(
  orderTime datetime null,
  deliveryPlace varchar(250) null,
  weight float null
)
  comment 'Beispieltabelle'
;
DROP TABLE IF EXISTS status;
create table status
(
  statusId int auto_increment
    primary key,
  type varchar(150) not null,
  typeStatusId int null,
  typeStatusValue varchar(250) null
)
;
DROP TABLE IF EXISTS userroles;
create table userroles
(
  userRoleId int auto_increment
    primary key,
  userRoleName varchar(150) null
)
;
DROP TABLE IF EXISTS users;
create table users
(
  userId int auto_increment
    primary key,
  name varchar(250) not null,
  password varchar(250) not null,
  userRoleId int not null
)
;

