package de.kostari.cloud.utilities.input;

import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.window.Window;

public class Input {

    private boolean[] buttons;
    private boolean hasGamepad, usingGamepad;

    boolean isWindows;

    public boolean[] keys, mouseButtons;

    public int lastKey;

    Window window;

    long windowId;

    public Input(long windowId, Window window, boolean isWindows) {
        this.windowId = windowId;
        this.window = window;
        this.isWindows = isWindows;
        this.keys = new boolean[GLFW_KEY_LAST];
        this.mouseButtons = new boolean[2];
        this.hasGamepad = glfwGetJoystickName(GLFW_JOYSTICK_1) != null;
        this.usingGamepad = hasGamepad;
        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            keys[i] = false;
        }
        for (int i = 0; i < 2; i++) {
            mouseButtons[i] = false;
        }
        if (hasGamepad) {
            this.buttons = new boolean[GLFW_GAMEPAD_BUTTON_LAST];
            for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
                buttons[i] = false;
            }
        }
    }

    public float getLeftJoystickX() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(0);
        if (Math.abs(a) > 0.5)
            usingGamepad = true;
        return a;
    }

    public float getLeftJoystickY() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(1);
        if (Math.abs(a) > 0.5)
            usingGamepad = true;
        return a;
    }

    public float getLeftTrigger() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(4);
        return a;
    }

    public float getRightJoystickX() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(2);
        if (Math.abs(a) > 0.5)
            usingGamepad = true;
        return a;
    }

    public float getRightJoystickY() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(3);
        if (Math.abs(a) > 0.5)
            usingGamepad = true;
        return a;
    }

    public float getRightTrigger() {
        if (!hasGamepad)
            return 0;
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float a = axes.get(5);
        return a;
    }

    public boolean getButton(int buttonCode) {
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

    public boolean getButtonDown(int buttonCode) {
        if (!hasGamepad)
            return false;

        boolean isDown = (getButton(buttonCode) && !buttons[buttonCode]);
        return isDown;
    }

    public boolean getButtonUp(int buttonCode) {
        if (!hasGamepad)
            return false;

        boolean isUp = (getButton(buttonCode) && buttons[buttonCode]);
        return isUp;
    }

    public boolean getKey(int keyCode) {
        return glfwGetKey(windowId, keyCode) == 1;
    }

    public boolean getKeyDown(int keyCode) {
        boolean isDown = (getKey(keyCode) && !keys[keyCode]);
        if (hasGamepad) {
            if (isDown)
                usingGamepad = false;
        }
        return isDown;
    }

    public boolean getKeyUp(int keyCode) {
        boolean isUp = (!getKey(keyCode) && keys[keyCode]);
        if (hasGamepad) {
            if (isUp)
                usingGamepad = false;
        }
        return isUp;
    }

    public boolean isLeftTriggerPressed() {
        if (!hasGamepad)
            return false;
        boolean isPressed = getLeftTrigger() == 1;
        if (isPressed)
            usingGamepad = true;
        return isPressed;
    }

    public boolean getMouseButton(int mouseButton) {
        boolean isDown = glfwGetMouseButton(windowId, mouseButton) == 1;
        if (hasGamepad) {
            if (isDown)
                usingGamepad = false;
        }
        return isDown;
    }

    public boolean getMouseButtonDown(int mouseButton) {
        boolean isDown = (getMouseButton(mouseButton) && !mouseButtons[mouseButton]);
        if (hasGamepad) {
            if (isDown)
                usingGamepad = false;
        }
        return isDown;
    }

    public boolean getMouseButtonUp(int mouseButton) {
        boolean isUp = (!getMouseButton(mouseButton) && mouseButtons[mouseButton]);
        if (hasGamepad) {
            if (isUp)
                usingGamepad = false;
        }
        return isUp;
    }

    public boolean isRightTriggerPressed() {
        if (!hasGamepad)
            return false;
        boolean isPressed = getRightTrigger() == 1;
        if (isPressed)
            usingGamepad = true;
        return isPressed;
    }

    public boolean noButtonPressed() {
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

    public boolean noKeyPressed() {
        boolean r = true;
        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            if (keys[i]) {
                r = false;
            }
        }
        return r;
    }

    public List<String> listenInputs() {
        List<String> inputs = new ArrayList<>();

        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            if (getKey(i)) {
                inputs.add("KEY: " + i);
            }
        }
        for (int i = 0; i < 5; i++) {
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

    public void update() {
        this.hasGamepad = glfwGetJoystickName(GLFW_JOYSTICK_1) != null;
        if (this.window.getLastMouseLocation().x != this.window.getMouseLocation().x
                || this.window.getLastMouseLocation().y != this.window.getMouseLocation().y) {
            this.usingGamepad = false;
        }
        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            keys[i] = getKey(i);
            if (keys[i]) {
                lastKey = i;
                usingGamepad = false;
            }
        }
        for (int i = 0; i < 2; i++) {
            mouseButtons[i] = getMouseButton(i);
        }

        if (hasGamepad) {
            if (buttons == null) {
                this.buttons = new boolean[GLFW_GAMEPAD_BUTTON_LAST];
                for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
                    buttons[i] = false;
                }
            }
            for (int i = 0; i < GLFW_GAMEPAD_BUTTON_LAST; i++) {
                buttons[i] = getButton(i);
                if (buttons[i]) {
                    usingGamepad = true;
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

    public boolean isUsingGamepad() {
        return usingGamepad;
    }

    public boolean hasGamepad() {
        return hasGamepad;
    }

}