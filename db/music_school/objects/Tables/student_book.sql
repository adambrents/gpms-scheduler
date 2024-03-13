CREATE TABLE `student_book` (
  `Student_Book_Id` int NOT NULL AUTO_INCREMENT,
  `Student_Id` int DEFAULT NULL,
  `Book_Id` int DEFAULT NULL,
  PRIMARY KEY (`Student_Book_Id`),
  KEY `fk_student_id_idx` (`Student_Id`),
  KEY `fk_book_id_idx` (`Book_Id`),
  CONSTRAINT `fk_book_id_studentbook` FOREIGN KEY (`Book_Id`) REFERENCES `books` (`Book_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_student_id_studentbook` FOREIGN KEY (`Student_Id`) REFERENCES `students` (`Student_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci