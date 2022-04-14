package de.kostari.cloud.utilities.color;

import de.kostari.cloud.utilities.math.CMath;

public class CColor {

    private int r;
    private int g;
    private int b;
    private int alpha;

    public static CColor WHITE = new CColor(255, 255, 255);
    public static CColor BLACK = new CColor(0, 0, 0);
    public static CColor RED = new CColor(255, 0, 0);
    public static CColor GREEN = new CColor(0, 255, 0);
    public static CColor BLUE = new CColor(0, 0, 255);
    public static CColor YELLOW = new CColor(255, 255, 0);
    public static CColor CYAN = new CColor(0, 255, 255);
    public static CColor MAGENTA = new CColor(255, 0, 255);
    public static CColor GRAY = new CColor(128, 128, 128);
    public static CColor ORANGE = new CColor(255, 165, 0);
    public static CColor PINK = new CColor(255, 192, 203);
    public static CColor PURPLE = new CColor(128, 0, 128);
    public static CColor LIME = new CColor(0, 255, 0);
    public static CColor NAVY = new CColor(0, 0, 128);
    public static CColor OLIVE = new CColor(128, 128, 0);
    public static CColor TEAL = new CColor(0, 128, 128);
    public static CColor MAROON = new CColor(128, 0, 0);
    public static CColor SILVER = new CColor(192, 192, 192);
    public static CColor OLIVE_DRAB = new CColor(107, 142, 35);
    public static CColor ROYAL_BLUE = new CColor(65, 105, 225);
    public static CColor LIGHT_GRAY = new CColor(211, 211, 211);
    public static CColor DARK_GRAY = new CColor(169, 169, 169);
    public static CColor NAVY_BLUE = new CColor(0, 0, 128);
    public static CColor SLATE_BLUE = new CColor(106, 90, 205);
    public static CColor SLATE_GRAY = new CColor(112, 128, 144);
    public static CColor SKY_BLUE = new CColor(135, 206, 235);
    public static CColor LIGHT_BLUE = new CColor(173, 216, 230);
    public static CColor TRANSPARENT = new CColor(0, 0, 0, 0);

    public CColor(int r, int g, int b, int alpha) {
        init(r, g, b, alpha);
    }

    public CColor(int r, int g, int b) {
        init(r, g, b, 255);
    }

    public CColor(float r, float g, float b, float alpha) {
        init((int) r, (int) g, (int) b, (int) alpha);
    }

    public CColor(float r, float g, float b) {
        init((int) r, (int) g, (int) b, 255);
    }

    public CColor(int hex) {
        CColor rgb = hexToRgb(hex);
        init(rgb.r, rgb.g, rgb.b, 255);
    }

    private void init(int r, int g, int b, int alpha) {
        this.r = CMath.clamp(r, 0, 255);
        this.g = CMath.clamp(g, 0, 255);
        this.b = CMath.clamp(b, 0, 255);
        this.alpha = CMath.clamp(alpha, 0, 255);
    }

    public static CColor hexToRgb(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        int a = (hex & 0xFF000000) >> 24;
        return new CColor(r, g, b, a);
    }

    public static CColor random() {
        return new CColor((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    public int getAlpha() {
        return alpha;
    }

    public float getRed01() {
        return r / 255.0F;
    }

    public float getGreen01() {
        return g / 255.0F;
    }

    public float getBlue01() {
        return b / 255.0F;
    }

    public float getAlpha01() {
        return alpha / 255.0F;
    }

    private float redTransition;
    private float greenTransition;
    private float blueTransition;
    private float alphaTransition;

    private float transitionProgress;

    public CColor transition(CColor color, boolean doTransition, float delta) {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        int a = getAlpha();

        int r2 = color.getRed();
        int g2 = color.getGreen();
        int b2 = color.getBlue();
        int a2 = color.getAlpha();

        float time = 1;

        if (doTransition) {
            if (transitionProgress < time) {
                transitionProgress += delta;
            } else {
                transitionProgress = time;
            }

            if (r > r2) {
                redTransition = r - CMath.quad(transitionProgress, r2, r, time);
            } else {
                redTransition = CMath.quad(transitionProgress, r, r2, time);
            }

            if (g > g2) {
                greenTransition = g - CMath.quad(transitionProgress, g2, g, time);
            } else {
                greenTransition = CMath.quad(transitionProgress, g, g2, time);
            }

            if (b > b2) {
                blueTransition = b - CMath.quad(transitionProgress, b2, b, time);
            } else {
                blueTransition = CMath.quad(transitionProgress, b, b2, time);
            }

            if (a > a2) {
                alphaTransition = a - CMath.quad(transitionProgress, a2, a, time);
            } else {
                alphaTransition = CMath.quad(transitionProgress, a, a2, time);
            }
        } else {
            if (transitionProgress > 0) {
                transitionProgress -= delta;
            } else {
                transitionProgress = 0;
            }

            if (r2 < r) {
                redTransition = CMath.quad(1 - transitionProgress, r2, r, time);
            } else {
                redTransition = r2 - CMath.quad(1 - transitionProgress, r, r2, time);
            }

            if (g2 < g) {
                greenTransition = CMath.quad(1 - transitionProgress, g2, g, time);
            } else {
                greenTransition = g2 - CMath.quad(1 - transitionProgress, g, g2, time);
            }

            if (b2 < b) {
                blueTransition = CMath.quad(1 - transitionProgress, b2, b, time);
            } else {
                blueTransition = b2 - CMath.quad(1 - transitionProgress, b, b2, time);
            }

            if (a2 < a) {
                alphaTransition = CMath.quad(1 - transitionProgress, a2, a, time);
            } else {
                alphaTransition = a2 - CMath.quad(1 - transitionProgress, a, a2, time);
            }

        }

        return new CColor(redTransition, greenTransition, blueTransition, alphaTransition);
    }

    @Override
    public String toString() {
        return String.format("{r: %s, g: %s, b: %s, a: %s}", r, g, b, alpha);
    }

}
