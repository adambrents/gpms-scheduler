package org.scheduler.utilities.navmanagement;

import javafx.scene.Scene;

public class SceneInfo {
    private Scene scene;
    private String fxmlPath;

    public SceneInfo(Scene scene, String fxmlPath) {
        this.scene = scene;
        this.fxmlPath = fxmlPath;
    }

    public Scene getScene() {
        return scene;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}

