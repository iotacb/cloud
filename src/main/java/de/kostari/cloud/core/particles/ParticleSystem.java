package de.kostari.cloud.core.particles;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.particles.emitters.BoxEmitter;
import de.kostari.cloud.core.particles.emitters.CircleEmitter;
import de.kostari.cloud.core.particles.emitters.Emitter;
import de.kostari.cloud.core.particles.emitters.PointEmitter;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.files.asset.assets.Image;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.time.Timer;

public class ParticleSystem extends GameObject {

    private Vec particleStartDirection = new Vec();

    /**
     * Defines the speed of each particle
     */
    private Vec minParticleSpeed = new Vec(50, 50);
    private Vec maxParticleSpeed = new Vec(200, 200);

    /**
     * Defines the gravity of the particle system.
     */
    private Vec gravity = new Vec(0, 2);

    /**
     * Defines the color of each particle
     */
    private CColor startParticleColor = CColor.WHITE;
    private CColor endParticleColor = CColor.WHITE;

    /**
     * Defines the life time of each particle
     */
    private float minParticleLifeTime = 0;
    private float maxParticleLifeTime = 2;

    /**
     * Defines the size of each particle
     */
    private float minParticleSize = 5;
    private float maxParticleSize = 40;
    private float endParticleSize = 0;

    private float minParticleRotation = -360;
    private float maxParticleRotation = 360;
    private float endParticleRotation = 0;

    // Defines the start direction that each particle is moving
    private float spread = 360;

    /**
     * When shot is set to true, there will be only one emission cycle.
     * The particle system also has to be played manually.
     */
    private boolean shot = false;

    /**
     * Wherever the particles are emitted
     */
    private boolean emitting = true;

    /**
     * The amount of particles that will be emitted in one cycle
     * The cycle is defined by the max particle life time
     * Therefore one cycle is the max time that the particles will be alive
     */
    private int amount = 300;

    /**
     * Keeps track of the current emission cycle
     */
    private int currentCycle;

    private List<Particle> particles;

    private Particle particleToInstantiate;

    private Timer cycleTime = new Timer();

    private Emitter emitter = new PointEmitter();

    private List<Image> particleTextures = new ArrayList<>();

    public ParticleSystem(Window window) {
        super(window);
        this.particles = new ArrayList<>();
        this.particleToInstantiate = new Particle(window) {
        };
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            particle.update(delta);
            if (particle.isDead()) {
                particles.remove(i);
            }
        }

        if (!emitting)
            return;

        if (!shot) {
            // Amount of particles multiplied by current frame time
            if (getCurrentAmountOfParticles() < amount) {
                int particlesPerFrame = (int) Math.ceil(amount * delta);
                for (int i = 0; i < particlesPerFrame; i++) {
                    emitParticle();
                }
            }

        } else {
            if (currentCycle == 0) {
                emitCycle();
            }
        }

