CREATE TABLE `student_level` (
  `Student_Level_Id` int NOT NULL AUTO_INCREMENT,
  `Student_Id` int DEFAULT NULL,
  `Level_Id` int DEFAULT NULL,
  PRIMARY KEY (`Student_Level_Id`),
  KEY `fk_student_id_idx` (`Student_Id`),
  KEY `fk_level_id_idx` (`Level_Id`),
  CONSTRAINT `fk_level_id_studentlevel` FOREIGN KEY (`Level_Id`) REFERENCES `levels` (`Level_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_student_id_studentlevel` FOREIGN KEY (`Student_Id`) REFERENCES `students` (`Student_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci