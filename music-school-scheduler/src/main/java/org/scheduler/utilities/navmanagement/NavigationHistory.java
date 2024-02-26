package org.scheduler.utilities.navmanagement;

import javafx.scene.Scene;
import java.util.Stack;

public class NavigationHistory {
    private final Stack<SceneInfo> history = new Stack<>();

    public Stack<SceneInfo> getHistory() {
        return history;
    }

    public void push(Scene scene, String fxmlPath) {
        history.push(new SceneInfo(scene, fxmlPath));
    }

    public SceneInfo pop() {
        if (history.size() > 1) {
            return history.pop();
        }
        return history.peek();
    }

    public SceneInfo peek() {
        return history.peek();
    }

    public void clear() {
        history.clear();
    }
}

