package de.kostari.cloud.core.particlesold.emitter;

import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.utilities.math.Vec;

public abstract class ParticleEmitter {

    private Transform transform;

    public ParticleEmitter() {
        this.transform = new Transform();
    }

    public Transform getTransform() {
        return transform;
    }

    public ParticleEmitter setPosition(float x, float y) {
        transform.position.set(x, y);
        return this;
    }

    public Vec getRandomVelocity() {
        return new Vec();
    }

}
