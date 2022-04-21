package de.kostari.demo;

import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;

public class App {

    public static void main(String[] args) {
        // Create a new window
        Window window = new Window(1280, 720, "Boids Demo");

        // Enable vsync
        window.useVsync(true);
        window.setScene(MainScene.class);
        window.setClearColor(CColor.GRAY);

        // Show the window
        try {
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
