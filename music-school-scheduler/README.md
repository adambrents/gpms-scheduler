# Project C868 Adam_Brents 
- This project is an app to schedule and maintain lessons between teachers and students
## Authors
- [@adambrents](https://www.github.com/adambrents)
## 🚀 Contact Information
[adam.brents1@gmail.com](mailto:adam.brents1@gmail.com)
## Application Version  
v1.0
## Date
02-2024
## Prerequisites
1. MySQL Server 8.0 and MySQL Workbench 8.0 must be installed prior to running the application
2. All migration scripts under *\db\music_school\migration_scripts\C868_MigrationScripts must be executed
3. JDK 21 must be installed - preferably the Liberica JDK, which is what this was tested with: https://bell-sw.com/pages/downloads/#jdk-21-lts
4. Ensure IntelliJ is recognizing pom.xml files in a project as a Maven project
## IDE
IntelliJ Community 2023.1
## IDE Run Instructions
1. Run configuration "music-school-scheduler [clean, javafx:run]" in IntelliJ

## Build Instructions
1. Using the maven utility in Intellij, run the package utility
   1. Maven is configured in the pom.xml file, IntelliJ should automatically detect the file and a Maven option will be available somewhere on the right hand side of IntelliJ
2. This will output to ./target a portable windows executable

