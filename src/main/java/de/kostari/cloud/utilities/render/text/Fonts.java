package de.kostari.cloud.utilities.render.text;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import static java.awt.Font.SANS_SERIF;
import static java.awt.Font.PLAIN;
import static java.awt.Font.TRUETYPE_FONT;

public class Fonts {

    public static CFont sans16 = new CFont(new Font(SANS_SERIF, PLAIN, 16), true);
    public static CFont sans32 = new CFont(new Font(SANS_SERIF, PLAIN, 32), true);
    public static CFont sans64 = new CFont(new Font(SANS_SERIF, PLAIN, 64), true);
    public static CFont sans128 = new CFont(new Font(SANS_SERIF, PLAIN, 128), true);
    public static CFont sans256 = new CFont(new Font(SANS_SERIF, PLAIN, 256), true);

    public static CFont getFont(String path, int size) {
        try {
            return new CFont(Font.createFont(TRUETYPE_FONT, new File(path)).deriveFont(PLAIN, size), true);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
