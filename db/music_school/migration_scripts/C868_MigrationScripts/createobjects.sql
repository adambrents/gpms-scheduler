DROP DATABASE IF EXISTS music_school;

CREATE DATABASE IF NOT EXISTS music_school;

USE music_school;

DROP TABLE IF EXISTS student_instrument;
DROP TABLE IF EXISTS student_teacher;
DROP TABLE IF EXISTS teacher_instrument;
DROP TABLE IF EXISTS lessons_scheduled_mst;
DROP TABLE IF EXISTS lessons_scheduled;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS student_recitals;
DROP TABLE IF EXISTS recitals;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS levels;
DROP TABLE IF EXISTS instruments;

CREATE TABLE IF NOT EXISTS `books` (
  `Book_Id` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`Book_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `levels` (
  `Level_Id` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`Level_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `instruments` (
  `Instrument_Id` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`Instrument_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `recitals` (
  `Recital_Id` int NOT NULL AUTO_INCREMENT,
  `EventDate` datetime DEFAULT NULL,
  `EventDescription` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`Recital_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `students` (
  `Student_ID` int NOT NULL AUTO_INCREMENT,
  `Student_First_Name` varchar(50) DEFAULT NULL,
  `Student_Last_Name` varchar(50) DEFAULT NULL,
  `Address_Line_1` varchar(100) DEFAULT NULL,
  `Address_Line_2` varchar(100) DEFAULT NULL,
  `Postal_Code` varchar(50) DEFAULT NULL,
  `Phone` varchar(50) DEFAULT NULL,
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
  CONSTRAINT `fk_current_book_id_students` FOREIGN KEY (`Current_Book_Id`) REFERENCES `books` (`Book_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_current_level_id_students` FOREIGN KEY (`Current_Level_Id`) REFERENCES `levels` (`Level_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `student_recitals` (
  `Student_Recital_Id` int NOT NULL AUTO_INCREMENT,
  `Student_ID` int NOT NULL,
  `Recital_Id` int NOT NULL,
  PRIMARY KEY (`Student_Recital_Id`),
  KEY `fk_student_id_idx` (`Student_ID`),
  KEY `fk_recital_id_idx` (`Recital_Id`),
  CONSTRAINT `fk_student_id_student_recitals` FOREIGN KEY (`Student_ID`) REFERENCES `students` (`Student_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_recital_id_student_recitals` FOREIGN KEY (`Recital_Id`) REFERENCES `recitals` (`Recital_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `users` (
  `User_ID` int NOT NULL AUTO_INCREMENT,
  `User_Name` varchar(50) DEFAULT NULL,
  `Password` text,
  `Create_Date` datetime DEFAULT NULL,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NULL DEFAULT NULL,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  `Active` bit DEFAULT NULL,
  PRIMARY KEY (`User_ID`),
  UNIQUE KEY `User_Name_UNIQUE` (`User_Name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `teachers` (
  `Teacher_Id` int NOT NULL AUTO_INCREMENT,
  `Teacher_First_Name` varchar(50) DEFAULT NULL,
  `Teacher_Last_Name` varchar(50) DEFAULT NULL,
  `Address_Line_1` varchar(100) DEFAULT NULL,
  `Address_Line_2` varchar(100) DEFAULT NULL,
  `Postal_Code` varchar(50) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `State` varchar(50) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `teacher_instrument` (
  `Teacher_Instrument_Id` int NOT NULL AUTO_INCREMENT,
  `Teacher_Id` int DEFAULT NULL,
  `Instrument_Id` int DEFAULT NULL,
  PRIMARY KEY (`Teacher_Instrument_Id`),
  KEY `fk_teacher_id_idx` (`Teacher_Id`),
  KEY `fk_instrument_id_idx` (`Instrument_Id`),
  CONSTRAINT `fk_teacher_id_teacherinstrument` FOREIGN KEY (`Teacher_Id`) REFERENCES `teachers` (`Teacher_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_instrument_id_teacherinstrument` FOREIGN KEY (`Instrument_Id`) REFERENCES `instruments` (`Instrument_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `student_instrument` (
  `Student_Instrument_Id` int NOT NULL AUTO_INCREMENT,
  `Student_Id` int DEFAULT NULL,
  `Instrument_Id` int DEFAULT NULL,
  PRIMARY KEY (`Student_Instrument_Id`),
  KEY `fk_student_id_idx` (`Student_Id`),
  KEY `fk_instrument_id_idx` (`Instrument_Id`),
  CONSTRAINT `fk_student_id_studentinstrument` FOREIGN KEY (`Student_Id`) REFERENCES `students` (`Student_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_instrument_id_studentinstrument` FOREIGN KEY (`Instrument_Id`) REFERENCES `instruments` (`Instrument_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `student_teacher` (
  `Student_Teacher_Id` int NOT NULL AUTO_INCREMENT,
  `Student_Id` int DEFAULT NULL,
  `Teacher_Id` int DEFAULT NULL,
  PRIMARY KEY (`Student_Teacher_Id`),
  KEY `fk_student_id_idx` (`Student_Id`),
  KEY `fk_teacher_id_idx` (`Teacher_Id`),
  CONSTRAINT `fk_student_id_studentteacher` FOREIGN KEY (`Student_Id`) REFERENCES `students` (`Student_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_teacher_id_studentteacher` FOREIGN KEY (`Teacher_Id`) REFERENCES `teachers` (`Teacher_Id`) ON DELETE CASCADE ON UPDATE CASCADE
  ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `lessons` (
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
  CONSTRAINT `fk_teacher_id` FOREIGN KEY (`Teacher_ID`) REFERENCES `teachers` (`Teacher_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_student_id` FOREIGN KEY (`Student_ID`) REFERENCES `students` (`Student_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id_lessons` FOREIGN KEY (`User_ID`) REFERENCES `users` (`User_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `lessons_scheduled` (
  `lessons_scheduled_Id` int NOT NULL AUTO_INCREMENT,
  `Description` varchar(50) DEFAULT NULL,
  `Type` varchar(50) DEFAULT NULL,
  `Location` varchar(50) DEFAULT NULL,
  `Start` datetime DEFAULT NULL,
  `End` datetime DEFAULT NULL,
  `Create_Date` datetime DEFAULT NULL,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NULL DEFAULT NULL,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  `Gold_Cup` bit NOT NULL DEFAULT 0,
  `New_Student` bit NOT NULL DEFAULT 0,
  `Lesson_Book_Id` int NOT NULL,
  `Lesson_Level_Id` int NOT NULL,
  `Lesson_Instrument_Id` int NOT NULL,
  PRIMARY KEY (`lessons_scheduled_Id`),
  KEY `fk_book_id_idx` (`Lesson_Book_Id`),
  KEY `fk_level_id_idx` (`Lesson_Level_Id`),
  KEY `fk_instrument_id_idx` (`Lesson_Instrument_Id`),
  CONSTRAINT `fk_book_id_lessons` FOREIGN KEY (`Lesson_Book_Id`) REFERENCES `books` (`Book_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_level_id_lessons` FOREIGN KEY (`Lesson_Level_Id`) REFERENCES `levels` (`Level_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_instrument_id_lessons` FOREIGN KEY (`Lesson_Instrument_Id`) REFERENCES `instruments` (`Instrument_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `lessons_scheduled_mst` (
  `lessons_scheduled_mst_Id` int NOT NULL AUTO_INCREMENT,
  `lessons_scheduled_Id` int NOT NULL,
  PRIMARY KEY (`lessons_scheduled_mst_Id`),
  KEY `fk_lessons_scheduled_Id_idx` (`lessons_scheduled_Id`),
  CONSTRAINT `fk_lessons_scheduled_Id_lessons_scheduled_mst` FOREIGN KEY (`lessons_scheduled_Id`) REFERENCES `lessons_scheduled` (`lessons_scheduled_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `student_book` (
  `Student_Book_Id` int NOT NULL AUTO_INCREMENT,
  `Student_Id` int DEFAULT NULL,
  `Book_Id` int DEFAULT NULL,
  PRIMARY KEY (`Student_Book_Id`),
  KEY `fk_student_id_idx` (`Student_Id`),
  KEY `fk_book_id_idx` (`Book_Id`),
  CONSTRAINT `fk_student_id_studentbook` FOREIGN KEY (`Student_Id`) REFERENCES `students` (`Student_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_book_id_studentbook` FOREIGN KEY (`Book_Id`) REFERENCES `books` (`Book_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `student_level` (
  `Student_Level_Id` int NOT NULL AUTO_INCREMENT,
  `Student_Id` int DEFAULT NULL,
  `Level_Id` int DEFAULT NULL,
  PRIMARY KEY (`Student_Level_Id`),
  KEY `fk_student_id_idx` (`Student_Id`),
  KEY `fk_level_id_idx` (`Level_Id`),
  CONSTRAINT `fk_student_id_studentlevel` FOREIGN KEY (`Student_Id`) REFERENCES `students` (`Student_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_level_id_studentlevel` FOREIGN KEY (`Level_Id`) REFERENCES `levels` (`Level_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
