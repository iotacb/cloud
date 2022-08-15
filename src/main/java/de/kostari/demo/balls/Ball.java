package de.kostari.demo.balls;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.files.asset.assets.Image;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class Ball extends GameObject {

    private CColor color;
    private Vec speed;
    private int size;

    Image circle = Render.CIRCLE_SPRITE;

    public Ball(Window window) {
        super(window);

        this.color = CColor.random();
        this.speed = Vec.fromRange(2, 5, 2, 5);
        this.size = (int) (CMath.random() * 20 + 1);
        transform.position = Vec.fromRange(size, window.getWidth() - size, size, window.getHeight() - size);
    }

    @Override
    public void draw(float delta) {
        // Render.circle(transform.position, size * 2, color);
        Render.polygon(transform.position, size * 2, 0, size + 3, color);
        // Render.image(circle, transform.position, size, size, color);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        transform.position.add(speed);

        if (transform.position.x >= (getWindow().getWidth() - size) ||
                (transform.position.x <= size)) {
            speed.x *= -1;
        }
        if (transform.position.y >= (getWindow().getHeight() - size) ||
                (transform.position.y <= size)) {
            speed.y *= -1;
        }
        super.update(delta);
    }

}
