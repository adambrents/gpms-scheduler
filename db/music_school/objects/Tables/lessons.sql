CREATE TABLE `lessons` (
  `Lesson_ID` int NOT NULL AUTO_INCREMENT,
  `Create_Date` datetime DEFAULT NULL,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NULL DEFAULT NULL,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  `Student_ID` int NOT NULL,
  `User_ID` int NOT NULL,
  `Teacher_ID` int NOT NULL,
  PRIMARY KEY (`Lesson_ID`),
  KEY `fk_student_id_idx` (`Student_ID`),
  KEY `fk_user_id_idx` (`User_ID`),
  KEY `fk_teacher_id_idx` (`Teacher_ID`),
  CONSTRAINT `fk_student_id` FOREIGN KEY (`Student_ID`) REFERENCES `students` (`Student_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_teacher_id` FOREIGN KEY (`Teacher_ID`) REFERENCES `teachers` (`Teacher_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id_lessons` FOREIGN KEY (`User_ID`) REFERENCES `users` (`User_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci