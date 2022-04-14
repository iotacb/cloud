package de.kostari.demo.objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.physics.Collisions;
import de.kostari.cloud.core.physics.Rigidbody;
import de.kostari.cloud.core.physics.colliders.BoxCollider;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.math.Randoms;
import de.kostari.cloud.utilities.render.Render;

public class Box extends GameObject {

    private CColor color;

    private boolean moveable;

    private Bounds bounds;
    private BoxCollider boxCollider;
    private Rigidbody rigidbody;

    public Box(Window window, float width, float height, BodyType bodyType, boolean spin, boolean moveable) {
        super(window);

        this.color = CColor.random();
        this.moveable = moveable;

        // Add components
        this.bounds = addComponent(new Bounds(width, height));
        this.rigidbody = addComponent(
                new Rigidbody().setBodyType(bodyType).setAngularVelocity(spin ? 1 : 0).allowSleeping()
                        .setMass(Randoms.randomFloat(1, 20)));
        this.boxCollider = addComponent(new BoxCollider());
    }

    @Override
    public void draw(float delta) {
        Render.rectOutlined(transform.position, bounds.getSize(), 1,
                transform.rotation, color);

        if (moveable) {

            // Check if mouse if hovering the box
            if (Collisions.pointInBox(Input.getMousePosition(), boxCollider)) {
                if (Input.getMouseButton(0)) {
                    // move box when the left mouse button is pressed
                    rigidbody.getPhysicsBody().setTransform(new Vec2(Input.getMouseX(), Input.getMouseY()),
                            rigidbody.getPhysicsBody().getAngle());
                }
                Render.rect(transform.position, bounds.getSize(), transform.rotation, color);
            }
        }

        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        if (transform.position.y > window.getHeight() + 200) {
            destroy();
        }
        super.update(delta);
    }

}
