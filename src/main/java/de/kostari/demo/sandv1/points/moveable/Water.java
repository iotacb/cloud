package de.kostari.demo.sandv1.points.moveable;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.demo.sandv1.Point;
import de.kostari.demo.sandv1.PointType;

public class Water extends Point {

    private final CColor WATER_FLOW = new CColor(0, 100, 255);
    private final CColor WATER_SHORE = new CColor(120, 180, 255);

    public Water(Window window, int size, int x, int y, GameObject[][] grid) {
        super(window, PointType.WATER, size, x, y, grid);
        setResistance(.6f);
    }

    @Override
    public int[] fall(int x, int y, GameObject[][] grid) {

        boolean up = cellEmpty(x, y - 1, grid);
        boolean down = cellEmpty(x, y + 1, grid);
        boolean left = cellEmpty(x - 1, y, grid);
        boolean right = cellEmpty(x + 1, y, grid);

        if (!down) {
            if (getTypeOfCell(x, y + 1) == PointType.OIL) {
                down = true;
            }
        }

        if (!left) {
            if (getTypeOfCell(x - 1, y) == PointType.OIL) {
                left = true;
            }
        }

        if (!right) {
            if (getTypeOfCell(x + 1, y) == PointType.OIL) {
                right = true;
            }
        }

        int distanceLeft = getGridX() - (getGridX() - x);
        int distanceRight = getGridX() - x;

        int emptyLeft = getEmptyCellsDistanceX(x - 1, y, -distanceLeft, grid);
        int emptyRight = getEmptyCellsDistanceX(x + 1, y, distanceRight, grid);

        if (left && right) {
            if (emptyLeft == emptyRight) {
                if (emptyLeft > emptyRight) {
                    left = true;
                    right = false;
                } else {
                    right = true;
                    left = false;
                }
            } else {
                boolean random = Math.random() < .5F;
                left = random ? true : false;
                right = random ? false : true;
            }
        }

        if (up) {
            if (getLifetime() % 2 == 0) {
                setColor(WATER_SHORE.clone().randomShade(-10, 10));
            }
        } else {
            if (getLifetime() % CMath.fromRange(10, 20) == 0) {
                setColor(WATER_FLOW.clone().randomShade(-2, 4));
            }
        }

        updateLastFrame(x, y);

        if (down)
            return new int[] { x, y + 1 };
        else if (left)
            return new int[] { x - 1, y };
        else if (right)
            return new int[] { x + 1, y };

        return new int[] { x, y };
    }
}
