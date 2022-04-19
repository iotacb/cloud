package de.kostari.cloud.core.particles.emitter;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.render.Render;

public class CircleEmitter extends ParticleEmitter {

    private float size;

    public CircleEmitter(float size) {
        this.size = size;
    }

    @Override
    public void draw(float delta) {
        if (!drawDebug)
            return;
        Render.circleOutlined(transform.position, getSize(), 2, CColor.GREEN);
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

}
