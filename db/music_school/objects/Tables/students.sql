CREATE TABLE `students` (
  `Student_ID` int NOT NULL AUTO_INCREMENT,
  `Student_First_Name` varchar(50) DEFAULT NULL,
  `Student_Last_Name` varchar(50) DEFAULT NULL,
  `Address_Line_1` varchar(100) DEFAULT NULL,
  `Address_Line_2` varchar(100) DEFAULT NULL,
  `Postal_Code` varchar(50) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `State` varchar(50) DEFAULT NULL,
  `Phone` varchar(50) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Create_Date` datetime DEFAULT NULL,
  `Created_By` int DEFAULT NULL,
  `Last_Update` timestamp NULL DEFAULT NULL,
  `Last_Updated_By` int DEFAULT NULL,
  `Current_Book_Id` int DEFAULT NULL,
  `Current_Level_Id` int DEFAULT NULL,
  `Gold_Cup` bit(1) DEFAULT NULL,
  PRIMARY KEY (`Student_ID`),
  KEY `fk_book_id_idx` (`Current_Book_Id`),
  KEY `fk_level_id_idx` (`Current_Level_Id`),
  KEY `fk_last_updated_by_idx` (`Last_Updated_By`),
  KEY `fk_created_by_idx` (`Created_By`),
  CONSTRAINT `fk_created_by_students` FOREIGN KEY (`Created_By`) REFERENCES `users` (`User_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_current_book_id_students` FOREIGN KEY (`Current_Book_Id`) REFERENCES `books` (`Book_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_current_level_id_students` FOREIGN KEY (`Current_Level_Id`) REFERENCES `levels` (`Level_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_last_updated_by_students` FOREIGN KEY (`Last_Updated_By`) REFERENCES `users` (`User_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci