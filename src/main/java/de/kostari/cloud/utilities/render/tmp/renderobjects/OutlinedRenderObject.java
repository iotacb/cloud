package de.kostari.cloud.utilities.render.tmp.renderobjects;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.tmp.RenderObject;

public class OutlinedRenderObject extends RenderObject {

    public OutlinedRenderObject(float x, float y, float width, float height, float rotation, CColor color) {
        super(x, y, width, height, rotation, color, true);
    }

    @Override
    public Vec[] getVertices() {
        Vec[] vertices = new Vec[8];

        vertices = new Vec[] {
                new Vec(x - width / 2, y - height / 2),
                new Vec(x + width / 2, y - height / 2),
                new Vec(x + width / 2, y - height / 2),
                new Vec(x + width / 2, y + height / 2),
                new Vec(x + width / 2, y + height / 2),
                new Vec(x - width / 2, y + height / 2),
                new Vec(x - width / 2, y + height / 2),
                new Vec(x - width / 2, y - height / 2),
        };

        for (Vec v : vertices) {
            CMath.rotate(v, new Vec(x, y), rotation);
        }

        return vertices;
    }

}
