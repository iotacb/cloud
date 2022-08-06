package de.kostari.demo;

import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.render.RenderType;

public class App {

    public static void main(String[] args) {
        // Create a new window
        Window window = new Window(1280, 720, "Sand", true, false);

        // Enable vsync
        // window.setFPSCap(60);
        window.setScene(SandScene.class);

        window.setRenderType(RenderType.BATCHED);

        // Show the window
        try {
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}