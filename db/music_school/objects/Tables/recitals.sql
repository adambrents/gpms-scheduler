CREATE TABLE `recitals` (
  `Recital_Id` int NOT NULL AUTO_INCREMENT,
  `EventDate` datetime DEFAULT NULL,
  `EventDescription` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`Recital_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci