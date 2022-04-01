package de.kostari.game.enitites;

import java.awt.Color;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.physics.colliders.CircleCollider;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.game.components.player.HealthScript;
import de.kostari.game.components.player.PlayerScoreScript;
import de.kostari.game.components.player.PlayerScript;

public class Player extends GameObject {

    public Player(Window window, String tag) {
        super(window, tag);
        transform.position.set(window.getWidth() / 2, window.getHeight() / 2);
        addComponent(new Bounds(40, 60));
        addComponent(new CircleCollider());
        addComponent(new Transform());
        addComponent(new PlayerScript());
        addComponent(new PlayerScoreScript());
        addComponent(new HealthScript(200));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void draw(float delta) {
        Render.rectCentered(transform.position.x, transform.position.y, getComponent(Bounds.class).width,
                getComponent(Bounds.class).height, Color.DARK_GRAY);
        super.draw(delta);
    }

}
