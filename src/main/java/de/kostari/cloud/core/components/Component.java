package de.kostari.cloud.core.components;

import de.kostari.cloud.core.gui.GUI;
import de.kostari.cloud.core.objects.GameObject;

public abstract class Component {

    public GameObject gameObject;
    public GUI gui;

    public Component(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public Component(GUI gui) {
        this.gui = gui;
    }

    public Component() {
    }

    public void start() {
    }

    public void update(float delta) {
    }

    public void draw(float delta) {
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return gameObject.getComponent(componentClass);
    }

}
