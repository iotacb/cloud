package de.kostari.cloud.core.particlesold.emitter;

import de.kostari.cloud.utilities.math.Vec;

public class CircleEmitter extends ParticleEmitter {

    @Override
    public Vec getRandomVelocity() {
        // random angle in degrees
        float angle = (float) (Math.random() * 360);
        return Vec.fromAngle(angle);
    }

}
