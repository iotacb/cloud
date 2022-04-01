package de.kostari.cloud.core.gui;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.components.Component;
import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.window.Window;

public class GUI {
    public Window window;
    private List<Component> components;

    private int zIndex = 0;

    public Transform transform;

    public GUI(Window window, Transform transform) {
        this.window = window;
        this.transform = transform;
        this.components = new ArrayList<>();
    }

    public GUI(Window window) {
        this.window = window;
        this.transform = new Transform();
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

    public GUI addComponent(Component component) {
        component.gui = this;
        components.add(component);
        return this;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public int getzIndex() {
        return zIndex;
    }
}
