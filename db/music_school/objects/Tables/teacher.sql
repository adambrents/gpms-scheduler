CREATE TABLE `teachers` (
  `Teacher_Id` int NOT NULL AUTO_INCREMENT,
  `Teacher_First_Name` varchar(50) DEFAULT NULL,
  `Teacher_Last_Name` varchar(50) DEFAULT NULL,
  `Address_Line_1` varchar(100) DEFAULT NULL,
  `Address_Line_2` varchar(100) DEFAULT NULL,
  `Postal_Code` varchar(50) DEFAULT NULL,
  `Phone` varchar(50) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Create_Date` datetime DEFAULT NULL,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NULL DEFAULT NULL,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  `User_ID` int DEFAULT NULL,
  PRIMARY KEY (`Teacher_Id`),
  KEY `fk_user_id_idx` (`User_ID`),
  CONSTRAINT `fk_user_id_teachers` FOREIGN KEY (`User_ID`) REFERENCES `users` (`User_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci