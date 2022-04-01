package de.kostari.game.components.player;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.RootPaneUI;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.components.Component;
import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.utilities.input.BasicControllings;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.game.enitites.Bullet;

public class PlayerScript extends Component {

    private static float MOVE_SPEED = 450;

    private static int KEY_UP = Keys.KEY_W;
    private static int KEY_DOWN = Keys.KEY_S;
    private static int KEY_LEFT = Keys.KEY_A;
    private static int KEY_RIGHT = Keys.KEY_D;

    private static int SHOOT_KEY = Keys.KEY_SPACE;

    private Transform bulletSpawn = new Transform();

    @Override
    public void start() {
        this.bulletSpawn = getComponent(Transform.class);
        super.start();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        BasicControllings.moveWithDetectedInput(gameObject, KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN, MOVE_SPEED * delta,
                0.01F,
                0);

        if (gameObject.window.getInput().getKeyDown(SHOOT_KEY)) {
            shoot();
        }

        updateSpawn();
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
        Render.circleCentered(bulletSpawn.position, 10);
    }

    private void updateSpawn() {
        bulletSpawn = gameObject.transform.copy();
        bulletSpawn.position.add(0, getComponent(Bounds.class).getHeight() / 2);
    }

    private void shoot() {
        gameObject.window.getScene().addObject(new GameObject(gameObject.window));
    }

}
