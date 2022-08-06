package de.kostari.cloud.utilities.render.batched;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.GL11;

import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;

public class BatchRender {

    public static Window window;

    public static void push() {
        glPushMatrix();
    }

    public static void pop() {
        glPopMatrix();
    }

    public static void enable(int glTarget) {
        glEnable(glTarget);
    }

    public static void disable(int glTarget) {
        glDisable(glTarget);
    }

    public static void start() {
        push();
        enable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        disable(GL_LIGHTING);
        disable(GL_TEXTURE_2D);
        disable(GL_CULL_FACE);
    }

    public static void stop() {
        enable(GL_CULL_FACE);
        enable(GL_TEXTURE_2D);
        enable(GL_LIGHTING);
        disable(GL_BLEND);
        pop();
    }

    public static void startSmooth() {
        enable(GL_POLYGON_SMOOTH);
        enable(GL_LINE_SMOOTH);
        enable(GL_POINT_SMOOTH);
        enable(GL_SMOOTH);
    }

    public static void stopSmooth() {
        disable(GL_SMOOTH);
        disable(GL_POINT_SMOOTH);
        disable(GL_LINE_SMOOTH);
        disable(GL_POLYGON_SMOOTH);
    }

    public static void begin(int glMode) {
        glBegin(glMode);
    }

    public static void end() {
        glEnd();
    }

    public static void vertex(float x, float y) {
        glVertex2f(x, y);
    }

    public static void vertex(Vec location) {
        vertex(location.x, location.y);
    }

    public static void translate(float x, float y) {
        glTranslated(x, y, 0);
    }

    public static void translate(Vec location) {
        translate(location.x, location.y);
    }

    public static void scale(float x, float y) {
        glScaled(x, y, 0);
    }

    public static void scale(Vec scaling) {
        scale(scaling.x, scaling.y);
    }

    public static void rotate(float x, float y, float z, float angle) {
        glRotated(angle, x, y, z);
    }

    public static void rotate(float angle) {
        rotate(0, 0, 1, angle);
    }

    public static void color(float red, float green, float blue, float alpha) {
        glColor4f(red, green, blue, alpha);
    }

    public static void color(float red, float green, float blue) {
        color(red, green, blue, 1);
    }

    public static void color(CColor color) {
        color(color.getRed01(), color.getGreen01(), color.getBlue01(), color.getAlpha01());
    }

    public static void lineWidth(float width) {
        glLineWidth((float) width);
    }

