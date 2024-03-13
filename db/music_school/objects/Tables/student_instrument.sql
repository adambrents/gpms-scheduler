CREATE TABLE `student_instrument` (
  `Student_Instrument_Id` int NOT NULL AUTO_INCREMENT,
  `Student_Id` int DEFAULT NULL,
  `Instrument_Id` int DEFAULT NULL,
  PRIMARY KEY (`Student_Instrument_Id`),
  KEY `fk_student_id_idx` (`Student_Id`),
  KEY `fk_instrument_id_idx` (`Instrument_Id`),
  CONSTRAINT `fk_instrument_id_studentinstrument` FOREIGN KEY (`Instrument_Id`) REFERENCES `instruments` (`Instrument_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_student_id_studentinstrument` FOREIGN KEY (`Student_Id`) REFERENCES `students` (`Student_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci