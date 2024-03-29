package de.kostari.cloud.utilities.math;

public class Vec {

    public float x;
    public float y;

    public static Vec zero = new Vec(0, 0);
    public static Vec one = new Vec(1, 1);
    public static Vec up = new Vec(0, -1);
    public static Vec down = new Vec(0, 1);
    public static Vec left = new Vec(-1, 0);
    public static Vec right = new Vec(1, 0);

    void init(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec() {
    }

    public Vec(float val) {
        init(val, val);
    }

    public Vec(float x, float y) {
        init(x, y);
    }

    public Vec(Vec vec) {
        init(vec.x, vec.y);
    }

    public Vec set(float val) {
        this.x = val;
        this.y = val;
        return this;
    }

    public Vec set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec set(Vec vec) {
        this.x = vec.x;
        this.y = vec.y;
        return this;
    }

    public Vec add(float val) {
        this.x += val;
        this.y += val;
        return this;
    }

    public Vec add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec add(Vec vec) {
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    public Vec sub(float val) {
        this.x -= val;
        this.y -= val;
        return this;
    }

    public Vec sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec sub(Vec vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }

    public Vec mul(float val) {
        this.x *= val;
        this.y *= val;
        return this;
    }

    public Vec mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec mul(Vec vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        return this;
    }

    public Vec div(float val) {
        this.x /= val;
        this.y /= val;
        return this;
    }

    public Vec div(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vec div(Vec vec) {
        this.x /= vec.x;
        this.y /= vec.y;
        return this;
    }

    public float dot(float val) {
        return this.x * val + this.y * val;
    }

    public float dot(float x, float y) {
        return this.x * x + this.y * y;
    }

    public float dot(Vec vec) {
        return dot(vec.x, vec.y);
    }

    public float cross(float val) {
        return this.x * val - this.y * val;
    }

    public float cross(float x, float y) {
        return this.x * y - this.y * x;
    }

    public float cross(Vec vec) {
        return cross(vec.x, vec.y);
    }

    /**
     * Creates a vector that points into the given angle
     * 
     * @param angle the angle in degrees
     * @return
     */
    public static Vec fromAngle(float angle) {
        angle = (float) Math.toRadians(angle);
        float x = (float) Math.cos(angle);
        float y = (float) Math.sin(angle);
        return new Vec(x, y);
    }

    public static Vec fromRange(float min, float max) {
        float x = (float) Math.random() * (max - min) + min;
        float y = (float) Math.random() * (max - min) + min;
        return new Vec(x, y);
    }

    public static Vec fromRange(float minX, float maxX, float minY, float maxY) {
        float x = (float) Math.random() * (maxX - minX) + minX;
        float y = (float) Math.random() * (maxY - minY) + minY;
        return new Vec(x, y);
    }

    public static Vec fromRange(Vec min, Vec max) {
        float x = (float) Math.random() * (max.x - min.x) + min.x;
        float y = (float) Math.random() * (max.y - min.y) + min.y;
        return new Vec(x, y);
    }

    public Vec normalize() {
        float magnitude = getMagnitude();
        if (magnitude != 0 && magnitude != 1) {
            div(magnitude);
        }
        return this;
    }

    public Vec limit(float limit) {
        if (getMagnitude() > limit * limit) {
            normalize();
            mul(limit);
        }
        return this;
    }

    public float getMagnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float getMagnitudeSq() {
        return x * x + y * y;
    }

    public Vec setMagnitude(float magnitude) {
        normalize();
        mul(magnitude);
        return this;
    }

    public Vec clamp(float xMin, float xMax) {
        this.x = (x < xMin ? xMin : x > xMax ? xMax : x);
        return this;
    }

    public Vec clamp(float xMin, float xMax, float yMin, float yMax) {
        this.x = (x < xMin ? xMin : x > xMax ? xMax : x);
        this.y = (y < yMin ? yMin : y > yMax ? yMax : y);
        return this;
    }

    public Vec clamp(Vec min, Vec max) {
        this.x = (x < min.x ? min.x : x > max.x ? max.x : x);
        this.y = (y < min.y ? min.y : y > max.y ? max.y : y);
        return this;
    }

    @Override
    public Vec clone() {
        return new Vec(this);
    }

    public Vec mirror() {
        this.x *= -1;
        this.y *= -1;
        return this;
    }

    public Vec mirrorCopy() {
        return new Vec(this).mirror();
    }

    public Vec abs() {
        return new Vec(Math.abs(x), Math.abs(y));
    }

    public Vec neg() {
        return new Vec(Math.abs(x) * -1, Math.abs(y) * -1);
    }

    public Vec center() {
        return new Vec(x / 2, y / 2);
    }

    public Vec zero() {
        this.x = 0;
        this.y = 0;
        return this;
    }

    public float direction() {
        return (float) Math.atan2(y, x);
    }

    public float distance(float val) {
        float xDiff = this.x - val;
        float yDiff = this.y - val;
        return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    public float distance(float x, float y) {
        float xDiff = this.x - x;
        float yDiff = this.y - y;
        return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    public float distance(Vec vec) {
        return distance(vec.x, vec.y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getXi() {
        return (int) x;
    }

    public int getYi() {
        return (int) y;
    }

    public float getAxis(int axis) {
        switch (axis) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }
    }

    public Vec setAxis(int axis, float value) {
        switch (axis) {
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }
        return this;
    }

    public Vec rotate(Vec origin, float angleDeg) {
        CMath.rotate(this, origin, angleDeg);
        return this;
    }

    public static Vec fromPolar(float radius, float angle) {
        float x = radius * (float) Math.cos(angle);
        float y = radius * (float) Math.sin(angle);
        return new Vec(x, y);
    }

    public static Vec lerp(Vec a, Vec b, float t) {
        return new Vec(CMath.lerp(a.x, b.x, t), CMath.lerp(a.y, b.y, t));
    }

    public Vec lerp(Vec b, float t) {
        return new Vec(CMath.lerp(x, b.x, t), CMath.lerp(y, b.y, t));
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vec) {
            Vec v = (Vec) obj;
            return v.x == this.x && v.y == this.y;
        }
        return false;
    }

}