package de.kostari.cloud.core.scene;

import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Matrix;
import de.kostari.cloud.utilities.math.Vec;

public class Camera {

    public Vec target;
    public Vec offset;
    public float zoom;
    public float rotation;

    public Camera() {
        this.target = Vec.zero;
        this.offset = Vec.zero;
        this.zoom = 1;
        this.rotation = 0;
    }

    public Matrix getMatrix() {
        return Camera.GetCameraMatrix(this);
    }

    public static Matrix GetCameraMatrix(Camera cam) {
        Matrix matTransform = new Matrix();
        Matrix matOrigin = CMath.MatrixTranslate(-cam.target.x, -cam.target.getY(), 0);
        Matrix matRotation = CMath.MatrixRotate(0, 0, 1, cam.rotation * CMath.DEG2RAD);
        Matrix matScale = CMath.MatrixScale(cam.zoom, cam.zoom, 1);
        Matrix matTranslation = CMath.MatrixTranslate(cam.offset.x, cam.offset.y, 0);

        matTransform = CMath.MatrixMultiply(
                CMath.MatrixMultiply(matOrigin, CMath.MatrixMultiply(matScale, matRotation)), matTranslation);
        return matTransform;
    }

}
