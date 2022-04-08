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

    public Circle(Window window, float radius, BodyType bodyType) {
        super(window);

        this.color = CColor.random();

        addComponent(new Bounds(radius, radius));
        addComponent(new Rigidbody().setBodyType(bodyType));
        addComponent(new CircleCollider());
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
        Render.push();
        Render.translate(transform.position);
        Render.rotate(transform.rotation);
        Render.circleCentered(0, 0, getComponent(Bounds.class).getWidth(), false,
                color);
        Render.pop();
        // Render.polygonOutlined(transform.position,
        // getComponent(Bounds.class).getWidth(), color, 20);
    }

    @Override
    public void update(float delta) {
        if (transform.position.y > window.getHeight() + 200) {
            destroy();
        }
        super.update(delta);
    }

}
