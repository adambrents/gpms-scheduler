CREATE TABLE `student_recitals` (
  `Student_Recital_Id` int NOT NULL AUTO_INCREMENT,
  `Student_ID` int NOT NULL,
  `Recital_Id` int NOT NULL,
  PRIMARY KEY (`Student_Recital_Id`),
  KEY `fk_student_id_idx` (`Student_ID`),
  KEY `fk_recital_id_idx` (`Recital_Id`),
  CONSTRAINT `fk_recital_id_student_recitals` FOREIGN KEY (`Recital_Id`) REFERENCES `recitals` (`Recital_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_student_id_student_recitals` FOREIGN KEY (`Student_ID`) REFERENCES `students` (`Student_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci