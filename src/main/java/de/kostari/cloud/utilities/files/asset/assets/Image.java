package de.kostari.cloud.utilities.files.asset.assets;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import static org.lwjgl.stb.STBImageResize.*;
import org.lwjgl.system.MemoryStack;

import de.kostari.cloud.utilities.files.asset.Asset;

public class Image extends Asset {

    private float width;
    private float height;

    private int imageId;

    private ByteBuffer pixelBuffer;

    public boolean finishedLoading = false;

    public Image(String path) {
        super(path);

        this.imageId = GL11.glGenTextures();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);
            pixelBuffer = STBImage.stbi_load(path, w, h, comp, 4);
            if (pixelBuffer == null)
                return;
            this.width = w.get(0);
            this.height = h.get(0);
            int stride = (int) width * 4;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
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

            this.finishedLoading = true;
        }
    }

    public Image(ByteBuffer buffer, float width, float height) {
        super("");

        this.imageId = GL11.glGenTextures();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.width = width;
            this.height = height;

            this.pixelBuffer = buffer;
            int stride = (int) width * 4;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
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

            this.finishedLoading = true;
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public ByteBuffer getPixelBuffer() {
        return pixelBuffer;
    }

    public boolean isFinishedLoading() {
        return finishedLoading;
    }

    public int getImageId() {
        return imageId;
    }

    public void resize(float width, float height, int filter) {
        int inputWidth = (int) this.width;
        int inputHeight = (int) this.height;

        ByteBuffer buffer = ByteBuffer.allocateDirect((int) (width * height * 4));
        stbir_resize_uint8_generic(getPixelBuffer(), inputWidth, inputHeight, inputWidth * 4, buffer,
                (int) width,
                (int) height, (int) width * 4, 4, 3, STBIR_FLAG_ALPHA_PREMULTIPLIED,
                STBIR_EDGE_CLAMP,
                filter,
                STBIR_COLORSPACE_SRGB);

        System.out.println(buffer);

        pixelBuffer = buffer.duplicate();
        this.width = width;
        this.height = height;
    }

    public void resize(float width, float height) {
        resize(width, height, STBIR_FILTER_DEFAULT);
    }
}
