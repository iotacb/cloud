package de.kostari.demo.balls;

import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.render.RenderType;

public class Main {

    public static void main(String[] args) throws Exception {
        Window window = new Window(800, 450, "Balls");

        window.setScene(BallScene.class);
        window.setRenderType(RenderType.BATCHED);

        window.show();
    }

}