        super.update(delta);
    }

    @Override
    public void draw(float delta) {
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            particle.draw(delta);
        }
        if (getEmitter().drawDebug) {
            getEmitter().pos = transform.position;
            getEmitter().draw(delta);
        }
        super.draw(delta);
    }

    /**
     * Emit a particle cycle
     */
    public void emitCycle() {
        currentCycle++;
        for (int i = 0; i < amount; i++) {
            emitParticle();
        }
    }

    /**
     * Emit a new particle at the particle systems positon
     */
    public void emitParticle() {
        Particle particle = particleToInstantiate.clone();
        particle.transform.position.set(getEmitPosition());
        particle.setDirection(Vec.fromAngle(CMath.fromRange(0, spread)));
        particle.setSpeed(Vec.fromRange(minParticleSpeed, maxParticleSpeed));
        particle.setLifeTime(CMath.fromRange(minParticleLifeTime, maxParticleLifeTime));
        particle.setStartSize(CMath.fromRange(minParticleSize, maxParticleSize));
        particle.setStartRotation(CMath.fromRange(minParticleRotation, maxParticleRotation));
        particle.setStartColor(startParticleColor);
        particle.setEndColor(endParticleColor);
        particle.setEndSize(endParticleSize);
        particle.setGravity(gravity);
        Image texture = getRandomTexture();
        if (texture != null) {
            particle.setTexture(texture);
        }
        particle.start();
        particles.add(particle);
    }

    /**
     * Will play the particle system.
     * The emission cycle will be reset.
     * 
     * @param killOld Will kill all particles that are still alive
     */
    public void play(boolean killOld) {
        if (killOld) {
            particles.clear();
        }
        currentCycle = 0;
        cycleTime.reset();
    }

    public void clear() {
        currentCycle = 0;
        cycleTime.reset();
        particles.clear();
    }

    private Vec getEmitPosition() {
        if (emitter instanceof PointEmitter) {
            return transform.position;
        } else if (emitter instanceof CircleEmitter) {

            // Get a random position inside of the circle emitter
            CircleEmitter e = (CircleEmitter) emitter;
            float r = (float) ((e.getSize() / 2) * Math.sqrt(CMath.random()));
            float theta = CMath.random() * CMath.TAU;
            return transform.position.clone().add(Vec.fromPolar(r, theta));
        } else if (emitter instanceof BoxEmitter) {

            // Get a random position inside of the box emitter
            BoxEmitter e = (BoxEmitter) emitter;
            Vec pos = Vec.fromRange(new Vec(), e.getSize()).sub(e.getSize().clone().mul(0.5F));
            return transform.position.clone().add(pos);
        } else {
            return transform.position;
        }
    }

    private Image getRandomTexture() {
        if (particleTextures.isEmpty()) {
            return null;
        }
        return particleTextures.get((int) (CMath.random() * particleTextures.size()));
    }

    public float getMaxParticleLifeTime() {
        return maxParticleLifeTime;
    }

    public Vec getMaxParticleSpeed() {
        return maxParticleSpeed;
    }

    public float getMinParticleLifeTime() {
        return minParticleLifeTime;
    }

    public Vec getMinParticleSpeed() {
        return minParticleSpeed;
    }

    public Vec getParticleStartDirection() {
        return particleStartDirection;
    }

    public float getMaxParticleSize() {
        return maxParticleSize;
    }

    public int getAmount() {
        return amount;
    }

    public int getCurrentCycle() {
        return currentCycle;
    }

    public CColor getEndParticleColor() {
        return endParticleColor;
    }

    public float getEndParticleSize() {
        return endParticleSize;
    }

    public Vec getGravity() {
        return gravity;
    }

    public float getMinParticleSize() {
        return minParticleSize;
    }

    public CColor getStartParticleColor() {
        return startParticleColor;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public float getSpread() {
        return spread;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setEndParticleColor(CColor endParticleColor) {
        this.endParticleColor = endParticleColor;
    }

    public void setEndParticleSize(float endParticleSize) {
        this.endParticleSize = endParticleSize;
    }

    public void setGravity(Vec gravity) {
        this.gravity = gravity;
    }

    public void setMaxParticleLifeTime(float maxParticleLifeTime) {
        this.maxParticleLifeTime = maxParticleLifeTime;
    }

    public void setMaxParticleSize(float maxParticleSize) {
        this.maxParticleSize = maxParticleSize;
    }

    public void setMaxParticleSpeed(Vec maxParticleSpeed) {
        this.maxParticleSpeed = maxParticleSpeed;
    }

    public void setMinParticleLifeTime(float minParticleLifeTime) {
        this.minParticleLifeTime = minParticleLifeTime;
    }

    public void setMinParticleSize(float minParticleSize) {
        this.minParticleSize = minParticleSize;
    }

    public void setMinParticleSpeed(Vec minParticleSpeed) {
        this.minParticleSpeed = minParticleSpeed;
    }

    public void setParticleStartDirection(Vec particleStartDirection) {
        this.particleStartDirection = particleStartDirection;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

    public void setSpread(float spread) {
        this.spread = spread;
    }

    public void setStartParticleColor(CColor startParticleColor) {
        this.startParticleColor = startParticleColor;
    }

    public int getCurrentAmountOfParticles() {
        return particles.size();
    }

    public void setParticle(Particle particle) {
        this.particleToInstantiate = particle;
    }

    public void setEmitting(boolean emitting) {
        this.emitting = emitting;
    }

    public boolean isEmitting() {
        return emitting;
    }

    public Emitter getEmitter() {
        return emitter;
    }

    public void setEmitter(Emitter emitter) {
        this.emitter = emitter;
    }

    public List<Image> getParticleTextures() {
        return particleTextures;
    }

    public void addParticleTexure(Image particleTexture) {
        particleTextures.add(particleTexture);
    }

    public void addParticleTexures(Image... particleTextures) {
        for (int i = 0; i < particleTextures.length; i++) {
            this.particleTextures.add(particleTextures[i]);
        }
    }
}
