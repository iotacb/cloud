package de.kostari.cloud.utilities.color;

import java.util.HashMap;
import java.util.Map;

public class CGradient {

    private Map<Float, CColor> colors = new HashMap<>();

    public void addColor(CColor color, float position) {
        colors.put(position, color);
    }

    public CColor getColorByPosition(float position) {
        for (Map.Entry<Float, CColor> entry : colors.entrySet()) {
            if (entry.getKey() > position) {
                return entry.getValue();
            }
        }
        return null;
    }

}
