package de.kostari.cloud.utilities.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import de.kostari.Cloud;
import de.kostari.cloud.core.scene.Camera;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.files.asset.assets.Image;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.batched.RenderObject;
import de.kostari.cloud.utilities.render.batched.Renderer;

public class Render {

    public static Window window;

    public static final Image SQUARE_SPRITE = new Image(Cloud.class.getResourceAsStream("../../assets/square.png"));
    public static final Image CIRCLE_SPRITE = new Image(Cloud.class.getResourceAsStream("../../assets/circle.png"));

    public static boolean isBatching() {
        return window.getRenderType() == RenderType.BATCHED;
    }

    public static boolean isPixeling() {
        return window.getRenderType() == RenderType.PIXELS;
    }

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

    public static void beginCam(Camera cam) {
        GL11.glLoadIdentity();
        GL11.glMultMatrixf(CMath.MatrixToFloat(cam.getMatrix()));
        GL11.glMultMatrixf(CMath.MatrixToFloat(window.getScreenScale()));
    }

    public static void endCam() {
        GL11.glLoadIdentity();
        GL11.glMultMatrixf(CMath.MatrixToFloat(window.getScreenScale()));
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

    public static void rect(float x, float y, float width, float height, float rotation, boolean filled,
            float lineWidth,
            CColor color) {

        if (isBatching()) {
            RenderObject renderObject = new RenderObject(x, y, width, height, rotation, color, false, 0);
            Renderer.add(renderObject);
            return;
        }

        if (color != null)
            color(color);
        else
            color(CColor.WHITE);
        if (!filled) {
            lineWidth(lineWidth);
        }

        Vec[] vertices = new Vec[] {
                new Vec(x - width / 2, y - height / 2),
                new Vec(x + width / 2, y - height / 2),
                new Vec(x + width / 2, y + height / 2),
                new Vec(x - width / 2, y + height / 2)
        };

        start();
        begin(filled ? GL_QUADS : GL_LINE_LOOP);
        {
            for (Vec v : vertices) {
                if (rotation != 0) {
                    CMath.rotate(v, new Vec(x, y), rotation);
                }
                vertex(v.x, v.y);
            }
        }
        end();
        color(CColor.WHITE);
        stop();
    }

    public static void rect(float x, float y, float width, float height, float rotation, CColor color) {
        rect(x, y, width, height, rotation, true, 0, color);
    }

    public static void rectOutlined(float x, float y, float width, float height, float rotation, float lineWidth,
            CColor color) {
        rect(x, y, width, height, rotation, false, lineWidth, color);
    }

    public static void rect(float x, float y, float width, float height, CColor color) {
        rect(x, y, width, height, 0, true, 0, color);
    }

    public static void rectOutlined(float x, float y, float width, float height, float lineWidth,
            CColor color) {
        rect(x, y, width, height, 0, false, lineWidth, color);
    }

    public static void rect(Vec pos, float width, float height, float rotation, CColor color) {
        rect(pos.x, pos.y, width, height, rotation, color);
    }

    public static void rectOutlined(Vec pos, float width, float height, float rotation, float lineWidth, CColor color) {
        rectOutlined(pos.x, pos.y, width, height, rotation, lineWidth, color);
    }

    public static void rect(float x, float y, Vec size, float rotation, CColor color) {
        rect(x, y, size.x, size.y, rotation, color);
    }

    public static void rectOutlined(float x, float y, Vec size, float rotation, float lineWidth, CColor color) {
        rectOutlined(x, y, size.x, size.y, rotation, lineWidth, color);
    }

    public static void rect(Vec pos, Vec size, float rotation, CColor color) {
        rect(pos.x, pos.y, size.x, size.y, rotation, color);
    }

    public static void rectOutlined(Vec pos, Vec size, float lineWidth, float rotation, CColor color) {
        rectOutlined(pos.x, pos.y, size.x, size.y, rotation, lineWidth, color);
    }

    public static void rect(Vec pos, float width, float height, float rotation) {
        rect(pos.x, pos.y, width, height, rotation, CColor.WHITE);
    }

    public static void rectOutlined(Vec pos, float width, float height, float rotation, float lineWidth) {
        rectOutlined(pos.x, pos.y, width, height, rotation, lineWidth, CColor.WHITE);
    }

    public static void rect(float x, float y, Vec size, float rotation) {
        rect(x, y, size.x, size.y, rotation, CColor.WHITE);
    }

    public static void rectOutlined(float x, float y, Vec size, float rotation, float lineWidth) {
        rectOutlined(x, y, size.x, size.y, rotation, lineWidth, CColor.WHITE);
    }

    public static void rect(Vec pos, Vec size, float rotation) {
        rect(pos.x, pos.y, size.x, size.y, rotation, CColor.WHITE);
    }

    public static void rectOutlined(Vec pos, Vec size, float rotation, float lineWidth) {
        rectOutlined(pos.x, pos.y, size.x, size.y, rotation, lineWidth, CColor.WHITE);
    }

    public static void rect(Vec pos, float width, float height, CColor color) {
        rect(pos.x, pos.y, width, height, color);
    }

    public static void rectOutlined(Vec pos, float width, float height, float lineWidth, CColor color) {
        rectOutlined(pos.x, pos.y, width, height, lineWidth, color);
    }

    public static void rect(float x, float y, Vec size, CColor color) {
        rect(x, y, size.x, size.y, color);
    }

    public static void rectOutlined(float x, float y, Vec size, float lineWidth, CColor color) {
        rectOutlined(x, y, size.x, size.y, lineWidth, color);
    }

    public static void rect(Vec pos, Vec size, CColor color) {
        rect(pos.x, pos.y, size.x, size.y, color);
    }

    public static void rectOutlined(Vec pos, Vec size, float lineWidth, CColor color) {
        rectOutlined(pos.x, pos.y, size.x, size.y, lineWidth, color);
    }

    public static void rect(Vec pos, float width, float height) {
        rect(pos.x, pos.y, width, height, CColor.WHITE);
    }

    public static void rectOutlined(Vec pos, float width, float height, float lineWidth) {
        rectOutlined(pos.x, pos.y, width, height, lineWidth, CColor.WHITE);
    }

    public static void rect(float x, float y, Vec size) {
        rect(x, y, size.x, size.y, CColor.WHITE);
    }

    public static void rectOutlined(float x, float y, Vec size, float lineWidth) {
        rectOutlined(x, y, size.x, size.y, lineWidth, CColor.WHITE);
    }

    public static void rect(Vec pos, Vec size) {
        rect(pos.x, pos.y, size.x, size.y, CColor.WHITE);
    }

    public static void rectOutlined(Vec pos, Vec size, float lineWidth) {
        rectOutlined(pos.x, pos.y, size.x, size.y, lineWidth, CColor.WHITE);
    }

    // polygon

    public static void polygon(float x, float y, float sideLength, float rotation, int amountOfSides, boolean filled,
            float lineWidth,
            CColor color) {

        sideLength /= 2;

        if (isBatching()) {
            RenderObject renderObject = new RenderObject(x, y, sideLength, 0, rotation, color, false, amountOfSides);
            Renderer.add(renderObject);
            return;
        }

        int increment = 360 / amountOfSides;
        int currentAngle = 0;

        start();
        if (!filled) {
            lineWidth(lineWidth);
        }
        Render.color(color);
        begin(filled ? GL_TRIANGLE_FAN : GL_LINE_LOOP);
        for (int i = 0; i < amountOfSides; i++) {
            Vec tmp = new Vec(0, sideLength);
            CMath.rotate(tmp, new Vec(), currentAngle);

            vertex(new Vec(tmp.clone()).add(x, y));
            currentAngle += increment;
        }
        end();
        Render.color(CColor.WHITE);
        stop();
    }

    public static void polygon(float x, float y, float sideLength, float rotation, int amountOfSides, CColor color) {
        polygon(x, y, sideLength, rotation, amountOfSides, true, 0, color);
    }

    public static void polygonOutlined(float x, float y, float sideLength, float rotation, int amountOfSides,
            float lineWidth,
            CColor color) {
        polygon(x, y, sideLength, rotation, amountOfSides, false, lineWidth, color);
    }

    public static void polygon(Vec pos, float sideLength, float rotation, int amountOfSides, CColor color) {
        polygon(pos.x, pos.y, sideLength, rotation, amountOfSides, color);
    }

    public static void polygonOutlined(Vec pos, float sideLength, float rotation, int amountOfSides, float lineWidth,
            CColor color) {
        polygonOutlined(pos.x, pos.y, sideLength, rotation, amountOfSides, lineWidth, color);
    }

    // circle

    public static void circle(float x, float y, float radius, CColor color) {
        polygon(x, y, radius, 0, 20, true, 0, color);
    }

    public static void circleOutlined(float x, float y, float radius, float lineWidth, CColor color) {
        polygon(x, y, radius, 0, 20, false, lineWidth, color);
    }

    public static void circle(Vec pos, float radius, CColor color) {
        circle(pos.x, pos.y, radius, color);
    }

    public static void circleOutlined(Vec pos, float radius, float lineWidth, CColor color) {
        circleOutlined(pos.x, pos.y, radius, lineWidth, color);
    }

    public static void line(float firstX, float firstY, float secondX, float secondY, float lineWidth, CColor color) {
        if (color != null)
            color(color);
        else
            color(CColor.WHITE);
        lineWidth(lineWidth <= 0 ? 1 : lineWidth);
        start();
        begin(GL_LINES);
        {
            vertex(firstX, firstY);
            vertex(secondX, secondY);
        }
        end();
        color(CColor.WHITE);
        stop();
    }

    public static void image(Image image, float x, float y, float width, float height, CColor color) {
        if (!image.isFinishedLoading())
            return;

        x -= width / 2;
        y -= height / 2;

        if (color == null) {
            color(CColor.WHITE);
        } else {
            color(color);
        }
        enable(GL_TEXTURE_2D);
        enable(GL_BLEND);
        disable(GL_LIGHTING);
        glBindTexture(GL_TEXTURE_2D, image.getImageId());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, (int) image.getWidth(), (int) image.getHeight(), 0, GL_RGBA,
                GL_UNSIGNED_BYTE,
                image.getPixelBuffer());

        begin(GL_QUADS);
        {
            glTexCoord2d(0, 0);
            vertex(x, y);
            glTexCoord2d(0, 1);
            vertex(x, y + height);
            glTexCoord2d(1, 1);
            vertex(x + width, y + height);
            glTexCoord2d(1, 0);
            vertex(x + width, y);
        }
        end();

        GL30.glGenerateMipmap(image.getImageId());

        enable(GL_LIGHTING);
        disable(GL_BLEND);
        disable(GL_TEXTURE_2D);
    }

