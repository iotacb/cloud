package de.kostari.cloud.utilities.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_POINT_BIT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_STIPPLE_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBEasyFont;

import de.kostari.cloud.utilities.color.CColor;

public class Text {

	private static int DEFAULT_SIZE = 16;

	public static void drawText(String text, float x, float y, CColor color, int size) {
		if (color == null) {
			color = CColor.WHITE;
		}

		ByteBuffer buffer = BufferUtils.createByteBuffer(text.length() * 300);

		int quads = stb_easy_font_print(0, 0, text, null, buffer);

		float sizeBuffer = size * .14F;

		Render.start();
		{
			Render.translate(x, y);
			Render.color(color);
			Render.scale(sizeBuffer, sizeBuffer);
			glEnableClientState(GL_VERTEX_ARRAY);
			glVertexPointer(GL_POINT_BIT, GL_FLOAT, GL_POLYGON_STIPPLE_BIT, buffer);
			glDrawArrays(GL_QUADS, 0, quads * 4);
		}
		Render.stop();
	}

	public static void drawText(String text, float x, float y, CColor color) {
		drawText(text, x, y, color, DEFAULT_SIZE);
	}

	public static void drawText(String text, float x, float y) {
		drawText(text, x, y, null, DEFAULT_SIZE);
	}

	public static void drawCenteredText(String text, float x, float y, CColor color) {
		drawText(text, x - getHalfWidth(text), y, color, DEFAULT_SIZE);
	}

	public static void drawCenteredText(String text, float x, float y) {
		drawText(text, x - getHalfWidth(text), y, null, DEFAULT_SIZE);
	}

	public static int getWidth(String text, int size) {
		return (int) (STBEasyFont.stb_easy_font_width(text) * (size * .1325));
	}

	public static int getWidth(String text) {
		return (int) (STBEasyFont.stb_easy_font_width(text) * (DEFAULT_SIZE * .1325));
	}

	public static int getHalfWidth(String text) {
		return getWidth(text) / 2;
	}

	public static int getHeight() {
		return DEFAULT_SIZE;
	}

}
