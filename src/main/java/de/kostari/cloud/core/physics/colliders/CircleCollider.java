package de.kostari.cloud.core.physics.colliders;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.physics.Rigidbody;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class CircleCollider extends Collider {

    private float size;

    @Override
    public void start() {
        if (gameObject != null) {
            Bounds bounds = gameObject.getComponent(Bounds.class);
            Rigidbody rigidbody = gameObject.getComponent(Rigidbody.class);

            if (bounds != null) {
                setSize(Math.max(bounds.getWidth(), bounds.getHeight()));
            }

            if (rigidbody != null) {
                setRigidbody(rigidbody);
            }
        }
        super.start();
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Vec getCenter() {
        return getRigidbody().gameObject.transform.position;
    }

    @Override
    public void draw(float delta) {
        if (!gameObject.doDrawDebug())
            return;

        super.draw(delta);

        Render.lineWidth(2);
        Render.polygonOutlined(gameObject.transform.position, getSize(), CColor.GREEN, 180);
    }

}
