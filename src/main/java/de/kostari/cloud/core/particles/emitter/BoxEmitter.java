package de.kostari.cloud.core.particles.emitter;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.render.Render;

public class BoxEmitter extends ParticleEmitter {

    private float width;
    private float height;

    public BoxEmitter(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(float delta) {
        if (!drawDebug)
            return;
        Render.rectOutlined(transform.position, getWidth(), getHeight(), 2, CColor.GREEN);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}