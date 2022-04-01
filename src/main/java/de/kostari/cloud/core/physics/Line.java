package de.kostari.cloud.core.physics;

import de.kostari.cloud.utilities.math.Vec;

public class Line {

    private Vec from;
    private Vec to;

    public Line(Vec from, Vec to) {
        this.from = from;
        this.to = to;
    }

    public Vec getFrom() {
        return from;
    }

    public Vec getTo() {
        return to;
    }

    public Vec getStart() {
        return this.from;
    }

    public Vec getEnd() {
        return this.to;
    }

    public float getMagnitudeSq() {
        return new Vec(to).sub(from).getMagnitudeSq();
    }

}
