CREATE DATABASE  IF NOT EXISTS `GiftShop`;
USE `GiftShop`;
-- Host: localhost    Database: GiftShop
-- ------------------------------------------------------

--
-- Table structure for table `CertificateDetails`
--
DROP TABLE IF EXISTS `CertificateDetails`;
CREATE TABLE `CertificateDetails` (
  `TagID` int NOT NULL,
  `CertificateID` int NOT NULL,
  PRIMARY KEY (`TagID`,`CertificateID`),
  KEY `FK_Certificate_Id_idx` (`CertificateID`),
  CONSTRAINT `FK_Certificate_Id` FOREIGN KEY (`CertificateID`) REFERENCES `GiftCertificates` (`ID`),
  CONSTRAINT `FK_Tag_Id` FOREIGN KEY (`TagID`) REFERENCES `Tags` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `GiftCertificates`
--
DROP TABLE IF EXISTS `GiftCertificates`;
CREATE TABLE `GiftCertificates` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Description` varchar(45) NOT NULL,
  `Price` DOUBLE NOT NULL,
  `CreateDate` datetime NOT NULL,
  `LastUpdateDate` datetime NOT NULL,
  `Duration` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `id_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `Tags`
--
DROP TABLE IF EXISTS `Tags`;
CREATE TABLE `Tags` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Id_UNIQUE` (`ID`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;