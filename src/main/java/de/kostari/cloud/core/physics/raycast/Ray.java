package de.kostari.cloud.core.physics.raycast;

import de.kostari.cloud.utilities.math.Vec;

public class Ray {

    private Vec origin;
    private Vec direction;

    public Ray(Vec origin, Vec direction) {
        this.origin = origin;
        this.direction = direction;
        this.direction.normalize();
    }

    public Vec getOrigin() {
        return this.origin;
    }

    public Vec getDirection() {
        return this.direction;
    }

}
