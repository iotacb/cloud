package de.kostari.cloud.core.window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
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
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Platform;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.observers.EventSystem;
import de.kostari.cloud.core.observers.Observer;
import de.kostari.cloud.core.observers.events.Event;
import de.kostari.cloud.core.observers.events.EventType;
import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.icon.ImageParser;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.files.asset.AssetManager;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

/**
 * @author Kostari
 * 
 *         Create a object of the window class to create a window
 *         and show it to the user
 */
public class Window implements Observer {

	private String title;

	private Vec size;
	private int width;
	private int height;

	private float deltaTime;

	private int fpsCap;
	private int framebufferWidth;
	private int framebufferHeight;
	private int sampling;

	private long windowId;
	private long mainMonitor;

	private long audioContext;
	private long audioDevice;

	private boolean isResizable;
	private boolean isFullscreen;
	private boolean isFocused;
	private boolean useVsync;

	private Scene scene;
	private Scene prevScene;

	private FrameTimer timer;

	private AssetManager assetManager;

	private CColor clearColor;

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
		this.sampling = 4;
		this.size = new Vec(width, height);

		this.clearColor = CColor.BLACK;

		this.scene = new Scene(this) {
			@Override
			public void draw(float delta) {
				super.draw(delta);
			}

			@Override
			public void update(float delta) {
				super.update(delta);
			}
		};

		this.assetManager = new AssetManager();

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
	}

	private void initGLFW() {
		glfwMakeContextCurrent(windowId);
		glfwSwapInterval(useVsync ? 1 : 0);
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
	 * Set a maximum FPS cap.
	 * 
	 * @param fpsCap
	 */
	public void setFPSCap(int fpsCap) {
		this.fpsCap = fpsCap;
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

			// TODO: find a way to add a icon without using javas awt
		} else {
			glfwSetWindowIcon(windowId, imageBuffer);
		}
	}

	/**
	 * !Does not work currently
	 * 
	 * @param sampling
	 */
	public void setSampling(int sampling) {
		this.sampling = sampling;
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
	public void useVsync(boolean vsync) {
		this.useVsync = vsync;
		glfwSwapInterval(vsync ? 1 : 0);
	}

	/**
	 * Set the opacity of the window
	 * !Only works on Windows
	 * 
	 * @param opacity
	 */
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
			image.set(w.get(0), h.get(0), pixels);
			pixels.clear();
			long cursor = glfwCreateCursor(image, xOffset, yOffset);
			image.clear();
			return cursor;
		}

	}

	/**
	 * Change the current displayed scene
	 * 
	 * @param scene the scene which should be displayed
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public long getWindowId() {
		return windowId;
	}

	public String getTitle() {
		return title;
	}

	public float getDeltaTime() {
		return deltaTime;
	}

	public int getFPS() {
		return timer.getFPS();
	}

	public int getFpsCap() {
		return fpsCap;
	}

	public Vec getSize() {
		return size;
	}

	public Vec getHalfSize() {
		return size.clone().mul(0.5F);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getFramebufferWidth() {
		return framebufferWidth;
	}

	public int getFramebufferHeight() {
		return framebufferHeight;
	}

	public Scene getScene() {
		return scene;
	}

	public Scene getPrevScene() {
		return prevScene;
	}

	public boolean isUsingVsync() {
		return useVsync;
	}

	public boolean isWindowClosing() {
		return glfwWindowShouldClose(windowId);
	}

	public boolean isFocused() {
		return isFocused;
	}

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public boolean isResizable() {
		return isResizable;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setClearColor(CColor clearColor) {
		this.clearColor = clearColor;
	}

}
