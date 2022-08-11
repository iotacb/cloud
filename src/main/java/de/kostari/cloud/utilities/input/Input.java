package de.kostari.cloud.utilities.input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.math.Vec;

public class Input {

    private static long windowId;

    private static boolean[] buttons;
    private static boolean hasGamepad;
    private static boolean[] keys, mouseButtons;

    private static int lastKey;

    private static Vec mousePosition = new Vec();
    private static Vec lastMousePosition = new Vec();
    private static Vec mouseScroll = new Vec();

    private static boolean isWindows;

    private static InputTypes usingInput;

    public static void initInput(Window window, boolean isWindows) {
        Input.windowId = window.getWindowId();
        Input.isWindows = isWindows;
        Input.keys = new boolean[GLFW_KEY_LAST];
        Input.mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
        Input.hasGamepad = glfwGetJoystickName(GLFW_JOYSTICK_1) != null;
        for (int i = 32; i < GLFW_KEY_LAST; i++) {
            keys[i] = false;
        }
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            mouseButtons[i] = false;
        }
        if (hasGamepad) {
            Input.buttons = new boolean[GLFW_GAMEPAD_BUTTON_LAST];
            for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
                buttons[i] = false;
            }
        }
    }

    /**
     * Returns the axis of the left gamepad stick.
     * 
     * @return
     */
    public static Vec getLeftJoystick() {
        if (!hasGamepad)
            return Vec.zero;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float x = axes.get(0);
        float y = axes.get(1);

        // Update input type
        if (Math.abs(x) > 0.5 || Math.abs(y) > 0.5) {
            usingInput = InputTypes.GAMEPAD;
        }
        return new Vec(x, y);
    }

    public static float getLeftTrigger() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(4);
        return a;
    }

    /**
     * Returns the axis of the left gamepad stick.
     * 
     * @return
     */
    public static Vec getRightJoystick() {
        if (!hasGamepad)
            return Vec.zero;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float x = axes.get(2);
        float y = axes.get(3);

        // Update input type
        if (Math.abs(x) > 0.5 || Math.abs(y) > 0.5) {
            usingInput = InputTypes.GAMEPAD;
        }
        return new Vec(x, y);
    }

    public static float getRightTrigger() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(5);
        return a;
    }

    /**
     * Returns true when the button is being pressed.
     * 
     * @param buttonCode
     * @return
     */
    public static boolean buttonState(int buttonCode) {
        if (!hasGamepad)
            return false;
        boolean r = false;
        ByteBuffer buttons = glfwGetJoystickButtons(GLFW_JOYSTICK_1);

        if (buttons == null)
            return false;

        for (int i = 0; i < buttons.capacity(); i++) {
            if (buttonCode != (15 | 16)) {
                if (i == buttonCode) {
                    r = (buttons.get(i) == 1);
                }
            } else {
                if (buttonCode == 15) {
                    r = isLeftTriggerPressed();
                }
                if (buttonCode == 16) {
                    r = isRightTriggerPressed();
                }
            }
        }
        return r;
    }

    /**
     * Returns true when the button has been pressed.
     * 
     * @param buttonCode
     * @return
     */
    public static boolean buttonPressed(int buttonCode) {
        if (!hasGamepad)
            return false;

        boolean isDown = (buttonState(buttonCode) && !buttons[buttonCode]);
        return isDown;
    }

    /**
     * Returns true when the button has been released.
     * 
     * @param buttonCode
     * @return
     */
    public static boolean buttonRelease(int buttonCode) {
        if (!hasGamepad)
            return false;

        boolean isUp = (buttonState(buttonCode) && buttons[buttonCode]);
        return isUp;
    }

    /**
     * Returns 0 or 1 depending on the button state.
     * for example when the 'a'/'x' button is pressed the value will be 1.
     * 
     * @param buttonCode
     * @return
     */
    public static int buttonStrength(int buttonCode) {
        return buttonState(buttonCode) ? 1 : 0;
    }

    /**
     * Returns true when the key is being pressed.
     * 
     * @param keyCode
     * @return
     */
    public static boolean keyState(int keyCode) {
        return glfwGetKey(windowId, keyCode) == 1;
    }

    /**
     * Returns true when the key has been pressed.
     * 
     * @param keyCode
     * @return
     */
    public static boolean keyPressed(int keyCode) {
        boolean isDown = (keyState(keyCode) && !keys[keyCode]);
        if (hasGamepad) {
            if (isDown) {
                usingInput = InputTypes.KEYBOARD;
            }
        }
        return isDown;
    }

    /**
     * Returns true when the key has been released.
     * 
     * @param keyCode
     * @return
     */
    public static boolean keyRelease(int keyCode) {
        boolean isUp = (!keyState(keyCode) && keys[keyCode]);
        if (hasGamepad) {
            if (isUp) {
                usingInput = InputTypes.KEYBOARD;
            }
        }
        return isUp;
    }

    /**
     * Returns 0 or 1 depending on the key state.
     * for example when the 'a' key is pressed the value will be 1.
     * 
     * @param keyCode
     * @return
     */
    public static int keyStrength(int keyCode) {
        return keyState(keyCode) ? 1 : 0;
    }

    public static boolean isLeftTriggerPressed() {
        if (!hasGamepad)
            return false;
        boolean isPressed = getLeftTrigger() == 1;
        if (isPressed) {
            usingInput = InputTypes.GAMEPAD;
        }
        return isPressed;
    }

    /**
     * Returns true when the mouse button is beeing pressed.
     * 
     * @param mouseButton
     * @return
     */
    public static boolean mouseButtonState(int mouseButton) {
        return glfwGetMouseButton(windowId, mouseButton) == 1;
    }

    /**
     * Returns true when the mouse button has been pressed.
     * 
     * @param mouseButton
     * @return
     */
    public static boolean mouseButtonPressed(int mouseButton) {
        boolean isDown = (mouseButtonState(mouseButton) && !mouseButtons[mouseButton]);
        if (hasGamepad) {
            if (isDown) {
                usingInput = InputTypes.KEYBOARD;
            }
        }
        return isDown;
    }

    /**
     * Returns true when the mouse button has been released.
     * 
     * @param mouseButton
     * @return
     */
    public static boolean mouseButtonReleased(int mouseButton) {
        boolean isUp = (!mouseButtonState(mouseButton) && mouseButtons[mouseButton]);
        if (hasGamepad) {
            if (isUp) {
                usingInput = InputTypes.KEYBOARD;
            }
        }
        return isUp;
    }

    /**
     * Returns 0 or 1 depending on the mouse button state.
     * for example when the left mouse button is pressed the value will be 1.
     * 
     * @param mouseButton
     * @return
     */
    public static int mouseButtonStrength(int mouseButton) {
        return mouseButtonState(mouseButton) ? 1 : 0;
    }

    public static boolean isRightTriggerPressed() {
        if (!hasGamepad)
            return false;
        boolean isPressed = getRightTrigger() == 1;
        if (isPressed) {
            usingInput = InputTypes.GAMEPAD;
        }
        return isPressed;
    }

    public static boolean noButtonPressed() {
        if (!hasGamepad)
            return true;
        boolean r = true;
        for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
            if (buttons[i]) {
                r = false;
            }
        }
        if (isLeftTriggerPressed() || isRightTriggerPressed()) {
            r = false;
        }
        return r;
    }

    public static boolean isMouseMoving() {
        return !mousePosition.equals(lastMousePosition);
    }

    public static boolean isMousePressed() {
        boolean pressed = false;
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            if (mouseButtons[i]) {
                pressed = true;
            }
        }
        return pressed;
    }

    public static boolean noKeyPressed() {
        boolean r = true;
        for (int i = 32; i < GLFW_KEY_LAST; i++) {
            if (keys[i]) {
                r = false;
            }
        }
        return r;
    }

    public static List<String> listenInputs() {
        List<String> inputs = new ArrayList<>();

        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            if (keyState(i)) {
                inputs.add("KEY: " + i);
            }
        }
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            if (mouseButtonState(i)) {
                inputs.add("MOUSE: " + i);
            }
        }
        for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
            if (buttonState(i)) {
                inputs.add("GAMEPAD: " + i);
            }
        }

        return inputs;
    }

    public static void updateInput() {
        hasGamepad = glfwGetJoystickName(GLFW_JOYSTICK_1) != null;
        if (getLastMouseX() != getMouseX()
                || getLastMouseY() != getMouseY()) {
            usingInput = InputTypes.KEYBOARD;
        }
        for (int i = 32; i < GLFW_KEY_LAST; i++) {
            keys[i] = keyState(i);
            if (keys[i]) {
                lastKey = i;
                usingInput = InputTypes.KEYBOARD;
            }
        }
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            mouseButtons[i] = mouseButtonState(i);
        }

        if (hasGamepad) {
            if (buttons == null) {
                buttons = new boolean[GLFW_GAMEPAD_BUTTON_LAST];
                for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
                    buttons[i] = false;
                }
            }
            for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
                buttons[i] = buttonState(i);
                if (buttons[i]) {
                    usingInput = InputTypes.GAMEPAD;
                }
            }
            if (isWindows) {
                isLeftTriggerPressed();
                isRightTriggerPressed();
                getLeftJoystick();
                getRightJoystick();
            }
        }
    }

    public static int getLastKey() {
        return lastKey;
    }

    public static boolean hasGamepad() {
        return hasGamepad;
    }

    public static InputTypes getUsingInput() {
        return usingInput;
    }

    public static int getMouseX() {
        return (int) mousePosition.x;
    }

    public static int getMouseY() {
        return (int) mousePosition.y;
    }

    public static int getLastMouseX() {
        return (int) lastMousePosition.x;
    }

    public static int getLastMouseY() {
        return (int) lastMousePosition.y;
    }

    public static int getScrollX() {
        return (int) mouseScroll.x;
    }

    public static int getScrollY() {
        return (int) mouseScroll.y;
    }

    public static Vec getMousePosition() {
        return mousePosition;
    }

    public static Vec getLastMousePosition() {
        return lastMousePosition;
    }

    public static void setMousePosition(float x, float y) {
        Input.mousePosition.set(x, y);
    }

    public static void setLastMousePosition(float x, float y) {
        Input.lastMousePosition.set(x, y);
    }

    public static void setMouseScroll(float x, float y) {
        Input.mouseScroll.set(x, y);
    }
}