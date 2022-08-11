package de.kostari.demo.sandv1.points.moveable;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.demo.sandv1.Point;
import de.kostari.demo.sandv1.PointType;

public class Sand extends Point {

    public Sand(Window window, int size, int x, int y, GameObject[][] grid) {
        super(window, PointType.SAND, size, x, y, grid);
    }

    @Override
    public int[] fall(int x, int y, GameObject[][] grid) {
        boolean down = cellEmpty(x, y + 1, grid);
        boolean left = cellEmpty(x - 1, y + 1, grid);
        boolean right = cellEmpty(x + 1, y + 1, grid);

        if (!down) {
            if (canBeMoved(x, y + 1) && Math.random() > .5F) {
                down = true;
            }
        }
        if (!left) {
            if (canBeMoved(x - 1, y + 1) && Math.random() > .5F) {
                left = true;
            }
        }
        if (!right) {
            if (canBeMoved(x + 1, y + 1) && Math.random() > .5F) {
                right = true;
            }
        }

        if (left && right) {
            boolean random = Math.random() < .5F;
            left = random ? true : false;
            right = random ? false : true;
        }

        updateLastFrame(x, y);

        if (down)
            return new int[] { x, y + 1 };
        else if (left)
            return new int[] { x - 1, y + 1 };
        else if (right)
            return new int[] { x + 1, y + 1 };
        else
            return new int[] { x, y };
    }

}
