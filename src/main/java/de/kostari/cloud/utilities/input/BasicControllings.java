package de.kostari.cloud.utilities.input;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;

public class BasicControllings {

    /**
     * Moves the provided gameObject in the provided direction.
     * 
     * @param gameObject The gameObject to move.
     * @param direction  The direction to move the gameObject in.
     * @param speed      The speed to move with.
     */
    public static void moveDirection(GameObject gameObject, float direction, float speed) {
        gameObject.transform.position.x += CMath.lengthDirX(speed, direction);
        gameObject.transform.position.y += CMath.lengthDirY(speed, direction);
    }

    /**
     * Moves the provided gameObject away from the provided direction.
     * 
     * @param gameObject The gameObject to move.
     * @param direction  The direction to move the gameObject away from.
     * @param speed      The speed to move with.
     */
    public static void leaveDirection(GameObject gameObject, float direction, float speed) {
        gameObject.transform.position.x -= CMath.lengthDirX(speed, direction);
        gameObject.transform.position.y -= CMath.lengthDirY(speed, direction);
    }

    /**
     * Moves the provided gameObject to the provided position
     * 
     * @param gameObject  The gameObject to move.
     * @param posX        The x position to move the gameObject to.
     * @param posY        The y position to move the gameObject to.
     * @param speed       The speed to move with.
     * @param minDistance The minimum distance that the gameObject has to be from
     *                    the position to move to.
     */
    public static void moveToPos(GameObject gameObject, float posX, float posY, float speed, float minDistance) {
        float direction = CMath.direction(gameObject.transform.position.x, gameObject.transform.position.y, posX, posY)
                - 90;
        if (CMath.dist(gameObject.transform.position.x, gameObject.transform.position.y, posX, posY) >= minDistance)
            moveDirection(gameObject, direction, speed);
    }

    /**
     * Moves the provided gameObject to the provided position
     * 
     * @param gameObject  The gameObject to move.
     * @param pos         The position to move the gameObject to.
     * @param speed       The speed to move with.
     * @param minDistance The minimum distance that the gameObject has to be from
     *                    the position to move to.
     */
    public static void moveToPos(GameObject gameObject, Vec pos, float speed, float minDistance) {
        moveToPos(gameObject, pos.x, pos.y, speed, minDistance);
    }

    /**
     * Moves the provided gameObject away from the provided position
     * 
     * @param gameObject  The gameObject to move.
     * @param posX        The x position to move the gameObject to.
     * @param posY        The y position to move the gameObject to.
     * @param speed       The speed to move with.
     * @param minDistance The minimum distance that the gameObject has to be from
     *                    the position to move to.
     */
    public static void leavePos(GameObject gameObject, float posX, float posY, float speed,
            float minDistance) {
        float direction = CMath.direction(gameObject.transform.position.x, gameObject.transform.position.y, posX,
                posY);
        if (CMath.dist(gameObject.transform.position.x, gameObject.transform.position.y, posX,
                posY) > minDistance)
            leaveDirection(gameObject, direction, speed);
    }

    /**
     * Moves the provided gameObject away from the provided position
     * 
     * @param gameObject  The gameObject to move.
     * @param pos         The position to move the gameObject away from.
     * @param speed       The speed to move with.
     * @param minDistance The minimum distance that the gameObject has to be from
     *                    the position to move to.
     */
    public static void leavePos(GameObject gameObject, Vec pos, float speed, float minDistance) {
        leavePos(gameObject, pos.x, pos.y, speed, minDistance);
    }

    /**
     * Moves the gameObject with the provided keys.
     * 
     * @param gameObject The gameObject to move.
     * @param keyLeft    The key to move the gameObject to the left.
     * @param keyRight   The key to move the gameObject to the right.
     * @param keyUp      The key to move the gameObject to the up.
     * @param keyDown    The key to move the gameObject to the down.
     * @param speed      The speed to move with
     */
    public static void moveWithKeys(GameObject gameObject, int keyLeft, int keyRight, int keyUp, int keyDown,
            float speed) {

        Vec input = new Vec();

        input.x = Input.keyStrength(keyRight) - Input.keyStrength(keyLeft);
        input.y = Input.keyStrength(keyDown) - Input.keyStrength(keyUp);
        Vec velocity = input.normalize().mul(speed);

        gameObject.transform.position.add(velocity);
    }

    /**
     * Moves the provided gameObject with the W,A,S,D keys.
     * 
     * @param gameObject The gameObject to move.
     * @param speed      The speed to move with.
     */
    public static void moveWASD(GameObject gameObject, float speed) {
        moveWithKeys(gameObject, Keys.KEY_A, Keys.KEY_D, Keys.KEY_W, Keys.KEY_S, speed);
    }

    /**
     * Moves the provided gameObject with the arrow keys.
     * 
     * @param gameObject The gameObject to move.
     * @param speed      The speed to move with.
     */
    public static void moveArrows(GameObject gameObject, float speed) {
        moveWithKeys(gameObject, Keys.KEY_LEFT, Keys.KEY_RIGHT, Keys.KEY_UP, Keys.KEY_DOWN, speed);
    }

    /**
     * Moves the provided gameObject with the gamepad.
     * 
     * @param gameObject The gameObject to move.
     * @param speed      The speed to move with.
     * @param threshold  The threshold for gamepad input to prevent accidental
     *                   movement.
     * @param moveStick  The gamepad stick to move.
     */
    public static void moveWithGamepad(GameObject gameObject, float speed, float threshold, int moveStick) {
        Vec input = moveStick == 0 ? Input.getLeftJoystick() : Input.getRightJoystick();

        input.x = Math.abs(input.x) > threshold ? input.x : 0;
        input.y = Math.abs(input.y) > threshold ? input.y : 0;

        Vec velocity = input.mul(speed);

        gameObject.transform.position.add(velocity);
    }

    /**
     * Moves the provided gameObject with the detected input mode.
     * 
     * @param gameObject The gameObject to move.
     * @param keyLeft    The key to move left.
     * @param keyRight   The key to move right.
     * @param keyUp      The key to move up.
     * @param keyDown    The key to move down.
     * @param speed      The speed to move with.
     * @param threshold  The threshold for gamepad input to prevent accidental
     *                   movement.
     * @param moveStick  The gamepad stick to move.
     */
    public static void moveWithDetectedInput(GameObject gameObject, int keyLeft, int keyRight, int keyUp, int keyDown,
            float speed, float threshold, int moveStick) {

        if (Input.getUsingInput() == InputTypes.GAMEPAD) {
            moveWithGamepad(gameObject, speed, threshold, moveStick);
        } else {
            moveWithKeys(gameObject, keyLeft, keyRight, keyUp, keyDown, speed);
        }
    }

}
