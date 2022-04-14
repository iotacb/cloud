package de.kostari.cloud.utilities.render.text;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.render.Texture;
import de.kostari.cloud.utilities.render.tmp.Renderer;
import de.kostari.cloud.utilities.render.tmp.renderobjects.UVRenderObject;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.RenderingHints;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static java.awt.Font.*;

public class CFont {

    private Map<Character, CGlyph> glyphs;

    private Texture texture;

    private int fontHeight;

    public CFont() {
        this.glyphs = new HashMap<>();
        this.texture = createFontTexture(new Font(MONOSPACED, BOLD, 16), true);
    }

    public CFont(Font font, boolean antiAliasing) {
        this.glyphs = new HashMap<>();
        this.texture = createFontTexture(font, antiAliasing);
    }

    private Texture createFontTexture(Font font, boolean antiAlias) {
        int imageWidth = 0;
        int imageHeight = 0;

        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                continue;
            }
            char c = (char) i;
            BufferedImage ch = createCharImage(font, c, antiAlias);
            if (ch == null) {
                continue;
            }

            imageWidth += ch.getWidth();
            imageHeight = Math.max(imageHeight, ch.getHeight());
        }

        fontHeight = imageHeight;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        int x = 0;

        /*
         * Create image for the standard chars, again we omit ASCII 0 to 31
         * because they are just control codes
         */
        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                continue;
            }
            char c = (char) i;
            BufferedImage charImage = createCharImage(font, c, antiAlias);
            if (charImage == null) {
                continue;
            }

            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();

            CGlyph ch = new CGlyph(charWidth, charHeight, x, image.getHeight() - charHeight);
            g.drawImage(charImage, x, 0, null);
            x += ch.width;
            glyphs.put(c, ch);
        }

        AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        AffineTransformOp operation = new AffineTransformOp(transform,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = operation.filter(image, null);

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = pixels[i * width + j];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        Texture fontTexture = Texture.createTexture(width, height, buffer);
        MemoryUtil.memFree(buffer);
        return fontTexture;
    }

    private BufferedImage createCharImage(Font font, char c, boolean antiAlias) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        if (charWidth == 0) {
            return null;
        }

        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        g.setPaint(java.awt.Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();
        return image;
    }

    /**
     * Gets the width of the specified text.
     *
     * @param text The text
     *
     * @return Width of text
     */
    public int getWidth(String text) {
        int width = 0;
        int lineWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }
            if (c == '\r') {
                continue;
            }
            CGlyph g = glyphs.get(c);
            lineWidth += g.width;
        }
        width = Math.max(width, lineWidth);
        return width;
    }

    /**
     * Gets the height of the specified text.
     *
     * @param text The text
     *
     * @return Height of text
     */
    public int getHeight(String text) {
        int height = 0;
        int lineHeight = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                height += lineHeight;
                lineHeight = 0;
                continue;
            }
            if (c == '\r') {
                continue;
            }
            CGlyph g = glyphs.get(c);
            lineHeight = Math.max(lineHeight, g.height);
        }
        height += lineHeight;
        return height;
    }

    /**
     * Draw text at the specified position and color.
     *
     * @param renderer The renderer to use
     * @param text     Text to draw
     * @param x        X coordinate of the text position
     * @param y        Y coordinate of the text position
     * @param c        Color to use
     */
    public void drawText(String text, float x, float y, CColor c) {
        int textHeight = getHeight(text);

        float drawX = x;
        float drawY = y;
        if (textHeight > fontHeight) {
            drawY += textHeight - fontHeight;
        }

        texture.bind();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                drawY += fontHeight;
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                continue;
            }
            CGlyph g = glyphs.get(ch);
            drawTextureRegion(texture, drawX, drawY - (text.contains("\n") ? g.height : 0), g.x, g.y, g.width, g.height,
                    c);
            drawX += g.width;
        }
    }

    public void drawText(String text, float x, float y) {
        drawText(text, x, y, CColor.WHITE);
    }

    public void drawCenteredText(String text, float x, float y, CColor c) {
        float width = getWidth(text) / 2;
        float height = getHeight(text) / 2;
        drawText(text, x - width, y - height, c);
    }

    public void drawCenteredText(String text, float x, float y) {
        drawCenteredText(text, x, y, CColor.WHITE);
    }

    public void drawText(String text, Vec pos, CColor c) {
        drawText(text, pos.x, pos.y, c);
    }

    public void drawCenteredText(String text, Vec pos, CColor c) {
        drawCenteredText(text, pos.x, pos.y, c);
    }

    public void drawText(String text, Vec pos) {
        drawText(text, pos.x, pos.y);
    }

    public void drawCenteredText(String text, Vec pos) {
        drawCenteredText(text, pos.x, pos.y);
    }

    private void drawTextureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth,
            float regHeight, CColor c) {

        float x2 = x + regWidth;
        float y2 = y + regHeight;

        float s1 = regX / texture.getWidth();
        float t1 = regY / texture.getHeight();
        float s2 = (regX + regWidth) / texture.getWidth();
        float t2 = (regY + regHeight) / texture.getHeight();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        Render.color(c);
        Render.begin(GL11.GL_QUADS);
        {
            vertexUV(x, y2, s1, t1);
            vertexUV(x2, y2, s2, t1);
            vertexUV(x2, y, s2, t2);
            vertexUV(x, y, s1, t2);
        }
        Render.end();
        Render.color(CColor.WHITE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

    }

    private void vertexUV(float x, float y, float u, float v) {
        GL11.glTexCoord2f(u, v);
        Render.vertex(x, y);
    }

}
