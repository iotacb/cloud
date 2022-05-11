package de.kostari.cloud.utilities.color;

import java.util.HashMap;
import java.util.Map;

public class CGradient {

    private Map<Float, CColor> colors = new HashMap<>();

    public void addColor(CColor color, float position) {
        colors.put(position, color);
    }

    public CColor getColor(float position) {
        return colors.get(position);
    }

    private CColor getColorByPosition(float position) {
        for (float f : colors.keySet()) {
            if (f > position) {
                return colors.get(f);
            }
        }
        return null;
    }

}
