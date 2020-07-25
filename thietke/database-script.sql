DROP DATABASE ConferenceManagement;
CREATE DATABASE ConferenceManagement;
USE ConferenceManagement;
CREATE TABLE `Conference` (
                               `id` int PRIMARY KEY AUTO_INCREMENT,
                               `placeId` int,
                               `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                               `shortDescription` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                               `detailDescription` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                               `image` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                               `holdTime` datetime,
                               `conferenceTime` int,
                               `currentPerson` int DEFAULT 0,
                               `limitPerson` int
);

CREATE TABLE `Place` (
                         `id` int PRIMARY KEY AUTO_INCREMENT,
                         `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                         `address` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                         `limitPerson` int
);

CREATE TABLE `User` (
                         `username` varchar(20) PRIMARY KEY,
                         `password` varchar(50),
                         `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                         `email` varchar(50),
                         `isAdmin` bool DEFAULT false
                         `isBlocked` bool DEFAULT false
);

CREATE TABLE `MeetingAccount` (
                                   `userId` varchar(20),
                                   `conferenceId` int,
                                   `isAccepted` bool DEFAULT false,
                                   PRIMARY KEY (`userId`, `conferenceId`)
);

ALTER TABLE `Conference` ADD FOREIGN KEY (`placeId`) REFERENCES `Place` (`id`);

ALTER TABLE `MeetingAccount` ADD FOREIGN KEY (`userId`) REFERENCES `User` (`username`);

ALTER TABLE `MeetingAccount` ADD FOREIGN KEY (`conferenceId`) REFERENCES `Conference` (`id`);
