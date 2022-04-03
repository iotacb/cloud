package de.kostari.demo;

import de.kostari.cloud.core.window.Window;
import de.kostari.demo.scenes.MainScene;

public class App {

    public static void main(String[] args) {
        // Create a variable for the window
        Window window = new Window(1280, 720, "Cloud Demo");

        // Set additional window hints
        window.setVSync(true);

        // Set the scene
        window.setScene(MainScene.class);

        try {
            // Show the window
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
