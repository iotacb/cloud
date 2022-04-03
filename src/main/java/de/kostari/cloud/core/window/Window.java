package de.kostari.cloud.core.window;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowOpacity;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.observers.EventSystem;
import de.kostari.cloud.core.observers.Observer;
import de.kostari.cloud.core.observers.events.Event;
import de.kostari.cloud.core.observers.events.EventType;
import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.icon.ImageParser;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class Window implements Observer {

	float deltaTime;

	int fpsCap, framebufferWidth, framebufferHeight, sampling;
	GLFWFramebufferSizeCallback framebufferSizeCallback;

	Input inputHandler;

	int mouseX, mouseY, lastMouseX, lastMouseY;

	GLFWWindowFocusCallback windowFocusCallback;

	long windowId, mainMonitor;

	boolean windowResizable, windowFullscreen, windowFocused, vsync;

	Vec size, mouseLocation, lastMouseLocation;

	GLFWWindowSizeCallback windowSizeCallback;

	String title;
	float width, height;
	Scene scene, prevScene;

	FrameTimer timer;

	private long audioContext;
	private long audioDevice;

	public Window(int width, int height, String title) {
		createWindow(width, height, title, false, false);
	}

	public Window(int width, int height, String title, boolean fullscreen, boolean resizable) {
		createWindow(width, height, title, fullscreen, resizable);
	}

	private void createWindow(float width, float height, String title, boolean fullscreen,
			boolean resizable) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.windowFullscreen = fullscreen;
		this.windowResizable = resizable;
		this.sampling = 0;
		this.size = new Vec(width, height);
		this.mouseLocation = new Vec();
		this.lastMouseLocation = new Vec();

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

		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() throws Exception {
		if (!glfwInit())
			return;

		setWindowHints();
		makeWindow();
		initGLFW();
		timer = new FrameTimer();
		inputHandler = new Input(this.windowId, this, System.getProperty("os.name").contains("Windows"));
		Render.window = this;
		EventSystem.addObserver(this);
	}

	public void destroy() {
		framebufferSizeCallback.free();
		windowSizeCallback.free();
		windowFocusCallback.free();
		ALC10.alcDestroyContext(audioContext);
		ALC10.alcCloseDevice(audioDevice);
		glfwTerminate();
		glfwDestroyWindow(windowId);
	}

	private void drawScreen() throws Exception {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glViewport(0, 0, (int) width, (int) height);
		glOrtho(0, width, height, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);

		glClear(GL_COLOR_BUFFER_BIT);
		scene.draw(getDeltaTime());
		glfwSwapBuffers(this.windowId);

		deltaTime = timer.getDelta();
		timer.updateFPS();
	}

	private void makeWindow() throws Exception {
		mainMonitor = glfwGetPrimaryMonitor();
		GLFWVidMode videoMode = glfwGetVideoMode(mainMonitor);

		if (windowFullscreen) {
			this.width = videoMode.width();
			this.height = videoMode.height();
			this.framebufferWidth = videoMode.width();
			this.framebufferHeight = videoMode.height();
			this.size.set(videoMode.width(), videoMode.height());
		}

		windowId = glfwCreateWindow((int) this.width, (int) this.height, this.title,
				(this.windowFullscreen ? mainMonitor : 0), 0);
		if (windowId == 0 || windowId == NULL) {
			glfwTerminate();
		}

		glfwSetWindowPos(windowId, (int) (videoMode.width() - width) / 2,
				(int) (videoMode.height() - height) / 2);

		setCallbacks();
	}

	private void setCallbacks() {
		glfwSetFramebufferSizeCallback(windowId, framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long windowId, int width, int height) {
				if (width > 0 && height > 0) {
					framebufferWidth = width;
					framebufferHeight = height;
				}
			}
		});

		glfwSetWindowSizeCallback(windowId, windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long windowId, int w, int h) {
				if (w > 0 && h > 0) {
					width = w;
					height = h;
					size.set(w, h);
				}
			}
		});

		glfwSetWindowFocusCallback(windowId, windowFocusCallback = new GLFWWindowFocusCallback() {

			@Override
			public void invoke(long windowId, boolean focused) {
				windowFocused = focused;
			}
		});
	}

	private void initGLFW() {
		glfwMakeContextCurrent(windowId);
		glfwSwapInterval(vsync ? 1 : 0);
		glfwShowWindow(windowId);
		initAudio();
		GL.createCapabilities();
	}

	private void initAudio() {
		// Initialize the audio device
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
		glfwWindowHint(GLFW.GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, (this.windowResizable ? GLFW_TRUE : GLFW_FALSE));
		if (sampling > 0) {
			glfwWindowHint(GLFW_SAMPLES, sampling);
		}
	}

	private void updateCursor() {
		DoubleBuffer bufferX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer bufferY = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(windowId, bufferX, bufferY);
		lastMouseLocation.set(mouseLocation);
		mouseLocation.set((float) bufferX.get(), (float) bufferY.get());
	}

	private void updateScreen() throws Exception {
		glfwPollEvents();
		updateCursor();
		scene.update(getDeltaTime());
		inputHandler.update();
	}

	public void setFPSCap(int fpsCap) {
		this.fpsCap = fpsCap;
	}

	public void setIcon(String path) {
		GLFWImage image = GLFWImage.malloc();
		GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
		ImageParser parsedImage = ImageParser.loadImage(path);
		image.set(parsedImage.width, parsedImage.height, parsedImage.image);
		imageBuffer.put(0, image);
		glfwSetWindowIcon(windowId, imageBuffer);
	}

	public void setSampling(int sampling) {
		this.sampling = sampling;
	}

	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(windowId, title);
	}

	public void setVSync(boolean vsync) {
		this.vsync = vsync;
		glfwSwapInterval(vsync ? 1 : 0);
	}

	public void setWindowOpacity(float opacity) {
		glfwSetWindowOpacity(windowId, opacity);
	}

	public void setScene(final Class<? extends Scene> scene) {
		try {
			this.scene = scene.getConstructor(getClass()).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void show() throws Exception {
		while (!glfwWindowShouldClose(windowId)) {
			Sync.sync(fpsCap);
			// if (deltaTime >= 0) {
			updateScreen();
			drawScreen();
			timer.updateCounters();
			// }
		}
		destroy();
	}

	@Override
	public void onNotify(GameObject gameObject, Event event) {
		if (event.type == EventType.START_PLAY) {
			System.out.println("STARTING PLAY!");
		} else if (event.type == EventType.STOP_PLAY) {
			System.out.println("STOPPING PLAY!");
		}
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

	public Input getInput() {
		return inputHandler;
	}

	public Vec getLastMouseLocation() {
		return lastMouseLocation;
	}

	public int getLastMouseX() {
		return lastMouseX;
	}

	public int getLastMouseY() {
		return lastMouseY;
	}

	public Vec getMouseLocation() {
		return mouseLocation;
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public Scene getPrevScene() {
		return prevScene;
	}

	public float getHeight() {
		return height;
	}

	public Vec getSize() {
		return size;
	}

	public float getWidth() {
		return width;
	}

	public Scene getScene() {
		return scene;
	}

	public boolean isVsync() {
		return vsync;
	}

	public boolean isWindowClosing() {
		return glfwWindowShouldClose(windowId);
	}

	public boolean isWindowFocused() {
		return windowFocused;
	}

	public boolean isWindowFullscreen() {
		return windowFullscreen;
	}

	public boolean isWindowResizable() {
		return windowResizable;
	}

}
