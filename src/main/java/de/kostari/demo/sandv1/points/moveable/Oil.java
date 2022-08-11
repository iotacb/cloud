package de.kostari.demo.sandv1.points.moveable;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.demo.sandv1.Point;
import de.kostari.demo.sandv1.PointType;

public class Oil extends Point {

    public Oil(Window window, int size, int x, int y, GameObject[][] grid) {
        super(window, PointType.OIL, size, x, y, grid);
        setResistance(.8f);
    }

    @Override
    public int[] fall(int x, int y, GameObject[][] grid) {
        boolean down = cellEmpty(x, y + 1, grid);
        boolean left = cellEmpty(x - 1, y, grid);
        boolean right = cellEmpty(x + 1, y, grid);

        if (down) {
            if (getTypeOfCell(x, y + 1) == PointType.WATER) {
                down = false;
            }
        }

        if (left) {
            if (getTypeOfCell(x - 1, y) == PointType.WATER) {
                left = false;
            }
        }

        if (right) {
            if (getTypeOfCell(x + 1, y) == PointType.WATER) {
                right = false;
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

        this.lastX = x;
        this.lastY = y;

        if (down)
            return new int[] { x, y + 1 };
        else if (left)
            return new int[] { x - 1, y };
        else if (right)
            return new int[] { x + 1, y };

        return new int[] { x, y };
    }
}
