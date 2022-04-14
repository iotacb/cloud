package de.kostari.cloud.utilities.render.tmp;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.render.tmp.renderobjects.FilledRenderObject;
import de.kostari.cloud.utilities.render.tmp.renderobjects.OutlinedPolygonRenderObject;
import de.kostari.cloud.utilities.render.tmp.renderobjects.OutlinedRenderObject;
import de.kostari.cloud.utilities.render.tmp.renderobjects.PolygonRenderObject;
import de.kostari.cloud.utilities.render.tmp.renderobjects.UVRenderObject;

public class RenderBatch {

    private List<RenderObject> filledObjects;
    private List<RenderObject> outlinedObjects;
    private List<RenderObject> filledPolygonObjects;
    private List<RenderObject> outlinedPolygonObjects;
    private List<RenderObject> uvObjects;
    private boolean hasRoom;

    private int maxSize;

    private int renderIndex;

    public RenderBatch(int maxSize, int renderIndex) {
        this.filledObjects = new ArrayList<>();
        this.outlinedObjects = new ArrayList<>();
        this.filledPolygonObjects = new ArrayList<>();
        this.outlinedPolygonObjects = new ArrayList<>();
        this.uvObjects = new ArrayList<>();
        this.maxSize = maxSize;
        this.hasRoom = true;
        this.renderIndex = renderIndex;
    }

    public void render() {
        Render.start();
        Render.begin(GL11.GL_TRIANGLES);
        for (RenderObject renderObject : filledObjects) {
            Render.color(renderObject.color);
            for (Vec v : renderObject.getVertices()) {
                GL11.glVertex2f(v.x, v.y);
            }
            Render.color(CColor.WHITE);
        }

        Render.end();

        Render.begin(GL11.GL_TRIANGLE_FAN);
        for (RenderObject renderObject : filledPolygonObjects) {
            Render.color(renderObject.color);
            for (Vec v : renderObject.getVertices()) {
                GL11.glVertex2f(v.x, v.y);
            }
            Render.color(CColor.WHITE);
        }
        Render.end();

        Render.begin(GL11.GL_LINES);
        for (RenderObject renderObject : outlinedObjects) {
            Render.color(renderObject.color);
            for (Vec v : renderObject.getVertices()) {
                GL11.glVertex2f(v.x, v.y);
            }
            Render.color(CColor.WHITE);
        }
        Render.end();

        Render.begin(GL11.GL_LINE_LOOP);
        for (RenderObject renderObject : outlinedPolygonObjects) {
            Render.color(renderObject.color);
            for (Vec v : renderObject.getVertices()) {
                GL11.glVertex2f(v.x, v.y);
            }
            Render.color(CColor.WHITE);
        }
        Render.end();
        Render.stop();

        Render.push();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        Render.begin(GL11.GL_QUADS);
        for (RenderObject renderObject : uvObjects) {
            Render.color(renderObject.color);
            for (Vec[] vecs : renderObject.getVerticesUV()) {
                Vec pos = vecs[0];
                Vec uv = vecs[1];
                GL11.glTexCoord2f(uv.x, uv.y);
                GL11.glVertex2f(pos.x, pos.y);
            }
            Render.color(CColor.WHITE);
        }
        Render.end();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        Render.pop();
    }

    public void add(RenderObject renderObject) {
        if (renderObject instanceof PolygonRenderObject || renderObject instanceof OutlinedPolygonRenderObject) {
            if (renderObject instanceof OutlinedPolygonRenderObject) {
                outlinedPolygonObjects.add(renderObject);

                if (getUsedSpace() >= maxSize) {
                    hasRoom = false;
                }
            } else {
                filledPolygonObjects.add(renderObject);

                if (getUsedSpace() >= maxSize) {
                    hasRoom = false;
                }
            }
        } else if (renderObject instanceof OutlinedRenderObject || renderObject instanceof FilledRenderObject) {
            if (renderObject instanceof OutlinedRenderObject) {
                outlinedObjects.add(renderObject);

                if (getUsedSpace() >= maxSize) {
                    hasRoom = false;
                }
            } else {
                filledObjects.add(renderObject);

                if (getUsedSpace() >= maxSize) {
                    hasRoom = false;
                }
            }
        } else {
            if (renderObject instanceof UVRenderObject) {
                uvObjects.add(renderObject);

                if (getUsedSpace() >= maxSize) {
                    hasRoom = false;
                }
            }
        }
    }

    public void clearBatch() {
        filledObjects.clear();
        outlinedObjects.clear();
        filledPolygonObjects.clear();
        outlinedPolygonObjects.clear();
        uvObjects.clear();
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    public int getUsedSpace() {
        return filledObjects.size() + outlinedObjects.size() + filledPolygonObjects.size()
                + outlinedPolygonObjects.size() + uvObjects.size();
    }

    public int getRenderIndex() {
        return renderIndex;
    }

}
