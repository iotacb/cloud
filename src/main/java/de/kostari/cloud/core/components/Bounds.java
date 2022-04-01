package de.kostari.cloud.core.components;

import de.kostari.cloud.utilities.math.Vec;

public class Bounds extends Component {

    public float width, height;
    public Vec size;

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

    public float getHeight() {
        return height;
    }

    public Vec getSize() {
        return size;
    }

    public Vec getHalfSize() {
        return new Vec(size).mul(0.5F);
    }

}
