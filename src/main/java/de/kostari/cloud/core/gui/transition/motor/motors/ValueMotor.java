package de.kostari.cloud.core.gui.transition.motor.motors;

import de.kostari.cloud.core.gui.transition.TransitionEasing;
import de.kostari.cloud.core.gui.transition.TransitionEasings;
import de.kostari.cloud.core.gui.transition.motor.TransitionMotor;

public class ValueMotor extends TransitionMotor {

    private float startValue;
    private float endValue;
    private float progress;

    private float time;

    private float currentValue;

    private boolean reverse;

    public ValueMotor(float start, float end, float time, TransitionEasing easing) {
        this.startValue = start;
        this.endValue = end;
        this.currentValue = start;
        this.time = time;
        setEasing(easing);
    }

    public ValueMotor(float start, float end, float time) {
        this.startValue = start;
        this.endValue = end;
        this.currentValue = start;
        this.time = time;
    }

    @Override
    public void update(float delta) {
        if (progress < time) {
            progress += delta;
        } else {
            progress = time;
        }

        switch (getEasing()) {
            case ELASTIC:
                if (startValue > endValue) {
                    currentValue = startValue - TransitionEasings.elastic(progress, startValue, endValue, time);
                } else {
                    currentValue = TransitionEasings.elastic(progress, startValue, endValue, time);
                }
                break;
            case LINEAR:
                if (startValue > endValue) {
                    currentValue = startValue - TransitionEasings.linear(progress, endValue, startValue, time);
                } else {
                    currentValue = TransitionEasings.linear(progress, startValue, endValue, time);
                }
                break;
            case QUADRATIC:
                if (startValue > endValue) {
                    currentValue = startValue - TransitionEasings.quad(progress, startValue, endValue, time);
                } else {
                    currentValue = TransitionEasings.quad(progress, startValue, endValue, time);
                }
                break;
        }
    }

    @Override
    public void reverse() {
        this.reverse = !reverse;
        this.progress = 0;
        float tmp = startValue;
        startValue = endValue;
        endValue = tmp;

    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setStartValue(float startValue) {
        this.startValue = startValue;
        this.progress = 0;
    }

    public void setEndValue(float endValue) {
        this.progress = 0;
        this.endValue = endValue;
    }

    public float getStartValue() {
        return startValue;
    }

    public float getEndValue() {
        return endValue;
    }

}
