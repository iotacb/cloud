package de.kostari.cloud.core.window;

import static org.lwjgl.glfw.GLFW.*;
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
import org.lwjgl.system.Platform;

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

	private String title;

	private Vec size;
	private float width;
	private float height;

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

	private Vec mousePosition;
	private Vec lastMousePosition;

	private GLFWWindowSizeCallback windowSizeCallback;
	private GLFWWindowFocusCallback windowFocusCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;

	private Scene scene;
	private Scene prevScene;

	private FrameTimer timer;

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
		this.isFullscreen = fullscreen;
		this.isResizable = resizable;
		this.sampling = 0;
		this.size = new Vec(width, height);
		this.mousePosition = new Vec();
		this.lastMousePosition = new Vec();

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
		Input.initInput(this, Platform.get() == Platform.WINDOWS);
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
				isFocused = focused;
			}
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
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		}
	}

	private void updateCursor() {
		DoubleBuffer bufferX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer bufferY = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(windowId, bufferX, bufferY);
		lastMousePosition.set(mousePosition);
		mousePosition.set((float) bufferX.get(), (float) bufferY.get());
	}

	private void updateScreen() throws Exception {
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
			System.out.println("STARTING PLAY!");
		} else if (event.type == EventType.STOP_PLAY) {
			System.out.println("STOPPING PLAY!");
		}
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
		glfwWindowHint(GLFW_SAMPLES, sampling);
	}

	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(windowId, title);
	}

	public void useVsync(boolean vsync) {
		this.useVsync = vsync;
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

	public Vec getLastMousePosition() {
		return lastMousePosition;
	}

	public int getLastMouseX() {
		return (int) lastMousePosition.getX();
	}

	public int getLastMouseY() {
		return (int) lastMousePosition.getY();
	}

	public Vec getMousePosition() {
		return mousePosition;
	}

	public int getMouseX() {
		return (int) mousePosition.getX();
	}

	public int getMouseY() {
		return (int) mousePosition.getY();
	}

	public Vec getSize() {
		return size;
	}

	public Vec getHalfSize() {
		return size.clone().mul(0.5F);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
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

}
