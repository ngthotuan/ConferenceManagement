CREATE TABLE `conferences` (
  `id` varchar(20) PRIMARY KEY,
  `name` nvarchar(20),
  `shortDescription` nvarchar(50),
  `detailDescription` nvarchar(200),
  `image` nvarchar(20),
  `holdTime` datetime,
  `conferenceTime` int
);

CREATE TABLE `venueLocations` (
  `id` varchar(20) PRIMARY KEY,
  `name` nvarchar(20),
  `address` nvarchar(20),
  `limitPerson` int
);

CREATE TABLE `conferenceVenueLocations` (
  `conferenceId` varchar(20),
  `holdTime` datetime,
  `conferenceTime` int,
  `venueLocationId` varchar(20),
  PRIMARY KEY (`conferenceId`, `holdTime`, `conferenceTime`, `venueLocationId`)
);

CREATE TABLE `accounts` (
  `id` varchar(20) PRIMARY KEY,
  `name` nvarchar(20),
  `username` varchar(20),
  `password` varchar(20),
  `email` varchar(50),
  `isAdmin` boolean
);

CREATE TABLE `meetingAccounts` (
  `accountId` varchar(20),
  `conferenceId` varchar(20),
  PRIMARY KEY (`accountId`, `conferenceId`)
);

ALTER TABLE `conferenceVenueLocations` ADD FOREIGN KEY (`conferenceId`) REFERENCES `conferences` (`id`);

ALTER TABLE `conferenceVenueLocations` ADD FOREIGN KEY (`holdTime`) REFERENCES `conferences` (`holdTime`);

ALTER TABLE `conferenceVenueLocations` ADD FOREIGN KEY (`conferenceTime`) REFERENCES `conferences` (`conferenceTime`);

ALTER TABLE `conferenceVenueLocations` ADD FOREIGN KEY (`venueLocationId`) REFERENCES `venueLocations` (`id`);

ALTER TABLE `meetingAccounts` ADD FOREIGN KEY (`accountId`) REFERENCES `accounts` (`id`);

