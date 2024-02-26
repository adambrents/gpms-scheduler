module music.school.scheduler {
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.slf4j;
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires java.sql;
    requires com.google.gson;
    requires java.naming;
    requires mysql.connector.j;

    opens org.scheduler.program to javafx.fxml;
    opens org.scheduler.controller to javafx.fxml;
    opens org.scheduler.controller.student to javafx.fxml;
    opens org.scheduler.controller.report to javafx.fxml;
    opens org.scheduler.controller.lesson to javafx.fxml;
    opens org.scheduler.controller.base to javafx.fxml;

    opens org.scheduler.viewmodels to javafx.base;
    opens org.scheduler.configuration.model to com.google.gson;
    opens org.scheduler.repository.configuration.model to com.google.gson;
    exports org.scheduler.program;
}