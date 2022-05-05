package de.kostari.cloud.core.particles;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class Particle extends GameObject {

    private Vec direction = new Vec();
    private Vec speed = new Vec();
    private Vec gravity = new Vec();
    private Vec velocity = new Vec();

    private CColor startColor = CColor.WHITE;
    private CColor endColor = CColor.WHITE;
    private CColor currentColor = startColor;

    private float maxLifeTime;
    private float currentLifeTime;

    private float startSize;
    private float endSize;
    private float size;

    public Particle(Window window) {
        super(window);
    }

    public void start() {
        velocity = direction.clone().mul(speed);
    }

    @Override
    public void draw(float delta) {
        Render.circle(transform.position, size, currentColor);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        if (currentLifeTime <= maxLifeTime) {
            currentLifeTime += delta;
        }

        // Update the particle size of the life time
        size = (startSize - endSize) * (1.0F - getLifeTimePercentage());
        currentColor = CColor.transition(startColor, endColor, getLifeTimePercentage());

        moveParticle(delta);
        super.update(delta);
    }

    public float getMaxLifeTime() {
        return maxLifeTime;
    }

    public float getCurrentLifeTime() {
        return currentLifeTime;
    }

    public float getLifeTimePercentage() {
        return CMath.clamp01(currentLifeTime / maxLifeTime);
    }

    public boolean isDead() {
        return currentLifeTime >= maxLifeTime;
    }

    private void moveParticle(float delta) {
        velocity.add(gravity);
        transform.position.add(velocity.clone().mul(delta));
    }

    public void setLifeTime(float lifeTime) {
        this.maxLifeTime = lifeTime;
    }

    public void setDirection(Vec direction) {
        this.direction = direction;
    }

    public void setSpeed(Vec speed) {
        this.speed = speed;
    }

    public void setStartSize(float size) {
        this.startSize = size;
        this.size = size;
    }

    public void setEndSize(float size) {
        this.endSize = size;
    }

    public void setStartColor(CColor startColor) {
        this.startColor = startColor;
        this.currentColor = startColor;
    }

    public void setEndColor(CColor endColor) {
        this.endColor = endColor;
    }

    public void setGravity(Vec gravity) {
        this.gravity = gravity;
    }

    public void setMaxLifeTime(float maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public CColor getStartColor() {
        return startColor;
    }

    public CColor getEndColor() {
        return endColor;
    }

    public Vec getDirection() {
        return direction;
    }

    public Vec getSpeed() {
        return speed;
    }

    public float getSize() {
        return size;
    }

    public CColor getCurrentColor() {
        return currentColor;
    }

    public float getEndSize() {
        return endSize;
    }

    public Vec getGravity() {
        return gravity;
    }

    public float getStartSize() {
        return startSize;
    }

    public Vec getVelocity() {
        return velocity;
    }

    public Particle clone() {
        return new Particle(window) {
        };
    }

}
