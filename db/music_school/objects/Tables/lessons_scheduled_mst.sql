CREATE TABLE `lessons_scheduled_mst` (
  `lessons_scheduled_mst_Id` int NOT NULL AUTO_INCREMENT,
  `lessons_scheduled_Id` int NOT NULL,
  PRIMARY KEY (`lessons_scheduled_mst_Id`),
  KEY `fk_lessons_scheduled_Id_idx` (`lessons_scheduled_Id`),
  CONSTRAINT `fk_lessons_scheduled_Id_lessons_scheduled_mst` FOREIGN KEY (`lessons_scheduled_Id`) REFERENCES `lessons_scheduled` (`lessons_scheduled_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci