package de.kostari.cloud.core.particles.emitters;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.render.Render;

public class CircleEmitter extends Emitter {

    private float size;

    public CircleEmitter(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    @Override
    public void draw(float delta) {
        Render.circleOutlined(pos, size, 3.0F, CColor.GREEN);
        super.draw(delta);
    }

}
