package de.kostari.demo.sandv1;

import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.render.RenderType;

public class App {

    public static void main(String[] args) {
        // Create a new window
        Window window = new Window(800, 800, "Sand", false, false);

        // gai&
        // Enable vsync
        window.setFpsCap(60);
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