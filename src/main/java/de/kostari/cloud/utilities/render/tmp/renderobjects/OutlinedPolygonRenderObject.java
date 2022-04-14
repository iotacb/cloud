package de.kostari.cloud.utilities.render.tmp.renderobjects;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.tmp.RenderObject;

public class OutlinedPolygonRenderObject extends RenderObject {

    private int amountOfSides;

    public OutlinedPolygonRenderObject(float x, float y, float sideLength, float rotation, CColor color,
            int amountOfSides) {
        super(x, y, sideLength, 0, rotation, color, true);
        this.amountOfSides = amountOfSides;
    }

    @Override
    public Vec[] getVertices() {
        Vec[] vertices = new Vec[amountOfSides];

        int increment = 360 / amountOfSides;
        float currentAngle = -45;
        for (int i = 0; i < vertices.length; i++) {
            Vec tmp = new Vec(0, width);
            CMath.rotate(tmp, new Vec(), currentAngle);

            vertices[i] = new Vec(tmp).add(x, y);

            currentAngle += increment;
        }

        for (Vec v : vertices) {
            CMath.rotate(v, new Vec(x, y), rotation);
        }

        return vertices;
    }

}
