CREATE TABLE IF NOT EXISTS Tags (
        `ID` int NOT NULL AUTO_INCREMENT,
        `Name` varchar(45) NOT NULL,
        PRIMARY KEY (`ID`),
        UNIQUE KEY `Id_UNIQUE` (`ID`),
        UNIQUE KEY `Name_UNIQUE` (`Name`)
);