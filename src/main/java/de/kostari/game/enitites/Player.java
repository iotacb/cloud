package de.kostari.game.enitites;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.input.BasicControllings;
import de.kostari.cloud.utilities.render.Render;

public class Player extends GameObject {

    public Player(Window window) {
        super(window);
        addComponent(new Bounds(60, 60));
    }

    @Override
    public void update(float delta) {
        BasicControllings.moveWASD(this, 400 * delta);

        transform.rotation += 60 * delta;

        super.update(delta);
    }

    @Override
    public void draw(float delta) {
        Render.push();
        Render.translate(transform.position.center());
        Render.rotate(transform.rotation);
        Render.rect(getComponent(Bounds.class).getHalfSize().mirror(), getComponent(Bounds.class).size);
        Render.translate(transform.position.mirror().center());
        Render.pop();
        super.draw(delta);
    }

}
