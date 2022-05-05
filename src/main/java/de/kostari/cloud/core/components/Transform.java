package de.kostari.cloud.core.components;

import de.kostari.cloud.utilities.math.Vec;

public class Transform extends Component {

    public Vec position;
    public float rotation;

    public Transform() {
        init(new Vec(), 0);
    }

    public Transform(Transform transform) {
        init(new Vec(transform.position), transform.rotation);
    }

    public Transform(Vec position) {
        init(position, 0);
    }

    public Transform(Vec position, float rotation) {
        init(position, rotation);
    }

    public void init(Vec position, float rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Transform copy() {
        return new Transform(new Vec(this.position), this.rotation);
    }

    public void copy(Transform to) {
        to.position.set(this.position);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Transform))
            return false;

        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.rotation == this.rotation;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}, %sdeg", position.x, position.y, rotation);
    }
}
