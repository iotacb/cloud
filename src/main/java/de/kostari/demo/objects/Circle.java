package de.kostari.demo.objects;

import org.jbox2d.dynamics.BodyType;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.physics.Rigidbody;
import de.kostari.cloud.core.physics.colliders.CircleCollider;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.render.Render;

public class Circle extends GameObject {

    private CColor color;

    private Bounds bounds;

    public Circle(Window window, float radius, BodyType bodyType) {
        super(window);

        this.color = CColor.random();

        this.bounds = addComponent(new Bounds(radius, radius));
        addComponent(new Rigidbody().setBodyType(bodyType));
        addComponent(new CircleCollider());
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
        Render.push();
        Render.translate(transform.position);
        Render.rotate(transform.rotation);
        Render.circleOutlined(0, 0, bounds.getWidth(), 1, color);
        Render.pop();
    }

    @Override
    public void update(float delta) {
        if (transform.position.y > window.getHeight() + 200) {
            destroy();
        }
        super.update(delta);
    }

}
