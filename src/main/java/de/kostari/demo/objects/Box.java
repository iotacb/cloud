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
import de.kostari.cloud.utilities.render.Render;

public class Box extends GameObject {

    private CColor color;

    private boolean moveable;

    public Box(Window window, float width, float height, BodyType bodyType, boolean spin, boolean moveable) {
        super(window);

        this.color = CColor.random();
        this.moveable = moveable;

        // Add components
        addComponent(new Bounds(width, height));
        addComponent(new Rigidbody().setBodyType(bodyType).setAngularVelocity(spin ? 1 : 0));
        addComponent(new BoxCollider());
    }

    @Override
    public void draw(float delta) {
        // Draw the box
        // Render.push();
        // Render.translate(transform.position);
        // Render.rotate(transform.rotation);
        // Render.rectCentered(0, 0, getComponent(Bounds.class).getSize(), false,
        // color);
        // Render.pop();

        // if (moveable) {
        // BoxCollider bc = getComponent(BoxCollider.class);
        // Rigidbody rb = getComponent(Rigidbody.class);

        // // Check if mouse if hovering the box
        // if (Collisions.pointInBox(Input.getMousePosition(), bc)) {
        // if (Input.getMouseButton(0)) {
        // // move box when the left mouse button is pressed
        // rb.getPhysicsBody().setTransform(new Vec2(Input.getMouseX(),
        // Input.getMouseY()),
        // rb.getPhysicsBody().getAngle());
        // }

        // // Draw a filled box when hovering
        // Render.push();
        // Render.translate(transform.position);
        // Render.rotate(transform.rotation);
        // Render.rectCentered(0, 0, getComponent(Bounds.class).getSize(), new
        // CColor(255, 255, 255, 100));
        // Render.pop();
        // }
        // }

        // Render.push();
        // Render.translate(transform.position);
        // Render.rotate(transform.rotation);
        // Render.rectCentered(0, 0, getComponent(Bounds.class).getSize(), false,
        // color);
        // Render.pop();
        Render.rectOutlined(transform.position, getComponent(Bounds.class).getSize(),
                transform.rotation, color);

        if (moveable) {
            BoxCollider bc = getComponent(BoxCollider.class);
            Rigidbody rb = getComponent(Rigidbody.class);

            // Check if mouse if hovering the box
            if (Collisions.pointInBox(Input.getMousePosition(), bc)) {
                if (Input.getMouseButton(0)) {
                    // move box when the left mouse button is pressed
                    rb.getPhysicsBody().setTransform(new Vec2(Input.getMouseX(), Input.getMouseY()),
                            rb.getPhysicsBody().getAngle());
                }

                // Draw a filled box when hovering
                // Render.push();
                // Render.translate(transform.position);
                // Render.rotate(transform.rotation);
                // Render.rectCentered(0, 0, getComponent(Bounds.class).getSize(), new
                // CColor(255, 255, 255, 100));
                // Render.pop();
                Render.rect(transform.position, getComponent(Bounds.class).getSize(),
                        transform.rotation, color);
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
