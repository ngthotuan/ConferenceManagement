CREATE TABLE `conferences` (
  `id` varchar(20) PRIMARY KEY,
  `name` nvarchar(20),
  `shortDescription` nvarchar(50),
  `detailDescription` nvarchar(200),
  `image` nvarchar(20),
  `holdTime` datetime,
  `conferencetime` int
);

CREATE TABLE `vanueLocations` (
  `id` varchar(20) PRIMARY KEY,
  `name` nvarchar(20),
  `address` nvarchar(20),
  `limitPerson` int
);

CREATE TABLE `conferenceVanueLocations` (
  `conferenceId` varchar(20),
  `holdTime` datetime,
  `conferencetime` int,
  `vanueLocationId` varchar(20),
  PRIMARY KEY (`conferenceId`, `holdTime`, `conferencetime`, `vanueLocationId`)
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

ALTER TABLE `conferenceVanueLocations` ADD FOREIGN KEY (`conferenceId`) REFERENCES `conferences` (`id`);

ALTER TABLE `conferenceVanueLocations` ADD FOREIGN KEY (`holdTime`) REFERENCES `conferences` (`holdTime`);

ALTER TABLE `conferenceVanueLocations` ADD FOREIGN KEY (`conferencetime`) REFERENCES `conferences` (`conferencetime`);

ALTER TABLE `conferenceVanueLocations` ADD FOREIGN KEY (`vanueLocationId`) REFERENCES `vanueLocations` (`id`);

ALTER TABLE `meetingAccounts` ADD FOREIGN KEY (`accountId`) REFERENCES `accounts` (`id`);

ALTER TABLE `meetingAccounts` ADD FOREIGN KEY (`conferenceId`) REFERENCES `conferences` (`id`);

