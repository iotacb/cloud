package de.kostari.cloud.core.scene;

import java.util.ArrayList;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.observers.EventSystem;
import de.kostari.cloud.core.observers.events.Event;
import de.kostari.cloud.core.observers.events.EventType;
import de.kostari.cloud.core.physics.PhysicsEngine;
import de.kostari.cloud.core.physics.Rigidbody;
import de.kostari.cloud.core.ui.UIComponent;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.render.RenderType;
import de.kostari.cloud.utilities.render.batched.Renderer;

public abstract class Scene {

	private Window window;

	private int idCounter;

	private ArrayList<GameObject> objects;
	private ArrayList<UIComponent> uiComponents;

	private ArrayList<GameObject> objectsToRemove;
	private ArrayList<UIComponent> uiComponentsToRemove;

	private boolean isPlaying = false;

	private PhysicsEngine physicsEngine;

	public Scene(Window window) {
		this.window = window;

		this.objects = new ArrayList<>();
		this.uiComponents = new ArrayList<>();

		this.objectsToRemove = new ArrayList<>();
		this.uiComponentsToRemove = new ArrayList<>();

		this.physicsEngine = new PhysicsEngine();

		objects.sort((o1, o2) -> {
			return o1.getZIndex() - o2.getZIndex();
		});

		uiComponents.sort((o1, o2) -> {
			return o1.getzIndex() - o2.getzIndex();
		});

		play();
	}

	public void update(float delta) {
		updateGameObjects(delta);
		updateUIComponents(delta);
	}

	public void updateGameObjects(float delta) {
		for (int i = 0; i < objectsToRemove.size(); i++) {
			GameObject object = objectsToRemove.get(i);
			objects.remove(object);
		}
		physicsEngine.update(delta);
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			object.update(delta);
		}
	}

	public void updateUIComponents(float delta) {
		for (int i = 0; i < uiComponentsToRemove.size(); i++) {
			UIComponent ui = uiComponentsToRemove.get(i);
			uiComponents.remove(ui);
		}
		for (int i = 0; i < uiComponents.size(); i++) {
			UIComponent ui = uiComponents.get(i);
			if (ui.isDisabled())
				continue;
			ui.update(delta);
		}
	}

	public void draw(float delta) {
		drawGameObjects(delta);
		drawUIComponents(delta);
	}

	public void drawGameObjects(float delta) {
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			object.draw(delta);
		}
		if (window.getRenderType() == RenderType.BATCHED) {
			Renderer.render();
			Renderer.clearBatches();
		}
	}

	public void drawUIComponents(float delta) {
		for (int i = 0; i < uiComponents.size(); i++) {
			UIComponent ui = uiComponents.get(i);
			if (ui.isDisabled())
				continue;
			ui.draw(delta);
		}
	}

	public void onFileDrop(String[] files) {

	}

	public void pause() {
		isPlaying = false;
		EventSystem.notify(null, new Event(EventType.STOP_PLAY));
	}

	public void play() {
		isPlaying = true;
		EventSystem.notify(null, new Event(EventType.START_PLAY));
	}

	/**
	 * Add a object to the object list.
	 * If a object has a rigidbody component attached it will be added to the
	 * physics engine
	 * 
	 * @param object the object which should be added
	 * @param zIndex used to sort the objects to control the drawing order
	 */
	public void addObject(GameObject object, int zIndex) {
		object.setZIndex(zIndex);
		object.setId(idCounter);
		if (object.getComponent(Rigidbody.class) != null) {
			physicsEngine.add(object);
		}

		objects.add(object);
		idCounter++;
		objects.sort((o1, o2) -> {
			return o1.getZIndex() - o2.getZIndex();
		});
	}

	/**
	 * Add a object to the object list.
	 * If a object has a rigidbody component attached it will be added to the
	 * physics engine
	 * 
	 * @param object the object which should be added
	 */
	public void addObject(GameObject object) {
		addObject(object, 0);
	}

	/**
	 * Add objects to the object list.
	 * If a object has a rigidbody component attached it will be added to the
	 * physics engine
	 * 
	 * @param objects the objects which should be added
	 */
	public void addObjects(GameObject... objects) {
		for (GameObject obj : objects) {
			addObject(obj);
		}
	}

	/**
	 * Remove a object from the object list.
	 * If a object has a rigidbody component attached it will be removed from the
	 * physics engine
	 * 
	 * @param gameObject the object which should be removed
	 */
	public void removeObject(GameObject gameObject) {
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			if (object.equals(gameObject)) {
				objectsToRemove.add(object);
				physicsEngine.removeRigidbody(object);
			}
		}
	}

	/**
	 * Add a UI component to the UI component list.
	 * 
	 * @param uiComponent the ui component which should be added
	 * @param zIndex      used to sort the ui components to control the drawing
	 *                    order
	 */
	public void addUI(UIComponent uiComponent, int zIndex) {
		uiComponent.setZIndex(zIndex);
		uiComponents.add(uiComponent);
		uiComponents.sort((o1, o2) -> {
			return o1.getzIndex() - o2.getzIndex();
		});
	}

	/**
	 * Add a UI component to the UI component list.
	 * 
	 * @param uiComponent the ui component which should be added
	 */
	public void addUI(UIComponent uiComponent) {
		addUI(uiComponent, 0);
	}

	/**
	 * Add a UI components to the UI component list.
	 * 
	 * @param uiComponents the ui components which should be added
	 */
	public void addUIs(UIComponent... uiComponents) {
		for (UIComponent obj : uiComponents) {
			addUI(obj);
		}
	}

	/**
	 * Remove a UI component from the UI component list.
	 * 
	 * @param uiComponent the ui component which should be removed
	 */
	public void removeUI(UIComponent uiComponent) {
		for (int i = 0; i < uiComponents.size(); i++) {
			UIComponent object = uiComponents.get(i);
			if (object.equals(uiComponent)) {
				objects.remove(i);
			}
		}
	}

	public ArrayList<GameObject> getObjects() {
		return objects;
	}

	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public Window getWindow() {
		return window;
	}

}
