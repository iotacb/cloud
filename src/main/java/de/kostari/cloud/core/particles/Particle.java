package de.kostari.cloud.core.particles;

import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Randoms;
import de.kostari.cloud.utilities.render.Render;

public abstract class Particle {

    public Transform transform;

    private float lifeTime;
    private float lifeTimeCounter;

    private float spawnSize;
    private float endSize;
    private float size;

    private float spawnSpeed;
    private float endSpeed;
    private float speed;

    private float spawnRotation;
    private float endRotation;
    private float rotation;
    private boolean randomDirection;

    private float lifePercentage;

    public Particle() {
        this.lifeTime = 1;
        this.lifeTimeCounter = 0;

        this.spawnSize = 20;
        this.endSize = 0;
        this.size = spawnSize;

        this.spawnSpeed = 100;
        this.endSpeed = 0;
        this.speed = 100;

        this.spawnRotation = 0;
        this.endRotation = 0;
        this.rotation = spawnRotation;
        this.randomDirection = true;

        this.transform = new Transform();
    }

    public void update(float delta) {
        lifeTimeCounter += delta;
        lifePercentage = 1 - (lifeTimeCounter / lifeTime);

        updateSize(delta);
        updateSpeed(delta);
        updateRotation(delta);
    }

    public void draw(float delta) {
        Render.circle(transform.position, size, CColor.WHITE);
    }

    private void updateSize(float delta) {
        if (getSpawnSize() != getEndSize()) {
            size = (getSpawnSize() - getEndSize()) * lifePercentage;
        } else {
            size = getEndSize();
        }
    }

    private void updateSpeed(float delta) {
        if (getSpawnSpeed() != getEndSpeed()) {
            speed = (getSpawnSpeed() - getEndSpeed()) * lifePercentage;
        } else {
            speed = getEndSpeed();
        }
    }

    private void updateRotation(float delta) {
        if (getSpawnRotation() != getEndRotation()) {
            rotation = (getSpawnRotation() - getEndRotation()) * lifePercentage;
        } else {
            rotation = getEndRotation();
        }
    }

    public boolean isDead() {
        return lifeTimeCounter >= lifeTime;
    }

    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setSpawnSize(float spawnSize) {
        this.spawnSize = spawnSize;
    }

    public void setEndSize(float endSize) {
        this.endSize = endSize;
    }

    public void setEndRotation(float endRotation, boolean randomDirection) {
        this.endRotation = randomDirection ? Randoms.randomBoolean() ? -endRotation : endRotation : endRotation;
        this.randomDirection = randomDirection;
    }

    public void setEndSpeed(float endSpeed) {
        this.endSpeed = endSpeed;
    }

    public void setSpawnRotation(float spawnRotation) {
        this.spawnRotation = spawnRotation;
    }

    public void setSpawnSpeed(float spawnSpeed) {
        this.spawnSpeed = spawnSpeed;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setRandomDirection(boolean randomDirection) {
        this.randomDirection = randomDirection;
    }

    public float getEndSize() {
        return endSize;
    }

    public float getEndSpeed() {
        return endSpeed;
    }

    public float getLifePercentage() {
        return lifePercentage;
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public float getLifeTimeCounter() {
        return lifeTimeCounter;
    }

    public float getSize() {
        return size;
    }

    public float getSpawnSize() {
        return spawnSize;
    }

    public float getSpawnSpeed() {
        return spawnSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public float getEndRotation() {
        return endRotation;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean isRandomRotateDirection() {
        return randomDirection;
    }

    public float getSpawnRotation() {
        return spawnRotation;
    }

}
