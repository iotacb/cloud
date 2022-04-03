package de.kostari.cloud.utilities.color;

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

    public CColor(int r, int g, int b, int alpha) {
        init(r, g, b, alpha);
    }

    public CColor(int r, int g, int b) {
        init(r, g, b, 255);
    }

    public CColor(int hex) {
        CColor rgb = hexToRgb(hex);
        init(rgb.r, rgb.g, rgb.b, 255);
    }

    private void init(int r, int g, int b, int alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
    }

    public static CColor hexToRgb(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        int a = (hex & 0xFF000000) >> 24;
        return new CColor(r, g, b, a);
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

    @Override
    public String toString() {
        return String.format("{r: %s, g: %s, b: %s, a: %s}", r, g, b, alpha);
    }

}
