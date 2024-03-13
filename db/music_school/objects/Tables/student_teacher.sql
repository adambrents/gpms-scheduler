CREATE TABLE `student_teacher` (
  `Student_Teacher_Id` int NOT NULL AUTO_INCREMENT,
  `Student_Id` int DEFAULT NULL,
  `Teacher_Id` int DEFAULT NULL,
  PRIMARY KEY (`Student_Teacher_Id`),
  KEY `fk_student_id_idx` (`Student_Id`),
  KEY `fk_teacher_id_idx` (`Teacher_Id`),
  CONSTRAINT `fk_student_id_studentteacher` FOREIGN KEY (`Student_Id`) REFERENCES `students` (`Student_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_teacher_id_studentteacher` FOREIGN KEY (`Teacher_Id`) REFERENCES `teachers` (`Teacher_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci