package org.scheduler.app.models.errors;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PossibleError {
    private String propertyName;
    private String propertyValue;
    private Label propertyLabel;

    public PossibleError(String propertyName, String propertyValue, Label propertyLabel) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.propertyLabel = propertyLabel;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public Label getPropertyLabel() {
        return propertyLabel;
    }
}
