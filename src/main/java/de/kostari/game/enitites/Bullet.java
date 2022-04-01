package de.kostari.game.enitites;

import java.awt.Color;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class Bullet extends GameObject {

    private static float MOVE_SPEED = 100;

    private Vec direction = new Vec();

    public Bullet(Window window, Vec direction) {
        super(window);
        this.direction = direction;
    }

    @Override
    public void draw(float delta) {
        Render.circleCentered(transform.position, 5, Color.blue);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        transform.position.add(direction.clone().mul(MOVE_SPEED * delta));
        super.update(delta);
    }

}
