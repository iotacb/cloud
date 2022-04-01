package de.kostari.cloud.core.objects;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.components.Component;
import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.window.Window;

public class GameObject {
    public Window window;
    private List<Component> components;
    private long id;
    private boolean ignoreCameraMovement = false;

    private int zIndex = 0;

    private boolean drawDebug;

    public Transform transform;
    public String tag;

    public GameObject(Window window) {
        init(window, -1, "tag", new Transform());
    }

    public GameObject(Window window, String tag) {
        init(window, -1, tag, new Transform());
    }

    public GameObject(Window window, Transform transform) {
        init(window, -1, "tag", transform);
    }

    public GameObject(Window window, String tag, Transform transform) {
        init(window, -1, tag, transform);
    }

    public GameObject(Window window, long id, Transform transform) {
        init(window, id, "tag", transform);
    }

    public GameObject(Window window, long id, String tag, Transform transform) {
        init(window, id, tag, transform);
    }

    private void init(Window window, long id, String tag, Transform transform) {
        this.window = window;
        this.id = id;
        this.tag = tag;
        this.transform = transform;
        this.components = new ArrayList<>();
    }

    public void update(float delta) {
        components.forEach(component -> {
            component.update(delta);
        });
    }

    public void draw(float delta) {
        components.forEach(component -> {
            component.draw(delta);
        });
    }

    public void start() {
        components.forEach(component -> {
            component.start();
        });
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return null;
    }

    public List<Component> getComponents() {
        return components;
    }

    public GameObject addComponent(Component component) {
        component.gameObject = this;
        component.start();
        components.add(component);
        return this;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getzIndex() {
        return zIndex;
    }

    public long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public boolean isIgnoringCameraMovement() {
        return ignoreCameraMovement;
    }

    public GameObject ignoreCameraMovement() {
        ignoreCameraMovement = true;
        return this;
    }

    public boolean doDrawDebug() {
        return drawDebug;
    }

    public GameObject drawDebug() {
        drawDebug = true;
        return this;
    }

    public GameObject setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof GameObject))
            return false;

        GameObject gameObject = (GameObject) o;
        return gameObject.equals(this) || gameObject.id == this.id;
    }
}
