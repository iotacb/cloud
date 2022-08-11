package de.kostari.cloud.core.window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Platform;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;

import de.kostari.cloud.core.exceptions.SceneException;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.observers.EventSystem;
import de.kostari.cloud.core.observers.Observer;
import de.kostari.cloud.core.observers.events.Event;
import de.kostari.cloud.core.observers.events.EventType;
import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.icon.ImageParser;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.render.RenderType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Kostari
 * 
 *         Create a object of the window class to create a window
 *         and show it to the user
 */
public class Window implements Observer {

	@Getter
	private String title;

	@Getter
	private Vec size;

	@Getter
	private int width;
	@Getter
	private int height;

	@Getter
	private float deltaTime;

	@Getter
	@Setter
	private int fpsCap;

	@Getter
	private int framebufferWidth;
	@Getter
	private int framebufferHeight;
	private int sampling;

	@Getter
	private long windowId;
	private long mainMonitor;

	private long audioContext;
	private long audioDevice;

	@Getter
	@Setter
	private boolean isResizable;
	@Getter
	@Setter
	private boolean isFullscreen;
	@Getter
	@Setter
	private boolean isFocused;
	@Getter
	private boolean isVsync;

	@Getter
	private Scene scene;
	@Getter
	private Scene prevScene;

	@Getter
	@Setter
	private CColor clearColor;

	@Getter
	@Setter
	private RenderType renderType;

	private FrameTimer timer;

	@Getter
	private ByteBuffer pixels;

	public Window(int width, int height, String title) {
		createWindow(width, height, title, false, false);
	}

	public Window(int width, int height, String title, boolean fullscreen, boolean resizable) {
		createWindow(width, height, title, fullscreen, resizable);
	}

	private void createWindow(int width, int height, String title, boolean fullscreen,
			boolean resizable) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.isFullscreen = fullscreen;
		this.isResizable = resizable;
		this.sampling = 0;
		this.size = new Vec(width, height);

