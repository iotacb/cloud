package de.kostari.cloud.core.particles;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.particles.emitter.BoxEmitter;
import de.kostari.cloud.core.particles.emitter.CircleEmitter;
import de.kostari.cloud.core.particles.emitter.ParticleEmitter;
import de.kostari.cloud.utilities.math.Randoms;
import de.kostari.cloud.utilities.math.Vec;

public class ParticleSystem {

    private Particle particle;
    private ParticleEmitter emitter;

    private List<Particle> particles;
    private List<Particle> particlesToRemove;

    private int maxParticles;

    private boolean loopSystem;
    private boolean active;

    private boolean drawDebug;

    public ParticleSystem() {
        init(new Particle(), new CircleEmitter(100), 200);
    }

    public ParticleSystem(Particle particle) {
        init(particle, new CircleEmitter(100), 200);
    }

    public ParticleSystem(Particle particle, int maxParticles) {
        init(particle, new CircleEmitter(100), maxParticles);
    }

    public ParticleSystem(int maxParticles) {
        init(new Particle(), new CircleEmitter(100), maxParticles);
    }

    public ParticleSystem(Particle particle, ParticleEmitter emitter) {
        init(particle, emitter, 200);
    }

    public ParticleSystem(ParticleEmitter emitter, int maxParticles) {
        init(new Particle(), emitter, maxParticles);
    }

    public ParticleSystem(Particle particle, ParticleEmitter emitter, int maxParticles) {
        init(particle, emitter, maxParticles);
    }

    private void init(Particle particle, ParticleEmitter emitter, int maxParticles) {
        this.particle = particle;
        this.emitter = emitter;
        this.maxParticles = maxParticles;

        this.loopSystem = true;
        this.active = true;

        this.particles = new ArrayList<Particle>();
        this.particlesToRemove = new ArrayList<Particle>();
    }

    public void updateParticles(float delta) {

        // update all particles
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            particle.update(delta);

            if (particle.canDie()) {
                particlesToRemove.add(particle);
            }
        }

        // add particles to the system if the system is active and looping
        if (particles.size() < maxParticles && loopSystem && active) {
            // add particles with the emission factor (1 particle per frame * emission
            // factor)
            for (int i = 0; i < getEmitter().getEmissionFactor(); i++) {
                addParticle();
            }
        }

        for (int i = 0; i < particlesToRemove.size(); i++) {
            Particle particle = particlesToRemove.get(i);
            particles.remove(particle);
            particlesToRemove.remove(particle);
        }
    }

    public void drawParticles(float delta) {
        // draw all particles
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            particle.draw(delta);
        }

        if (!drawDebug)
            return;
        getEmitter().draw(delta);
    }

    public void addParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            addParticle();
        }
    }

    private void addParticle() {
        if (particle == null)
            return;

        Particle newParticle = null;
        try {
            // create a new instance of the given particle class
            newParticle = getParticle().getClass().getDeclaredConstructor().newInstance();
            newParticle.getStartTransform().position = getSpawnPosition();
            newParticle.setRenderIndex(particles.size());
            newParticle.create();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        if (newParticle == null)
            return;

        // add the particle instance to the particle system
        particles.add(newParticle);

        // sort the particles by their render index
        particles.sort((p1, p2) -> {
            return p1.getRenderIndex() - p2.getRenderIndex();
        });
    }

    /**
     * Get a position to spawn the particle at, based on the emitter
     * 
     * @return
     */
    private Vec getSpawnPosition() {
        if (getEmitter() instanceof BoxEmitter) {
            BoxEmitter e = (BoxEmitter) getEmitter();
            float randX = Randoms.randomFloat(e.getX(), e.getX() + e.getWidth());
            float randY = Randoms.randomFloat(e.getY(), e.getY() + e.getHeight());
            return new Vec(randX, randY);
        }
        return getEmitter().transform.position.clone();
    }

    public void setEmitter(ParticleEmitter emitter) {
        this.emitter = emitter;
    }

    public ParticleEmitter getEmitter() {
        return emitter;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setLoopSystem(boolean loopSystem) {
        this.loopSystem = loopSystem;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void pause() {
        setActive(false);
    }

    public void play() {
        setActive(true);
    }

    public Particle getParticle() {
        return particle;
    }

    public void drawDebug() {
        drawDebug = true;
        getEmitter().drawDebug();
    }

    public void setPosition(float x, float y) {
        getEmitter().transform.position.set(x, y);
    }

    public void setPosition(Vec pos) {
        getEmitter().transform.position.set(pos);
    }

}
