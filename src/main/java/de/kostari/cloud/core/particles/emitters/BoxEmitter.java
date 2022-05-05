package de.kostari.cloud.core.particles.emitters;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class BoxEmitter extends Emitter {

    private Vec size = new Vec();

    public BoxEmitter(Vec size) {
        this.size = size;
    }

    public BoxEmitter(float width, float height) {
        this.size = new Vec(width, height);
    }

    public Vec getSize() {
        return size;
    }

    @Override
    public void draw(float delta) {
        Render.rectOutlined(pos, size, 3.0F, CColor.GREEN);
        super.draw(delta);
    }

}
