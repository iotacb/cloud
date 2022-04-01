package de.kostari.cloud.core.physics.raycast;

import de.kostari.cloud.utilities.math.Vec;

public class RaycastResult {

    private Vec point;
    private Vec normal;
    private float t;
    private boolean hit;

    public RaycastResult() {
        this.point = new Vec();
        this.normal = new Vec();
        this.t = -1;
        this.hit = false;
    }

    public void init(Vec point, Vec normal, float t, boolean hit) {
        this.point.set(point);
        this.normal.set(normal);
        this.t = t;
        this.hit = hit;
    }

    public static void reset(RaycastResult result) {
        if (result != null) {
            result.point.zero();
            result.normal.set(0, 0);
            result.t = -1;
            result.hit = false;
        }
    }

    // What is t?
    public float getT() {
        return t;
    }

    public boolean isHit() {
        return hit;
    }
}
