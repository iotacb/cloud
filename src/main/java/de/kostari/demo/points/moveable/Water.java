package de.kostari.demo.points.moveable;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.demo.Point;
import de.kostari.demo.PointType;

public class Water extends Point {

    public Water(Window window, int size, int x, int y, GameObject[][] grid) {
        super(window, PointType.WATER, size, x, y, grid);
    }

    @Override
    public int[] fall(int x, int y, GameObject[][] grid) {
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
