package org.scheduler.app.utilities.navmanagement;

import java.net.URL;
import java.util.Stack;

public class NavigationHistory {
    private final Stack<URL> history = new Stack<>();

    public Stack<URL> getHistory() {
        return history;
    }

    public void push(URL fxmlPath) {
        if(fxmlPath != null){
            history.push(fxmlPath);
        }
    }


    public URL pop() {
        if (!history.isEmpty()) {
            return history.pop();
        }
        return null;
    }

    public URL peek() {
        if (!history.isEmpty()) {
            return history.peek();
        }
        else {
            return null;
        }
    }

    public void clear() {
        history.clear();
    }
}

