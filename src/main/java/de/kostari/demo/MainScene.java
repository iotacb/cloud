package de.kostari.demo;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.render.Image;

public class MainScene extends Scene {

    Image image;

    public MainScene(Window window) {
        super(window);

        this.image = new Image("hi.png");
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
    }

}
