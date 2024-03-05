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

    opens org.scheduler.app.program to javafx.fxml;
    opens org.scheduler.app.controller to javafx.fxml;
    opens org.scheduler.app.controller.student to javafx.fxml;
    opens org.scheduler.app.controller.report to javafx.fxml;
    opens org.scheduler.app.controller.lesson to javafx.fxml;
    opens org.scheduler.app.controller.base to javafx.fxml;

    opens org.scheduler.data.dto to javafx.base;
    opens org.scheduler.app.configuration.model to com.google.gson;
    opens org.scheduler.data.configuration to com.google.gson;
    exports org.scheduler.app.program;
    opens org.scheduler.data.repository.interfaces to javafx.base;
    opens org.scheduler.data.dto.interfaces to javafx.base;
    opens org.scheduler.data.dto.properties to javafx.base;
}