package de.kostari.demo.sandv1.points.moveable;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.demo.sandv1.Point;
import de.kostari.demo.sandv1.PointType;

public class Bubble extends Point {

    public Bubble(Window window, int size, int x, int y, GameObject[][] grid) {
        super(window, PointType.BUBBLE, size, x, y, grid);
    }

    @Override
    public int[] fall(int x, int y, GameObject[][] grid) {
        boolean up = cellEmpty(x, y - 1, grid);
        boolean left = cellEmpty(x - 1, y + 1, grid);
        boolean right = cellEmpty(x + 1, y + 1, grid);

        updateLastFrame(x, y);

        if (up)
            return new int[] { x, y - 1 };
        else if (left && Math.random() < .1f)
            return new int[] { x - 1, y + 1 };
        else if (right && Math.random() < .1f)
            return new int[] { x + 1, y + 1 };
        else
            return new int[] { x, y };
    }

}
