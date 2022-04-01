package de.kostari.cloud.core.components;

import de.kostari.cloud.utilities.math.Vec;

public class Transform extends Component {

    public Vec position;
    public Vec scale;
    public float rotation;

    public Transform() {
        init(new Vec(), new Vec(), 0);
    }

    public Transform(Transform transform) {
        init(new Vec(transform.position), new Vec(transform.scale), transform.rotation);
    }

    public Transform(Vec position) {
        init(position, new Vec(), 0);
    }

    public Transform(Vec position, Vec scale) {
        init(position, scale, 0);
    }

    public Transform(Vec position, Vec scale, float rotation) {
        init(position, scale, rotation);
    }

    public void init(Vec position, Vec scale, float rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
    }

    public Transform copy() {
        return new Transform(new Vec(this.position), new Vec(this.scale), this.rotation);
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Transform))
            return false;

        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}, %sdeg", position.x, position.y, rotation);
    }
}
