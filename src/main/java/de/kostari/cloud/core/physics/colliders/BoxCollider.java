package de.kostari.cloud.core.physics.colliders;

import java.awt.Color;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.physics.Rigidbody;
import de.kostari.cloud.utilities.math.Maths;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class BoxCollider extends Collider {

    private Vec size = new Vec();
    private Vec halfSize = new Vec();

    @Override
    public void start() {
        if (gameObject != null) {
            Bounds bounds = gameObject.getComponent(Bounds.class);
            Rigidbody rigidbody = gameObject.getComponent(Rigidbody.class);

            if (bounds != null) {
                setSize(bounds.getSize());
            }

            if (rigidbody != null) {
                setRigidbody(rigidbody);
            }
        }
        super.start();
    }

    public Vec getLocalMin() {
        return new Vec(getRigidbody().gameObject.transform.position).sub(this.halfSize);
    }

    public Vec getLocalMax() {
        return new Vec(getRigidbody().gameObject.transform.position).add(this.halfSize);
    }

    public Vec getSize() {
        return size;
    }

    public Vec getHalfSize() {
        return halfSize;
    }

    public Vec[] getVertices() {
        Vec min = getLocalMin();
        Vec max = getLocalMax();

        Vec[] vertices = {
                new Vec(min.x, min.y), new Vec(min.x, max.y),
                new Vec(max.x, min.y), new Vec(max.x, max.y)
        };

        if (getRigidbody().gameObject.transform.rotation != 0.0f) {
            for (Vec vert : vertices) {
                // Rotates point(Vec) about center(Vec) by rotation(float in degrees)
                Maths.rotate(vert, getRigidbody().gameObject.transform.position,
                        getRigidbody().gameObject.transform.rotation);
            }
        }

        return vertices;
    }

    public BoxCollider setSize(Vec size) {
        this.size.set(size);
        this.halfSize.set(new Vec(size).mul(0.5F));
        return this;
    }

    @Override
    public void draw(float delta) {
        if (!gameObject.doDrawDebug())
            return;

        Render.lineWidth(2);
        Render.rect(gameObject.transform.position, getSize(), false, Color.GREEN);
        super.draw(delta);
    }

}
