package de.kostari.cloud.core.particles;

import java.util.Random;

import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Randoms;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class Particle {

    private Transform startTransform = new Transform();
    private Transform transform = new Transform();

    private Vec startSpeed = new Vec();
    private Vec endSpeed = new Vec();
    private Vec minSpeed = new Vec();
    private Vec maxSpeed = new Vec();
    private Vec speed = new Vec();

    private float startSize;
    private float endSize;
    private float minSize;
    private float maxSize;
    private float size;

    private float minLifetime;
    private float maxLifetime;
    private float lifetime;
    private float lifetimeCounter;
    private float liftimePercentage;

    private CColor startColor = CColor.WHITE;
    private CColor endColor = CColor.WHITE;
    private CColor color = CColor.WHITE;

    private Vec gravity = new Vec();

    private boolean canDie = false;

    private int renderIndex = 0;

    public Particle() {
        init(new Transform(), new Vec());
    }

    private void init(Transform startTransform, Vec moveSpeed) {
        this.startTransform = startTransform;
        this.transform = this.startTransform;
        this.speed = moveSpeed;
    }

    public void update(float delta) {
        updateVariables();

        // increase the lifetime
        this.lifetimeCounter += delta;
        // the passed lifetime in percentage
        this.liftimePercentage = this.lifetimeCounter / this.lifetime;

        if (lifetimeCounter > lifetime) {
            canDie = true;
        }

        speed.add(gravity);
        transform.position.add(speed.clone().mul(delta));
    }

    public void draw(float delta) {
        Render.circle(transform.position, size, color);
    }

    public void create() {
        // lifetime
        if (minLifetime != -1 && maxLifetime != -1) {
            lifetime = 2;
        } else {
            lifetime = Randoms.randomFloat(minSize, maxSize);
        }

        // speed
        if (!minSpeed.equals(endSpeed)) {
            startSpeed.set(Vec.fromAngle(new Random().nextInt() * 360));
        } else {
            float x = Randoms.randomFloat(minSpeed.x, maxSpeed.x);
            float y = Randoms.randomFloat(minSpeed.y, maxSpeed.y);
            startSpeed.set(x, y);
        }
        speed.set(startSpeed);
        endSpeed.set(startSpeed);

        // size
        if (minSize != -1 && maxSize != -1) {
            startSize = 20;
        } else {
            startSize = Randoms.randomFloat(minSize, maxSize);
        }
        size = startSize;
        endSize = startSize;

        // color
        startColor = CColor.BLUE;
        endColor = CColor.RED;
        color = startColor;
    }

    private void updateVariables() {
        updateColor();
        updateSize();
        updateSpeed();
    }

    private void updateColor() {
        if (startColor.equals(endColor))
            return;
        color = startColor.transition(endColor, getLiftimePercentage());
    }

    private void updateSize() {
        if (startSize == endSize)
            return;

        size = CMath.lerp(startSize, endSize, getLiftimePercentage());
    }

    private void updateSpeed() {
        if (startSpeed.equals(endSpeed))
            return;

        float lerpedX = CMath.lerp(startSpeed.x, endSpeed.x, getLiftimePercentage());
        float lerpedY = CMath.lerp(startSpeed.y, endSpeed.y, getLiftimePercentage());
        speed.set(lerpedX, lerpedY);
    }

    public Transform getTransform() {
        return transform;
    }

    public Transform getStartTransform() {
        return startTransform;
    }

    public float getStartSize() {
        return startSize;
    }

    public float getEndSize() {
        return endSize;
    }

    public float getMinSize() {
        return minSize;
    }

    public float getMaxSize() {
        return maxSize;
    }

    public float getSize() {
        return size;
    }

    public Vec getSpeed() {
        return speed;
    }

    public Vec getGravity() {
        return gravity;
    }

    public Vec getMinSpeed() {
        return minSpeed;
    }

    public Vec getMaxSpeed() {
        return maxSpeed;
    }

    public Vec getStartSpeed() {
        return startSpeed;
    }

    public Vec getEndSpeed() {
        return endSpeed;
    }

    public CColor getStartColor() {
        return startColor;
    }

    public CColor getEndColor() {
        return endColor;
    }

    public CColor getColor() {
        return color;
    }

    public float getLifetime() {
        return lifetime;
    }

    public float getLifetimeCounter() {
        return lifetimeCounter;
    }

    public float getMinLifetime() {
        return minLifetime;
    }

    public float getMaxLifetime() {
        return maxLifetime;
    }

    public float getLiftimePercentage() {
        return liftimePercentage;
    }

    public int getRenderIndex() {
        return renderIndex;
    }

    public boolean canDie() {
        return canDie;
    }

    public void setRenderIndex(int renderIndex) {
        this.renderIndex = renderIndex;
    }

    public void setStartSize(float startSize) {
        this.startSize = startSize;
    }

    public void setEndSize(float endSize) {
        this.endSize = endSize;
    }

    public void setMinSize(float minSize) {
        this.minSize = minSize;
    }

    public void setMaxSize(float maxSize) {
        this.maxSize = maxSize;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setSpeed(Vec speed) {
        this.speed = speed;
    }

    public void setGravity(Vec gravity) {
        this.gravity = gravity;
    }

    public void setMinSpeed(Vec minSpeed) {
        this.minSpeed.set(minSpeed);
    }

    public void setMaxSpeed(Vec maxSpeed) {
        this.maxSpeed.set(maxSpeed);
    }

    public void setStartSpeed(Vec startSpeed) {
        this.startSpeed.set(startSpeed);
    }

    public void setEndSpeed(Vec endSpeed) {
        this.endSpeed.set(endSpeed);
    }

    public void setStartColor(CColor startColor) {
        this.startColor = startColor;
    }

    public void setEndColor(CColor endColor) {
        this.endColor = endColor;
    }

    public void setColor(CColor color) {
        this.color = color;
    }

}
