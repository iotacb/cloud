package de.kostari.cloud.core.scene;

import java.util.ArrayList;

import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.observers.EventSystem;
import de.kostari.cloud.core.observers.events.Event;
import de.kostari.cloud.core.observers.events.EventType;
import de.kostari.cloud.core.physics.PhysicsEngine;
import de.kostari.cloud.core.physics.Rigidbody;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.math.Vec;

public abstract class Scene {

	public Window window;

	private ArrayList<GameObject> objects;
	private Camera camera;

	private boolean isPlaying = false;

	private PhysicsEngine physicsEngine;

	public Scene(Window window) {
		this.window = window;

		this.objects = new ArrayList<>();
		this.camera = new Camera(window);

		this.physicsEngine = new PhysicsEngine();

		objects.sort((o1, o2) -> {
			return o1.getzIndex() - o2.getzIndex();
		});

		play();
	}

	public void update(float delta) {
		if (!isPlaying)
			return;

		physicsEngine.update(delta);

		objects.forEach(object -> {
			object.update(delta);
		});
	}

	public void draw(float delta) {
		objects.forEach(object -> {
			if (object.isIgnoringCameraMovement()) {
				object.draw(delta);
			} else {
				Transform oldTransform = new Transform(object.transform.position);
				oldTransform.rotation = object.transform.rotation;
				oldTransform.scale = new Vec(object.transform.scale);
				object.transform.position = new Vec(object.transform.position.x - camera.transform.position.x,
						object.transform.position.y - camera.transform.position.y);

				object.draw(delta);
				object.transform = oldTransform;
			}
		});
	}

	public void pause() {
		isPlaying = false;
		EventSystem.notify(null, new Event(EventType.STOP_PLAY));
	}

	public void play() {
		isPlaying = true;
		EventSystem.notify(null, new Event(EventType.START_PLAY));
	}

	public void addObject(GameObject object, int zIndex) {
		object.setZIndex(zIndex);
		if (object.getComponent(Rigidbody.class) != null) {
			physicsEngine.add(object);
		}
		objects.add(object);
		objects.sort((o1, o2) -> {
			return o1.getzIndex() - o2.getzIndex();
		});
	}

	public void addObject(GameObject object) {
		addObject(object, 0);
	}

	public void addObjects(GameObject... objects) {
		for (GameObject obj : objects) {
			addObject(obj);
		}
	}

	public void removeObject(GameObject gameObject) {
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			physicsEngine.removeRigidbody(object);
			if (object.equals(gameObject)) {
				objects.remove(i);
			}
		}
	}

	public GameObject getObject(long id) {
		for (GameObject obj : objects) {
			if (obj.getId() == id) {
				return obj;
			}
		}
		return null;
	}

	public <T extends GameObject> T getObject(Class<T> objectClass) {
		for (GameObject obj : objects) {
			if (objectClass.isAssignableFrom(obj.getClass())) {
				try {
					return objectClass.cast(obj);
				} catch (ClassCastException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
		return null;
	}

	public ArrayList<GameObject> getObjects() {
		return objects;
	}

	public Camera getCamera() {
		return camera;
	}

	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

}
