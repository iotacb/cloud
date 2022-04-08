package de.kostari.cloud.core.gui.transition;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.gui.transition.motor.TransitionMotor;

public class TransitionManager {

    private List<TransitionMotor> motors;

    public float delay;

    public TransitionManager(float delay) {
        this.delay = delay;
        this.motors = new ArrayList<>();
    }

    public TransitionManager() {
        this.motors = new ArrayList<>();
    }

    public void update(float delta) {
        for (TransitionMotor motor : motors) {
            motor.updateDelay(delta);
            if (motor.getDelay() >= delay) {
                motor.update(delta);
                motor.setDelay(0);
            }
        }
    }

    public TransitionManager addMotor(TransitionMotor motor) {
        this.motors.add(motor);
        return this;
    }

    public TransitionManager addMotors(TransitionMotor... motors) {
        for (TransitionMotor m : motors) {
            addMotor(m);
        }
        return this;
    }

    public float getDelay() {
        return delay;
    }

    public TransitionManager setDelay(float delay) {
        this.delay = delay;
        return this;
    }

    public TransitionManager reverseMotors() {
        for (TransitionMotor motor : motors) {
            motor.reverse();
        }
        return this;
    }

}
