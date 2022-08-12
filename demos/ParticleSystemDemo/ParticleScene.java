import de.kostari.cloud.core.particles.ParticleSystem;
import de.kostari.cloud.core.particles.emitters.BoxEmitter;
import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.text.Fonts;

public class ParticleScene extends Scene {

    private ParticleSystem particleSystem;

    public ParticleScene(Window window) {
        super(window);
        this.particleSystem = new ParticleSystem(window);

        // Set the emitter to be a box
        BoxEmitter emitter = new BoxEmitter(window.getWidth() * 2, 10);
        particleSystem.setEmitter(emitter);

        // Set the max life time of each particle
        particleSystem.setMaxParticleLifeTime(3);

        // Set the gravity of the particle system
        particleSystem.setGravity(new Vec(2, 2));

        // Set the amount of particles per cycle
        particleSystem.setAmount(600);

        // Set the min particle speed
        particleSystem.setMaxParticleSpeed(new Vec(200, 0));

        // Update the particle systems position
        particleSystem.transform.position.set(0, -20);
    }

    @Override
    public void draw(float delta) {
        // Draw the particles
        particleSystem.draw(delta);

        // Draw information
        Fonts.sans32.drawTextShadow("FPS: " + getWindow().getFPS(), 12, 12);
        Fonts.sans32.drawTextShadow("Particles: " +
                particleSystem.getCurrentAmountOfParticles(), 12, 44);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        // Update the particles
        particleSystem.update(delta);
        super.update(delta);
    }

}