    public static void imageRegion(Image image, float x, float y, float width, float height, float offsetX,
            float offsetY, CColor color) {
        if (!image.isFinishedLoading())
            return;

        x -= width / 2;
        y -= height / 2;

        if (color == null) {
            color(CColor.WHITE);
        } else {
            color(color);
        }
        enable(GL_TEXTURE_2D);
        enable(GL_BLEND);
        disable(GL_LIGHTING);
        glBindTexture(GL_TEXTURE_2D, image.getImageId());

        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
        // GL_LINEAR_MIPMAP_LINEAR);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        // glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, (int) image.getWidth(), (int)
        // image.getHeight(), 0, GL_RGBA,
        // GL_UNSIGNED_BYTE,
        // image.getPixelBuffer());

        begin(GL_QUADS);
        {
            glTexCoord2d(0, 0);
            vertex(x + offsetX, y);
            glTexCoord2d(0, 1);
            vertex(x + offsetX, y + height);
            glTexCoord2d(1, 1);
            vertex(x + offsetX + width, y + height);
            glTexCoord2d(1, 0);
            vertex(x + offsetX + width, y);
        }
        end();

        // GL30.glGenerateMipmap(image.getImageId());

        enable(GL_LIGHTING);
        disable(GL_BLEND);
        disable(GL_TEXTURE_2D);
    }

