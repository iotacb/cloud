package de.kostari.demo;

import de.kostari.cloud.core.window.Window;

public class App {

    public static void main(String[] args) throws Exception {
        // Create a new window
        Window window = new Window(1280, 720, "Physics Demo");

        // Enable vsync
        window.useVsync(true);

        // Set the scene
        window.setScene(MainScene.class);

        // Show the window
        window.show();
    }

}
