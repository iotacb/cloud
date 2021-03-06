package de.kostari.cloud.core.scene;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.render.Render;

public class Camera extends GameObject {

    private float posXDiff, posYDiff;

    public Camera(Window window) {
        super(window);
        ignoreCameraMovement();
    }

    /**
     * Will center the camera to a specific GameObject
     * 
     * @param object  the object to center the camera to
     * @param damping the damping of the camera movement
     */
    public void followObject(GameObject object, float damping) {
        float xOffset = getWindow().getWidth() / 2;
        float yOffset = getWindow().getHeight() / 2;

        float objCenterWidth = object.getComponent(Bounds.class).getWidth() / 2;
        float objCenterHeight = object.getComponent(Bounds.class).getHeight() / 2;

        posXDiff = object.transform.position.x - transform.position.x - xOffset + objCenterWidth;
        posYDiff = object.transform.position.y - transform.position.y - yOffset + objCenterHeight;

        transform.position.x = CMath.lerp(transform.position.x,
                object.transform.position.x - xOffset + objCenterWidth,
                damping);
        transform.position.y = CMath.lerp(transform.position.y,
                object.transform.position.y - yOffset + objCenterHeight,
                damping);
    }

    /**
     * Will center the camera to a specific GameObject
     * 
     * @param object the object to center the camera to
     */
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
        Render.rect(getWindow().getWidth() / 2, getWindow().getHeight() / 2, posXDiff, 2, CColor.GREEN);
        Render.rect(getWindow().getWidth() / 2, getWindow().getHeight() / 2, 2, posYDiff, CColor.RED);
        super.draw(delta);
    }

    public Transform getTransform() {
        return transform;
    }

}
