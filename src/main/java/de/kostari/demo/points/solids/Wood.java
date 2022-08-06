package de.kostari.demo.points.solids;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.demo.Point;
import de.kostari.demo.PointType;

public class Wood extends Point {

    public Wood(Window window, int size, int x, int y, GameObject[][] grid) {
        super(window, PointType.WOOD, size, x, y, grid);
        canBeMoved = false;
    }

    @Override
    public int[] fall(int x, int y, GameObject[][] grid) {
        return new int[] { x, y };
    }

}
