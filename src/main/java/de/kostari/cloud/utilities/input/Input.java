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

    public static float getLeftJoystickX() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(0);
        if (Math.abs(a) > 0.5) {
            usingInput = InputTypes.GAMEPAD;
        }
        return a;
    }

    public static float getLeftJoystickY() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(1);
        if (Math.abs(a) > 0.5) {
            usingInput = InputTypes.GAMEPAD;
        }
        return a;
    }

    public static float getLeftTrigger() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(4);
        return a;
    }

    public static float getRightJoystickX() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(2);
        if (Math.abs(a) > 0.5) {
            usingInput = InputTypes.GAMEPAD;
        }
        return a;
    }

    public static float getRightJoystickY() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(3);
        if (Math.abs(a) > 0.5) {
            usingInput = InputTypes.GAMEPAD;
        }
        return a;
    }

    public static float getRightTrigger() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(5);
        return a;
    }

    public static boolean getButton(int buttonCode) {
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

    public static boolean getButtonDown(int buttonCode) {
        if (!hasGamepad)
            return false;

        boolean isDown = (getButton(buttonCode) && !buttons[buttonCode]);
        return isDown;
    }

    public static boolean getButtonUp(int buttonCode) {
        if (!hasGamepad)
            return false;

        boolean isUp = (getButton(buttonCode) && buttons[buttonCode]);
        return isUp;
    }

    public static boolean getKey(int keyCode) {
        return glfwGetKey(windowId, keyCode) == 1;
    }

    public static boolean getKeyDown(int keyCode) {
        boolean isDown = (getKey(keyCode) && !keys[keyCode]);
        if (hasGamepad) {
            if (isDown) {
                usingInput = InputTypes.KEYBOARD;
            }
        }
        return isDown;
    }

    public static boolean getKeyUp(int keyCode) {
        boolean isUp = (!getKey(keyCode) && keys[keyCode]);
        if (hasGamepad) {
            if (isUp) {
                usingInput = InputTypes.KEYBOARD;
            }
        }
        return isUp;
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

    public static boolean getMouseButton(int mouseButton) {
        return glfwGetMouseButton(windowId, mouseButton) == 1;
    }

    public static boolean getMouseButtonDown(int mouseButton) {
        boolean isDown = (getMouseButton(mouseButton) && !mouseButtons[mouseButton]);
        if (hasGamepad) {
            if (isDown) {
                usingInput = InputTypes.KEYBOARD;
            }
        }
        return isDown;
    }

    public static boolean getMouseButtonUp(int mouseButton) {
        boolean isUp = (!getMouseButton(mouseButton) && mouseButtons[mouseButton]);
        if (hasGamepad) {
            if (isUp) {
                usingInput = InputTypes.KEYBOARD;
            }
        }
        return isUp;
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
            if (getKey(i)) {
                inputs.add("KEY: " + i);
            }
        }
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            if (getMouseButton(i)) {
                inputs.add("MOUSE: " + i);
            }
        }
        for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
            if (getButton(i)) {
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
            keys[i] = getKey(i);
            if (keys[i]) {
                lastKey = i;
                usingInput = InputTypes.KEYBOARD;
            }
        }
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            mouseButtons[i] = getMouseButton(i);
        }

        if (hasGamepad) {
            if (buttons == null) {
                buttons = new boolean[GLFW_GAMEPAD_BUTTON_LAST];
                for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
                    buttons[i] = false;
                }
            }
            for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
                buttons[i] = getButton(i);
                if (buttons[i]) {
                    usingInput = InputTypes.GAMEPAD;
                }
            }
            if (isWindows) {
                isLeftTriggerPressed();
                isRightTriggerPressed();
                getLeftJoystickX();
                getLeftJoystickY();
                getRightJoystickX();
                getRightJoystickY();
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