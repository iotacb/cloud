package de.kostari.cloud.core.components;

import de.kostari.cloud.utilities.math.Vec;

public class Bounds extends Component {

    private float width, height;
    private Vec size = new Vec();

    public Bounds(float width, float height) {
        this.width = width;
        this.height = height;
        this.size = new Vec(width, height);
    }

    public Bounds(Vec size) {
        this.width = size.x;
        this.height = size.y;
        this.size = size;
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public float getWidth() {
        return width;
    }

    public float getHalfWidth() {
        return width / 2;
    }

    public float getHeight() {
        return height;
    }

    public float getHalfHeight() {
        return height / 2;
    }

    public Vec getSize() {
        return size;
    }

    public Vec getHalfSize() {
        return new Vec(size).mul(0.5F);
    }

    public Bounds setSize(Vec size) {
        this.size.set(size);
        this.width = size.x;
        this.height = size.y;
        return this;
    }

    public Bounds setSize(float width, float height) {
        this.size.set(width, height);
        this.width = size.x;
        this.height = size.y;
        return this;
    }

    public Bounds setWidth(float width) {
        this.size.x = width;
        this.width = width;
        return this;
    }

    public Bounds setHeight(float height) {
        this.size.y = height;
        this.height = height;
        return this;
    }

}
