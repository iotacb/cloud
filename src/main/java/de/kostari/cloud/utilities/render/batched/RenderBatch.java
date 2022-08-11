package de.kostari.cloud.utilities.render.batched;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

import static de.kostari.cloud.utilities.render.Render.start;
import static de.kostari.cloud.utilities.render.Render.stop;
import static de.kostari.cloud.utilities.render.Render.begin;
import static de.kostari.cloud.utilities.render.Render.end;
import static de.kostari.cloud.utilities.render.Render.color;

public class RenderBatch {

    private List<RenderObject> filledObjects;
    private List<RenderObject> outlinedObjects;
    private List<RenderObject> filledPolygonObjects;
    private List<RenderObject> outlinedPolygonObjects;
    private boolean hasRoom;

    private int maxSize;

    public RenderBatch(int maxSize) {
        this.filledObjects = new ArrayList<>();
        this.outlinedObjects = new ArrayList<>();
        this.filledPolygonObjects = new ArrayList<>();
        this.outlinedPolygonObjects = new ArrayList<>();
        this.maxSize = maxSize;
        this.hasRoom = true;
    }

    public void render() {
        start();
        begin(GL11.GL_TRIANGLES);
        for (RenderObject renderObject : filledObjects) {
            color(renderObject.color);
            for (Vec v : renderObject.getVertices()) {
                GL11.glVertex2f(v.x, v.y);
            }
            color(CColor.WHITE);
        }

        end();

        begin(GL11.GL_TRIANGLE_FAN);
        for (RenderObject renderObject : filledPolygonObjects) {
            color(renderObject.color);
            for (Vec v : renderObject.getVertices()) {
                GL11.glVertex2f(v.x, v.y);
            }
            color(CColor.WHITE);
        }
        end();

        begin(GL11.GL_LINES);
        for (RenderObject renderObject : outlinedObjects) {
            color(renderObject.color);
            for (Vec v : renderObject.getVertices()) {
                GL11.glVertex2f(v.x, v.y);
            }
            color(CColor.WHITE);
        }
        end();

        Render.begin(GL11.GL_LINE_LOOP);
        for (RenderObject renderObject : outlinedPolygonObjects) {
            color(renderObject.color);
            for (Vec v : renderObject.getVertices()) {
                GL11.glVertex2f(v.x, v.y);
            }
            color(CColor.WHITE);
        }
        end();

        stop();
    }

    public void add(RenderObject renderObject) {
        if (renderObject.isPolygon()) {
            if (renderObject.outlined) {
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
        } else {
            if (renderObject.outlined) {
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
        }
    }

    public void clearBatch() {
        filledObjects.clear();
        outlinedObjects.clear();
        filledPolygonObjects.clear();
        outlinedPolygonObjects.clear();
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    public int getUsedSpace() {
        return filledObjects.size() + outlinedObjects.size() + filledPolygonObjects.size()
                + outlinedPolygonObjects.size();
    }

}