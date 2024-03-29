package de.kostari.cloud.core.objects;

import java.util.ArrayList;
import java.util.List;

import javax.naming.ConfigurationException;

import de.kostari.cloud.core.components.Component;
import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.window.Window;
import lombok.Getter;
import lombok.Setter;

public class GameObject {

    private Window window;

    public String tag;
    public Transform transform;

    @Getter
    @Setter
    private int id;

    @Getter
    private List<Component> components;

    @Getter
    @Setter
    private int zIndex = 0;

    @Getter
    @Setter
    private boolean drawDebug;

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

    public GameObject(Window window, int id, Transform transform) {
        init(window, id, "tag", transform);
    }

    public GameObject(Window window, int id, String tag, Transform transform) {
        init(window, id, tag, transform);
    }

    private void init(Window window, int id, String tag, Transform transform) {
        this.window = window;
        this.id = id;
        this.tag = tag;
        this.transform = transform;
        this.components = new ArrayList<>();
        start();
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

    /**
     * Search for a component in the components list and return it
     * 
     * @param <T>            The type of the component
     * @param componentClass The class of the component
     * @return
     */
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

    /**
     * Add a component to the gameobject.
     * 
     * @param component the component to add
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T addComponent(Component component) {
        for (Component com : components) {
            if (com.getClass() == component.getClass()) {
                try {
                    throw new ConfigurationException(
                            String.format("Component '%s' already exists", component.getClass().getSimpleName()));
                } catch (ConfigurationException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        component.gameObject = this;
        component.start();
        components.add(component);

        if (component.getClass().isAssignableFrom(component.getClass())) {
            try {
                return (T) component.getClass().cast(component);
            } catch (ClassCastException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return null;
    }

    public boolean doDrawDebug() {
        return drawDebug;
    }

    /**
     * Draw debug visuals for the gameobject.
     * 
     * @return
     */
    public GameObject drawDebug() {
        drawDebug = true;
        return this;
    }

    /**
     * Destroy the gameobject
     */
    public void destroy() {
        window.getScene().removeObject(this);
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
        return gameObject.id == this.id;
    }

    public Window getWindow() {
        return window;
    }
}
