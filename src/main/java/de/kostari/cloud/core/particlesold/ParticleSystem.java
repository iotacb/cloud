package de.kostari.cloud.core.particlesold;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.kostari.cloud.core.particlesold.emitter.CircleEmitter;
import de.kostari.cloud.core.particlesold.emitter.ParticleEmitter;

public class ParticleSystem {

    private Particle particle;

    private ParticleEmitter emitter;

    private boolean loop;
    private boolean prewarm;

    private boolean playing;

    private int maxParticles;

    private List<Particle> particles;
    private List<Particle> particlesToRemove;

    public ParticleSystem(Particle particle, int amount) {
        init(particle, amount);
    }

    public ParticleSystem(Particle particle) {
        init(particle, 200);
    }

    private void init(Particle particle, int amount) {
        this.particle = particle;
        this.emitter = new CircleEmitter();

        this.playing = true;

        this.loop = true;
        this.prewarm = true;
        this.maxParticles = amount;

        this.particles = new ArrayList<>();
        this.particlesToRemove = new ArrayList<>();
    }

    public void update(float delta) {
        for (Particle particle : particles) {
            particle.update(delta);

            if (particle.isDead()) {
                particlesToRemove.add(particle);
            }
        }

        if (particles.size() < maxParticles && loop) {
            addParticle();
        }

        for (Particle particle : particlesToRemove) {
            particles.remove(particle);
        }
    }

    public void draw(float delta) {
        for (Particle particle : particles) {
            particle.draw(delta);
        }
    }

    public void spawnParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            addParticle();
        }
    }

    public void addParticle() {
        if (!isPlaying())
            return;
        try {
            Particle p;
            try {
                p = particle.getClass().getDeclaredConstructor().newInstance();
                if (p == null)
                    return;
                p.transform.position.set(emitter.getTransform().position.clone());
                p.setLifeTime(particle.getLifeTime());
                p.setSpawnRotation(particle.getSpawnRotation());
                p.setRotation(particle.getSpawnRotation());
                p.setRandomDirection(particle.isRandomRotateDirection());

                // p.setSpawnSize(particle.getSpawnSize());
                p.setSpawnSize(.1F + new Random().nextFloat() * 10);

                p.setSize(particle.getSpawnSize());

                // p.setSpawnSpeed(particle.getSpawnSpeed());
                p.setSpawnSpeed(particle.getSpawnSpeed() + (new Random().nextFloat() * 50));

                p.setSpeed(particle.getSpawnSpeed());
                p.setEndRotation(particle.getEndRotation(), particle.isRandomRotateDirection());
                p.setEndSize(particle.getEndSize());
                p.setEndSpeed(particle.getEndSpeed());
                p.setVelocity(emitter.getRandomVelocity());
                if (p != null)
                    particles.add(p);
            } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                    | SecurityException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ParticleSystem doLoop(boolean loop) {
        this.loop = loop;
        return this;
    }

    public ParticleSystem doPrewarm(boolean prewarm) {
        this.prewarm = prewarm;
        return this;
    }

    public ParticleSystem setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
        return this;
    }

    public ParticleSystem play() {
        this.playing = true;
        return this;
    }

    public ParticleSystem pause() {
        this.playing = false;
        return this;
    }

    public ParticleSystem setPlaying(boolean playing) {
        this.playing = playing;
        return this;
    }

    public int getMaxParticles() {
        return maxParticles;
    }

    public boolean isLooping() {
        return loop;
    }

    public boolean isPrewarmed() {
        return prewarm;
    }

    public boolean isPlaying() {
        return playing;
    }

    public ParticleEmitter getEmitter() {
        return emitter;
    }

    public List<Particle> getParticles() {
        return particles;
    }

}
