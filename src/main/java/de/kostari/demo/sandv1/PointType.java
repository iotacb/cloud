package de.kostari.demo.sandv1;

import de.kostari.cloud.utilities.color.CColor;

public enum PointType {

    EMPTY(CColor.BLACK),
    SAND(new CColor(235, 200, 105)),
    WATER(new CColor(50, 50, 255)),
    BUBBLE(new CColor(120, 180, 255)),
    WOOD(CColor.BROWN),
    OIL(CColor.OLIVE),
    SPOUT(CColor.PINK);

    private CColor color;

    PointType(CColor color) {
        this.color = color;
    }

    public CColor getColor() {
        return color;
    }

}