    public static void image(Image image, float x, float y) {
        image(image, x, y, image.getWidth(), image.getHeight(), null);
    }

    public static void image(Image image, Vec pos) {
        image(image, pos.x, pos.y, image.getWidth(), image.getHeight(), null);
    }

    public static void image(Image image, float x, float y, Vec size) {
        image(image, x, y, size.x, size.y, null);
    }

    public static void image(Image image, Vec pos, Vec size) {
        image(image, pos.x, pos.y, size.x, size.y, null);
    }

    public static void image(Image image, Vec pos, float width, float height) {
        image(image, pos.x, pos.y, width, height, null);
    }

    public static void image(Image image, float x, float y, CColor color) {
        image(image, x, y, image.getWidth(), image.getHeight(), color);
    }

    public static void image(Image image, Vec pos, CColor color) {
        image(image, pos.x, pos.y, image.getWidth(), image.getHeight(), color);
    }

    public static void image(Image image, float x, float y, Vec size, CColor color) {
        image(image, x, y, size.x, size.y, color);
    }

    public static void image(Image image, Vec pos, Vec size, CColor color) {
        image(image, pos.x, pos.y, size.x, size.y, color);
    }

    public static void image(Image image, Vec pos, float width, float height, CColor color) {
        image(image, pos.x, pos.y, width, height, color);
    }

}
