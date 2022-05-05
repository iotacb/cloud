package de.kostari.demo;

import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;

public class App {

    public static void main(String[] args) {
        // Create a new window
        Window window = new Window(1280, 720, "Boids Demo");

        // Enable vsync
        window.useVsync(true);
        // window.setScene(TilemapScene.class);
        window.setScene(ParticleScene.class);
        window.setClearColor(CColor.BLACK.setAlpha(20));

        // Show the window
        try {
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
