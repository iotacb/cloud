package de.kostari.cloud.core.gui.transition.motor;

import de.kostari.cloud.core.gui.transition.TransitionEasing;

public abstract class TransitionMotor {

    private float delay;

    private boolean isTransitioning;
    private boolean reversed;

    private TransitionEasing easing = TransitionEasing.LINEAR;

    public void updateDelay(float delta) {
        this.delay += delta;
    }

    public abstract void update(float delta);

    public abstract void reverse();

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public boolean isTransitioning() {
        return isTransitioning;
    }

    public void setTransitioning(boolean isTransitioning) {
        this.isTransitioning = isTransitioning;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public boolean isReversed() {
        return reversed;
    }

    public TransitionEasing getEasing() {
        return easing;
    }

    public TransitionMotor setEasing(TransitionEasing easing) {
        this.easing = easing;
        return this;
    }

}
