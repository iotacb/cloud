package de.kostari.cloud.utilities.input;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.utilities.math.Maths;
import de.kostari.cloud.utilities.math.Vec;

public class BasicControllings {

    public static void moveTo(GameObject gameObject, float direction, float speed, int key) {
        if (gameObject.window.getInput().getKey(key) || key == -1) {
            gameObject.transform.position.x += Maths.lengthDirX(speed, direction);
            gameObject.transform.position.y += Maths.lengthDirY(speed, direction);
        }
    }

    public static void moveAway(GameObject gameObject, float direction, float speed, int key) {
        if (gameObject.window.getInput().getKey(key) || key == -1) {
            gameObject.transform.position.x -= Maths.lengthDirX(speed, direction);
            gameObject.transform.position.y -= Maths.lengthDirY(speed, direction);
        }
    }

    public static void moveToLocation(GameObject gameObject, float locationX, float locationY, float speed,
            float threshold,
            int key) {
        float direction = Maths.direction(gameObject.transform.position.x, gameObject.transform.position.y, locationX,
                locationY) - 90;
        if (Maths.dist(gameObject.transform.position.x, gameObject.transform.position.y, locationX,
                locationY) >= threshold)
            moveTo(gameObject, direction, speed, key);
    }

    public static void moveToLocation(GameObject gameObject, Vec location, float speed, float threshold, int key) {
        moveToLocation(gameObject, location.x, location.y, speed, threshold, key);
    }

    public static void leaveLocation(GameObject gameObject, float locationX, float locationY, float speed,
            float threshold,
            int key) {
        float direction = Maths.direction(gameObject.transform.position.x, gameObject.transform.position.y, locationX,
                locationY);
        if (Maths.dist(gameObject.transform.position.x, gameObject.transform.position.y, locationX,
                locationY) > threshold)
            moveAway(gameObject, direction, speed, key);
    }

    public static void leaveLocation(GameObject gameObject, Vec location, float speed, float threshold, int key) {
        leaveLocation(gameObject, location.x, location.y, speed, threshold, key);
    }

    public static void moveWithKeys(GameObject gameObject, int keyLeft, int keyRight, int keyUp, int keyDown,
            float speed) {
        int moveX = gameObject.window.getInput().getKey(keyLeft) ? -1
                : gameObject.window.getInput().getKey(keyRight) ? 1 : 0;
        int moveY = gameObject.window.getInput().getKey(keyUp) ? -1
                : gameObject.window.getInput().getKey(keyDown) ? 1 : 0;

        Vec input = new Vec(moveX, moveY);
        Vec direction = input.normalize();
        Vec velocity = direction.mul(speed);

        gameObject.transform.position.add(velocity);
    }

    public static void moveWASD(GameObject gameObject, float speed) {
        moveWithKeys(gameObject, Keys.KEY_A, Keys.KEY_D, Keys.KEY_W, Keys.KEY_S, speed);
    }

    public static void moveArrows(GameObject gameObject, float speed) {
        moveWithKeys(gameObject, Keys.KEY_LEFT, Keys.KEY_RIGHT, Keys.KEY_UP, Keys.KEY_DOWN, speed);
    }

    public static void moveWithGamepad(GameObject gameObject, float speed, float threshold, int moveStick) {
        float stickX = moveStick == 0 ? gameObject.window.getInput().getLeftJoystickX()
                : gameObject.window.getInput().getRightJoystickX();
        float stickY = moveStick == 0 ? gameObject.window.getInput().getLeftJoystickY()
                : gameObject.window.getInput().getRightJoystickY();

        stickX = Math.abs(stickX) > threshold ? stickX : 0;
        stickY = Math.abs(stickY) > threshold ? stickY : 0;

        Vec input = new Vec(stickX, stickY);
        Vec velocity = input.mul(speed);

        gameObject.transform.position.add(velocity);
    }

    public static void moveWithDetectedInput(GameObject gameObject, int keyLeft, int keyRight, int keyUp, int keyDown,
            float speed, float threshold, int moveStick) {

        if (gameObject.window.getInput().isUsingGamepad()) {
            moveWithGamepad(gameObject, speed, threshold, moveStick);
        } else {
            moveWithKeys(gameObject, keyLeft, keyRight, keyUp, keyDown, speed);
        }
    }

}
