package de.kostari.cloud.core.gui.transition;

public class TransitionEasings {

    public static float quad(float progress, float start, float end, float duration) {
        if ((progress /= duration / 2) < 1)
            return end / 2 * progress * progress + start;
        return -end / 2 * ((--progress) * (progress - 2) - 1) + start;
    }

    public static float elastic(float progress, float start, float end, float duration) {
        if (progress == 0)
            return start;
        if ((progress /= duration / 2) == 2)
            return start + end;
        float p = duration * (.3f * 1.5f);
        float a = end;
        float s = p / 4;
        if (progress < 1)
            return -.5f * (a * (float) Math.pow(2, 10 * (progress -= 1))
                    * (float) Math.sin((progress * duration - s) * (2 * (float) Math.PI) / p)) + start;
        return a * (float) Math.pow(2, -10 * (progress -= 1))
                * (float) Math.sin((progress * duration - s) * (2 * (float) Math.PI) / p) * .5f + end + start;
    }

    public static float linear(float progress, float start, float end, float duration) {
        return end * progress / duration + start;
    }

}
