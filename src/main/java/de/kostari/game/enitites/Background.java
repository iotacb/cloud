package de.kostari.game.enitites;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.render.Image;

public class Background extends GameObject {

    private Image image = new Image("test.png");

    public Background(Window window) {
        super(window);
    }

    @Override
    public void draw(float delta) {
        image.drawImage(transform.position, window.getWidth(), window.getHeight());
        super.draw(delta);
    }

}
