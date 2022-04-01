package de.kostari.cloud.core.scene;

import java.awt.Color;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.math.Maths;
import de.kostari.cloud.utilities.render.Render;

public class Camera extends GameObject {

    private float posXDiff, posYDiff;

    public Camera(Window window) {
        super(window);
        ignoreCameraMovement();
    }

    public void followObject(GameObject object, float damping) {
        float xOffset = window.getWidth() / 2;
        float yOffset = window.getHeight() / 2;

        float objCenterWidth = object.getComponent(Bounds.class).width / 2;
        float objCenterHeight = object.getComponent(Bounds.class).height / 2;

        posXDiff = object.transform.position.x - transform.position.x - xOffset + objCenterWidth;
        posYDiff = object.transform.position.y - transform.position.y - yOffset + objCenterHeight;

        transform.position.x = Maths.lerp(transform.position.x,
                object.transform.position.x - xOffset + objCenterWidth,
                damping);
        transform.position.y = Maths.lerp(transform.position.y,
                object.transform.position.y - yOffset + objCenterHeight,
                damping);
    }

    public void followObject(GameObject object) {
        followObject(object, 0.1F);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void draw(float delta) {
        if (!doDrawDebug())
            return;
        Render.rect(window.getWidth() / 2, window.getHeight() / 2, posXDiff, 2, Color.GREEN);
        Render.rect(window.getWidth() / 2, window.getHeight() / 2, 2, posYDiff, Color.RED);
        super.draw(delta);
    }

    public Transform getTransform() {
        return transform;
    }

}
