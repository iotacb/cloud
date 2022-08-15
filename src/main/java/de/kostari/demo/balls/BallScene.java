package de.kostari.demo.balls;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.render.text.Fonts;

public class BallScene extends Scene {

    public BallScene(Window window) {
        super(window);

        for (int i = 0; i < 40000; i++) {
            addObject(new Ball(window));
        }
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
        Fonts.sans32.drawTextShadow(getWindow().getFPS() + " + FPS", 10, 10, CColor.WHITE);
    }

}
