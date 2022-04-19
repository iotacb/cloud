package de.kostari.demo;

import java.util.Random;

import de.kostari.cloud.core.particles.Particle;
import de.kostari.cloud.utilities.math.Vec;

public class TestParticle extends Particle {

    @Override
    public void create() {
        super.create();
        System.out.println("df");
        setSpeed(Vec.fromAngle(new Random().nextInt() * 360));
    }

}
