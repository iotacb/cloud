package de.kostari.cloud.core.particles.emitter;

import de.kostari.cloud.core.components.Transform;

public abstract class ParticleEmitter {

    public Transform transform;

    private int emissionFactor;

    public boolean drawDebug;

    public ParticleEmitter() {
        this.transform = new Transform();
        this.emissionFactor = 1;
    }

    public abstract void draw(float delta);

    public int getEmissionFactor() {
        return emissionFactor;
    }

    public void setEmissionFactor(int emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public float getX() {
        return transform.position.x;
    }

    public float getY() {
        return transform.position.y;
    }

    public void drawDebug() {
        drawDebug = true;
    }

}
