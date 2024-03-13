CREATE TABLE `teacher_instrument` (
  `Teacher_Instrument_Id` int NOT NULL AUTO_INCREMENT,
  `Teacher_Id` int DEFAULT NULL,
  `Instrument_Id` int DEFAULT NULL,
  PRIMARY KEY (`Teacher_Instrument_Id`),
  KEY `fk_teacher_id_idx` (`Teacher_Id`),
  KEY `fk_instrument_id_idx` (`Instrument_Id`),
  CONSTRAINT `fk_instrument_id_teacherinstrument` FOREIGN KEY (`Instrument_Id`) REFERENCES `instruments` (`Instrument_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_teacher_id_teacherinstrument` FOREIGN KEY (`Teacher_Id`) REFERENCES `teachers` (`Teacher_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci