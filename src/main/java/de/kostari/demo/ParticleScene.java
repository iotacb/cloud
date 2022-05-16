package de.kostari.demo;

import de.kostari.cloud.core.particles.ParticleSystem;
import de.kostari.cloud.core.particles.emitters.BoxEmitter;
import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.text.Fonts;
import de.kostari.cloud.utilities.tileset.Tileset;

public class ParticleScene extends Scene {

    private Tileset tileset;
    private ParticleSystem particleSystem;

    public ParticleScene(Window window) {
        super(window);
        this.particleSystem = new ParticleSystem(window);
        this.tileset = new Tileset("tileset.png", 16, 16);

        // Set the emitter to be a box
        BoxEmitter emitter = new BoxEmitter(window.getWidth() * 2, 10);
        particleSystem.setEmitter(emitter);
        particleSystem.setMaxParticleSize(100);

        // Set the max life time of each particle
        particleSystem.setMaxParticleLifeTime(3);

        // Set the gravity of the particle system
        particleSystem.setGravity(new Vec(2, 2));

        // Set the amount of particles per cycle
        particleSystem.setAmount(400);

        // Set the min particle speed
        particleSystem.setMaxParticleSpeed(new Vec(200, 0));

        particleSystem.setStartParticleColor(CColor.GREEN);
        particleSystem.setEndParticleColor(CColor.MAGENTA);

        particleSystem.addParticleTexures(tileset.getTiles());

        // Update the particle systems position
        particleSystem.transform.position.set(0, -20);
    }

    @Override
    public void draw(float delta) {
        particleSystem.draw(delta);
        Fonts.sans32.drawTextShadow("FPS: " + getWindow().getFPS(), 12, 12);
        Fonts.sans32.drawTextShadow("Particles: " +
                particleSystem.getCurrentAmountOfParticles(), 12, 44);

        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        particleSystem.update(delta);

        if (Input.keyPressed(Keys.KEY_ESCAPE)) {
            System.exit(0);
        }
        super.update(delta);
    }

}
