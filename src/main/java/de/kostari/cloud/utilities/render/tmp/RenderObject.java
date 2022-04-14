package de.kostari.cloud.utilities.render.tmp;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.tmp.renderobjects.OutlinedRenderObject;

public abstract class RenderObject {

    public float x;
    public float y;
    public float width;
    public float height;
    public float rotation;

    public CColor color;

    private int renderIndex;

    public RenderObject(float x, float y, float width, float height, float rotation, CColor color, boolean outlined) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.color = color;
        this.renderIndex = 0;
    }

    public Vec[] getVertices() {
        return new Vec[0];
    }

    public Vec[][] getVerticesUV() {
        return new Vec[0][0];
    }

    public boolean isOutlined() {
        return this instanceof OutlinedRenderObject;
    }

    public int getRenderIndex() {
        return renderIndex;
    }

    public void setRenderIndex(int renderIndex) {
        this.renderIndex = renderIndex;
    }

}
