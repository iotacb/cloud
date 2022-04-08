package de.kostari.cloud.utilities.render;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;

public class RenderObject {

    public float x;
    public float y;
    public float width;
    public float height;
    public float rotation;

    public CColor color;
    public boolean outlined;

    public int amountOfSides;

    public RenderObject(float x, float y, float width, float height, float rotation, CColor color, boolean outlined,
            int amountOfSides) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.color = color;
        this.outlined = outlined;
        this.amountOfSides = amountOfSides;
    }

    public Vec[] getVertices() {
        Vec[] vertices = new Vec[isPolygon() ? amountOfSides : outlined ? 8 : 6];
        if (isPolygon()) {

            int increment = 360 / amountOfSides;
            float currentAngle = -45;
            for (int i = 0; i < vertices.length; i++) {
                Vec tmp = new Vec(0, width);
                CMath.rotate(tmp, new Vec(), currentAngle);

                vertices[i] = new Vec(tmp).add(x, y);

                currentAngle += increment;
            }

        } else {
            if (outlined) {
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
            } else {
                vertices = new Vec[] {
                        new Vec(x + width / 2, y - height / 2),
                        new Vec(x - width / 2, y - height / 2),
                        new Vec(x - width / 2, y + height / 2),
                        new Vec(x + width / 2, y - height / 2),
                        new Vec(x + width / 2, y + height / 2),
                        new Vec(x - width / 2, y + height / 2),
                };
            }
        }

        for (Vec v : vertices) {
            CMath.rotate(v, new Vec(x, y), rotation);
        }

        return vertices;
    }

    public boolean isPolygon() {
        return amountOfSides > 0;
    }

}