		this.clearColor = CColor.BLACK;
		this.renderType = RenderType.SIMPLE;

		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}

		setWindowHints();
		makeWindow();
		initGLFW();
		timer = new FrameTimer();
		pixels = BufferUtils.createByteBuffer(width * height * 4);
		Input.initInput(this, Platform.get() == Platform.WINDOWS);
		Render.window = this;
		EventSystem.addObserver(this);
	}

	public void destroy() {
		ALC10.alcDestroyContext(audioContext);
		ALC10.alcCloseDevice(audioDevice);
		glfwFreeCallbacks(windowId);
		glfwDestroyWindow(windowId);
		glfwTerminate();

		glfwSetErrorCallback(null).free();
	}

	private void drawScreen() throws Exception {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glViewport(0, 0, width, height);
		glOrtho(0, width, height, 0, -1, 1);

		deltaTime = timer.getDelta();
		if (deltaTime >= 0) {
			Render.rect(getHalfSize(), getSize(), clearColor);
			scene.draw(deltaTime);
		}

		if (getRenderType() == RenderType.PIXELS) {
			GL11.glDrawPixels(width, height, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, pixels);
		}
		glfwSwapBuffers(windowId);
		timer.updateFPS();
	}

	private void makeWindow() throws Exception {
		mainMonitor = glfwGetPrimaryMonitor();
		GLFWVidMode videoMode = glfwGetVideoMode(mainMonitor);

		if (isFullscreen) {
			this.width = videoMode.width();
			this.height = videoMode.height();
			this.framebufferWidth = videoMode.width();
			this.framebufferHeight = videoMode.height();
			this.size.set(videoMode.width(), videoMode.height());
		}

		windowId = glfwCreateWindow((int) this.width, (int) this.height, this.title,
				(this.isFullscreen ? mainMonitor : 0), 0);
		if (windowId == 0 || windowId == NULL) {
			glfwTerminate();
		}

		glfwSetWindowPos(windowId, (int) (videoMode.width() - width) / 2,
				(int) (videoMode.height() - height) / 2);

		try (MemoryStack frame = MemoryStack.stackPush()) {
			IntBuffer framebufferSize = frame.mallocInt(2);
			nglfwGetFramebufferSize(windowId, MemoryUtil.memAddress(framebufferSize),
					MemoryUtil.memAddress(framebufferSize) + 4);
			width = framebufferSize.get(0);
			height = framebufferSize.get(1);
		}

		setCallbacks();
	}

	private void setCallbacks() {
		glfwSetFramebufferSizeCallback(windowId, (id, newWidth, newHeight) -> {
			framebufferWidth = newWidth;
			framebufferHeight = newHeight;
		});

		glfwSetWindowSizeCallback(windowId, (id, newWidth, newHeight) -> {
			width = newWidth;
			height = newHeight;
			size.set(width, height);
		});

		glfwSetWindowFocusCallback(windowId, (id, focused) -> {
			isFocused = focused;
		});

		glfwSetScrollCallback(windowId, (id, scrollX, scrollY) -> {
			Input.setMouseScroll((float) scrollX, (float) scrollY);
		});

		glfwSetDropCallback(windowId, (id, count, names) -> {
			String[] files = new String[count];
			for (int i = 0; i < count; i++) {
				files[i] = GLFWDropCallback.getName(names, i);
			}
			onFileDrop(files);
		});
	}

	private void onFileDrop(String[] files) {
		if (scene == null)
			return;
		scene.onFileDrop(files);
	}

	private void initGLFW() {
		glfwMakeContextCurrent(windowId);
		glfwSwapInterval(isVsync ? 1 : 0);
		glfwShowWindow(windowId);
		initAudio();
		GL.createCapabilities();
	}

	private void initAudio() {
		String defaultDeviceName = ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);

		audioDevice = ALC11.alcOpenDevice(defaultDeviceName);

		int[] attributes = { 0 };
		audioContext = ALC11.alcCreateContext(audioDevice, attributes);
		ALC11.alcMakeContextCurrent(audioContext);

		ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

		if (!alCapabilities.OpenAL10) {
			assert false : "Audio library not supported.";
		}
	}

	private void setWindowHints() {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, (isResizable ? GLFW_TRUE : GLFW_FALSE));
		glfwWindowHint(GLFW_SAMPLES, sampling);

		if (Platform.get() == Platform.MACOSX) {
			glfwWindowHint(GLFW.GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);

			// ?: No context found on MacOSX when using this:
			// glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
			// glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
			// glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
			// glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		}
	}

	private void updateCursor() {
		DoubleBuffer bufferX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer bufferY = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(windowId, bufferX, bufferY);
		Input.setLastMousePosition(Input.getMouseX(), Input.getMouseY());
		Input.setMousePosition((float) bufferX.get(), (float) bufferY.get());
	}

	private void updateScreen() throws Exception {
		Input.setMouseScroll(0, 0);
		glfwPollEvents();
		updateCursor();
		scene.update(getDeltaTime());
		Input.updateInput();
	}

	public void show() throws Exception {
		if (scene == null) {
			throw new SceneException("No Scene set!");
		}
		while (!glfwWindowShouldClose(windowId)) {
			Sync.sync(fpsCap);
			updateScreen();
			drawScreen();
			timer.updateCounters();
		}
		destroy();
	}

	@Override
	public void onNotify(GameObject gameObject, Event event) {
		if (event.type == EventType.START_PLAY) {
			System.out.println("Engine start");
		} else if (event.type == EventType.STOP_PLAY) {
			System.out.println("Engine stop");
		}
	}

	/**
	 * Add a icon to the window
	 * 
	 * @param path the path to the image file
	 */
	public void setIcon(String path) {
		GLFWImage image = GLFWImage.malloc();
		GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
		ImageParser parsedImage = ImageParser.loadImage(path);
		image.set(parsedImage.width, parsedImage.height, parsedImage.image);
		imageBuffer.put(0, image);

		if (Platform.get() == Platform.MACOSX) {
			// The windowson mac don't have a window icon
			// the only icon that can be shown is in the doc
		} else {
			glfwSetWindowIcon(windowId, imageBuffer);
		}
	}

	/**
	 * !Does not work currently
	 * 
	 * @param sampling
	 */
	@Deprecated
	public void setSampling(int sampling) {
		this.sampling = sampling;
	}

	/**
	 * Create a custom cursor with an image file
	 * 
	 * @param path    the path to the image file
	 * @param offsetX the x position of the hotspot
	 * @param offsetY the y position of the hotspot
	 * @return the created cursor
	 */
	public long createCursor(String path, int xOffset, int yOffset) {
		ByteBuffer pixels;
		GLFWImage image = GLFWImage.create();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			pixels = STBImage.stbi_load(path, w, h, comp, 4);
			if (pixels == null)
				return glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
			image.set(w.get(0), h.get(0), pixels);
			pixels.clear();
			long cursor = glfwCreateCursor(image, xOffset, yOffset);
			image.clear();
			return cursor;
		}
	}

	/**
	 * Set the title of the window
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(windowId, title);
	}

	/**
	 * Enable or disable vsync
	 * 
	 * @param vsync
	 */
	public void setVsync(boolean vsync) {
		this.isVsync = vsync;
		glfwSwapInterval(vsync ? 1 : 0);
	}

	/**
	 * Set the opacity of the window
	 * !Only works on Windows
	 * 
	 * @param opacity
	 */
	@Deprecated
	public void setWindowOpacity(float opacity) {
		glfwSetWindowOpacity(windowId, opacity);
	}

	/**
	 * Change the current displayed scene
	 * 
	 * @param scene the scene which should be displayed
	 */
	public void setScene(final Class<? extends Scene> scene) {
		try {
			this.scene = scene.getConstructor(getClass()).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the cursor using the system standard cursors
	 */
	public void setCursor(int cursor) {
		long createdCursor = glfwCreateStandardCursor(cursor);
		glfwSetCursor(windowId, createdCursor);
	}

	/**
	 * Set the cursor using a custom cursor
	 */
	public void setCursor(long cursor) {
		glfwSetCursor(windowId, cursor);
	}

	public int getFPS() {
		return timer.getFPS();
	}

	public Vec getHalfSize() {
		return size.clone().mul(0.5F);
	}

	public boolean isWindowClosing() {
		return glfwWindowShouldClose(windowId);
	}

	/**
	 * Returns the pixel buffer that is currently being drawn to the screen
	 * 
	 * @return
	 */
	public ByteBuffer loadPixels() {
		GL11.glReadPixels(0, 0, getWidth(), getHeight(), GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE,
				pixels);
		return pixels;
	}

	/**
	 * Change a pixel in a pixel buffer to a certain color
	 * 
	 * @param pixels The pixel buffer where a pixels color should be changed
	 * @param x      The x position of the pixel
	 * @param y      The y position of the pixel
	 * @param color  The color of the pixel
	 * @return
	 */
	public ByteBuffer changePixel(ByteBuffer pixels, int x, int y, CColor color) {
		y = (getHeight() - y) - 1;
		int stride = (int) width * 4;
		int i = y * stride + x * 4;

		pixels.put(i + 0, (byte) Math.round((color.getRed() & 0xFF)));
		pixels.put(i + 1, (byte) Math.round((color.getGreen() & 0xFF)));
		pixels.put(i + 2, (byte) Math.round((color.getBlue() & 0xFF)));
		return pixels;
	}

	/**
	 * Update the pixels that are getting drawn to the screen
	 * 
	 * @param pixels
	 */
	public void updatePixels(ByteBuffer pixels) {
		this.pixels = pixels;
	}

	/**
	 * Creates a clean pixel buffer
	 * 
	 * Useful when manipulating the pixels directly
	 * 
	 * @return
	 */
	public ByteBuffer newPixels() {
		return BufferUtils.createByteBuffer(width * height * 4);
	}

}
