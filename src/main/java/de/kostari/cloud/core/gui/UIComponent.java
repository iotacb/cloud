package de.kostari.cloud.core.gui;

import java.util.ArrayList;
import java.util.List;

import javax.naming.ConfigurationException;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.components.Component;
import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.window.Window;

public class UIComponent {
    public Window window;

    public Transform transform;

    public UIComponent parent;

    private List<Component> components;
    private List<UIComponent> children;

    private int zIndex = 0;

    public UIComponent(Window window, Transform transform, UIComponent parent) {
        init(window, transform, parent);
    }

    public UIComponent(Window window, Transform transform) {
        init(window, transform, null);
    }

    public UIComponent(Window window, UIComponent parent) {
        init(window, new Transform(), parent);
    }

    public UIComponent(Window window) {
        init(window, new Transform(), null);
    }

    private void init(Window window, Transform transform, UIComponent parent) {
        this.window = window;
        this.transform = transform;
        this.parent = parent;
        this.components = new ArrayList<>();
        this.children = new ArrayList<>();

        addComponent(new Bounds(0, 0));
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

    public UIComponent addComponent(Component component) {
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
        component.ui = this;
        component.start();
        components.add(component);
        return this;
    }

    public List<Component> getComponents() {
        return components;
    }

    public UIComponent setZIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public int getzIndex() {
        return zIndex;
    }

    public UIComponent getParent() {
        return parent;
    }

    public UIComponent setParent(UIComponent parent) {
        this.parent = parent;
        this.parent.getChildren().add(this);
        return this;
    }

    public List<UIComponent> getChildren() {
        return children;
    }

    public UIComponent setWidth(float val) {
        Bounds bounds = getComponent(Bounds.class);
        bounds.setWidth(val);
        return this;
    }

    public UIComponent setWidthPercentage(float val) {
        if (parent == null)
            return this;

        Bounds parentBounds = parent.getComponent(Bounds.class);
        setWidth(parentBounds.getWidth() * val);
        return this;
    }

    public UIComponent setHeight(float val) {
        Bounds bounds = getComponent(Bounds.class);
        bounds.setHeight(val);
        return this;
    }

    public UIComponent setHeightPercentage(float val) {
        if (parent == null)
            return this;

        Bounds parentBounds = parent.getComponent(Bounds.class);
        setHeight(parentBounds.getHeight() * val);
        return this;
    }

    public UIComponent setSize(float width, float height) {
        setWidth(width);
        setHeight(height);
        return this;
    }

    public UIComponent setSizePercentage(float widthVal, float heightVal) {
        if (parent == null)
            return this;

        setWidthPercentage(widthVal);
        setHeightPercentage(heightVal);
        return this;
    }

    public UIComponent fullWidth() {
        setWidthPercentage(1);
        return this;
    }

    public UIComponent fullHeight() {
        setHeightPercentage(1);
        return this;
    }

    public UIComponent fullSize() {
        fullWidth();
        fullHeight();
        return this;
    }

}