    public static void rect(Vec pos, Vec size, float rotation, CColor color) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, size.x, size.y, rotation, color, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(float x, float y, Vec size, float rotation, CColor color) {
        RenderObject renderObject = new RenderObject(x, y, size.x, size.y, rotation, color, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(Vec pos, float width, float height, float rotation, CColor color) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, width, height, rotation, color, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(float x, float y, float width, float height, float rotation, CColor color) {
        RenderObject renderObject = new RenderObject(x, y, width, height, rotation, color, false, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(Vec pos, Vec size, float rotation, CColor color) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, size.x, size.y, rotation, color, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(float x, float y, Vec size, float rotation, CColor color) {
        RenderObject renderObject = new RenderObject(x, y, size.x, size.y, rotation, color, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(Vec pos, float width, float height, float rotation, CColor color) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, width, height, rotation, color, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(float x, float y, float width, float height, float rotation, CColor color) {
        RenderObject renderObject = new RenderObject(x, y, width, height, rotation, color, true, 0);
        Renderer.add(renderObject);
    }

    public static void rect(Vec pos, Vec size, float rotation) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, size.x, size.y, rotation, CColor.WHITE, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(float x, float y, Vec size, float rotation) {
        RenderObject renderObject = new RenderObject(x, y, size.x, size.y, rotation, CColor.WHITE, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(Vec pos, float width, float height, float rotation) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, width, height, rotation, CColor.WHITE, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(float x, float y, float width, float height, float rotation) {
        RenderObject renderObject = new RenderObject(x, y, width, height, rotation, CColor.WHITE, false, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(Vec pos, Vec size, float rotation) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, size.x, size.y, rotation, CColor.WHITE, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(float x, float y, Vec size, float rotation) {
        RenderObject renderObject = new RenderObject(x, y, size.x, size.y, rotation, CColor.WHITE, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(Vec pos, float width, float height, float rotation) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, width, height, rotation, CColor.WHITE, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(float x, float y, float width, float height, float rotation) {
        RenderObject renderObject = new RenderObject(x, y, width, height, rotation, CColor.WHITE, true, 0);
        Renderer.add(renderObject);
    }

    public static void rect(Vec pos, Vec size, CColor color) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, size.x, size.y, 0, color, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(float x, float y, Vec size, CColor color) {
        RenderObject renderObject = new RenderObject(x, y, size.x, size.y, 0, color, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(Vec pos, float width, float height, CColor color) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, width, height, 0, color, false, 0);
        Renderer.add(renderObject);
    }

    public static void rect(float x, float y, float width, float height, CColor color) {
        RenderObject renderObject = new RenderObject(x, y, width, height, 0, color, false, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(Vec pos, Vec size, CColor color) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, size.x, size.y, 0, color, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(float x, float y, Vec size, CColor color) {
        RenderObject renderObject = new RenderObject(x, y, size.x, size.y, 0, color, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(Vec pos, float width, float height, CColor color) {
        RenderObject renderObject = new RenderObject(pos.x, pos.y, width, height, 0, color, true, 0);
        Renderer.add(renderObject);
    }

    public static void rectOutlined(float x, float y, float width, float height, CColor color) {
        RenderObject renderObject = new RenderObject(x, y, width, height, 0, color, true, 0);
        Renderer.add(renderObject);
    }

    public static void polygon(Vec pos, float len, float rotation, CColor color, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(pos.x, pos.y, len, 0, rotation, color, false, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygon(float x, float y, float len, float rotation, CColor color, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(x, y, len, 0, rotation, color, false, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygonOutlined(Vec pos, float len, float rotation, CColor color, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(pos.x, pos.y, len, 0, rotation, color, true, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygonOutlined(float x, float y, float len, float rotation, CColor color, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(x, y, len, 0, rotation, color, true, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygon(Vec pos, float len, float rotation, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(pos.x, pos.y, len, 0, rotation, CColor.WHITE, false,
                amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygon(float x, float y, float len, float rotation, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(x, y, len, 0, rotation, CColor.WHITE, false, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygonOutlined(Vec pos, float len, float rotation, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(pos.x, pos.y, len, 0, rotation, CColor.WHITE, true, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygonOutlined(float x, float y, float len, float rotation, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(x, y, len, 0, rotation, CColor.WHITE, true, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygon(Vec pos, float len, CColor color, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(pos.x, pos.y, len, 0, 0, color, false, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygon(float x, float y, float len, CColor color, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(x, y, len, 0, 0, color, false, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygonOutlined(float x, float y, float len, CColor color, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(x, y, len, 0, 0, color, true, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygonOutlined(Vec pos, float len, CColor color, int amountOfSides) {
        len /= 2;
        RenderObject renderObject = new RenderObject(pos.x, pos.y, len, 0, 0, color, true, amountOfSides);
        Renderer.add(renderObject);
    }

    public static void polygonOld(float x, float y, float sideLength, float amountOfSides, boolean filled,
            CColor color) {
        sideLength /= 2;
        start();
        if (color != null)
            color(color);
        else
            color(CColor.WHITE);
        begin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_LOOP);
        {
            for (float i = 0; i <= amountOfSides; i++) {
                float angle = i * CMath.TAU / amountOfSides;
                vertex(x + (sideLength * (float) Math.cos(angle)) + sideLength,
                        y + (sideLength * (float) Math.sin(angle)) + sideLength);
            }
        }
        end();
        stop();
    }

    public static void circleCentered(float x, float y, float radius, boolean filled, CColor color) {
        x -= radius / 2;
        y -= radius / 2;
        polygonOld(x, y, radius, 360, filled, color);
    }

}