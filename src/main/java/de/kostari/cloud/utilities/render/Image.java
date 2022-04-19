package de.kostari.cloud.utilities.render;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.time.Timer;

public class Image {

	public File imageFile;

	int imageWidth, imageHeight, imageId, imageIndex;

	ByteBuffer pixelBuffer;

	Timer animationTimer;

	public boolean finishedLoading = false;

	public Image(String imagePath) {
		this.imageFile = new File(imagePath);
		if (!imageFile.exists()) {

		}
		try {
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Image(File file) {
		this.imageFile = file;
		if (!imageFile.exists()) {

		}
		try {
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws IOException {
		this.animationTimer = new Timer();

		this.imageId = glGenTextures();

		// BufferedImage image = ImageIO.read(this.imageFile);
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			pixelBuffer = STBImage.stbi_load(imageFile.getPath(), w, h, comp, 4);

			// ByteBuffer tmp = BufferUtils.createByteBuffer(pixelBuffer.capacity());
			this.imageWidth = w.get(0);
			this.imageHeight = h.get(0);
			int stride = imageWidth * 4;
			// for (int y = 0; y < imageHeight; y++) {
			// for (int x = 0; x < imageWidth; x++) {
			// int i = y * stride + x * 4;
			// int r = pixelBuffer.get(i);
			// int g = pixelBuffer.get(i + 1);
			// int b = pixelBuffer.get(i + 2);
			// int a = pixelBuffer.get(i + 3);

			// tmp.put((byte) r);
			// tmp.put((byte) g);
			// tmp.put((byte) b);
			// tmp.put((byte) a);
			// }
			// }

			// pixelBuffer = tmp;

			for (int y = 0; y < imageHeight; y++) {
				for (int x = 0; x < imageWidth; x++) {
					int i = y * stride + x * 4;

					float alpha = (pixelBuffer.get(i + 3) & 0xFF) / 255.0f;
					pixelBuffer.put(i + 0, (byte) Math.round(((pixelBuffer.get(i + 0) & 0xFF) *
							alpha)));
					pixelBuffer.put(i + 1, (byte) Math.round(((pixelBuffer.get(i + 1) & 0xFF) *
							alpha)));
					pixelBuffer.put(i + 2, (byte) Math.round(((pixelBuffer.get(i + 2) & 0xFF) *
							alpha)));
				}
			}

			this.pixelBuffer.flip();

			this.finishedLoading = true;
		}
	}

	public void drawImage(float x, float y, float width, float height) {
		if (this.pixelBuffer == null) {
			try {
				throw new Exception("Pixel buffer of image is empty. Error: #004");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		width /= 2;
		height /= 2;

		Render.color(CColor.WHITE);
		Render.enable(GL_TEXTURE_2D);
		Render.enable(GL_BLEND);
		Render.disable(GL_LIGHTING);
		glBindTexture(GL_TEXTURE_2D, this.imageId);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, this.imageWidth, this.imageHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE,
				this.pixelBuffer);

		float diffW = (float) Math.abs(this.imageWidth - width);
		float diffH = (float) Math.abs(this.imageHeight - height);
		x += (this.imageWidth - diffW);
		y += (this.imageHeight - diffH);

		Render.begin(GL_QUADS);
		{
			glTexCoord2d(0, 1);
			Render.vertex(x - width, y + height);
			glTexCoord2d(1, 1);
			Render.vertex(x + width, y + height);
			glTexCoord2d(1, 0);
			Render.vertex(x + width, y - height);
			glTexCoord2d(0, 0);
			Render.vertex(x - width, y - height);
		}
		Render.end();

		GL30.glGenerateMipmap(imageId);

		Render.enable(GL_LIGHTING);
		Render.disable(GL_BLEND);
		Render.disable(GL_TEXTURE_2D);
	}

	public void drawImage(Vec location, Vec size) {
		drawImage(location.x, location.y, size.x, size.y);
	}

	public void drawImage(Vec location, float width, float height) {
		drawImage(location.x, location.y, width, height);
	}

	public void drawImage(float x, float y, Vec size) {
		drawImage(x, y, size.x, size.y);
	}

	public void drawImage(float x, float y) {
		drawImage(x, y, this.imageWidth, this.imageHeight);
	}

	public void drawImage(Vec location) {
		drawImage(location.x, location.y, this.imageWidth, this.imageHeight);
	}

	public void drawImageCentered(float x, float y, float width, float height) {
		drawImage(x - width / 2, y - height / 2, width, height);
	}

	public void drawImageCentered(Vec location, Vec size) {
		drawImage(location.x - size.x / 2, location.y - size.y / 2, size.x, size.y);
	}

	public void drawImageCentered(Vec location, float width, float height) {
		drawImage(location.x - width / 2, location.y - height / 2, width, height);
	}

	public void drawImageCentered(float x, float y, Vec size) {
		drawImage(x - size.x / 2, y - size.y / 2, size.x, size.y);
	}

	public void drawImageCentered(float x, float y) {
		drawImage(x - imageWidth / 2, y - imageHeight / 2, this.imageWidth, this.imageHeight);
	}

	public void drawImageCentered(Vec location) {
		drawImage(location.x - imageWidth / 2, location.y - imageHeight / 2, this.imageWidth, this.imageHeight);
	}

	public void reloadImage() {
		try {
			this.finishedLoading = false;
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}
}
