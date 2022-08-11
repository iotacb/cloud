package de.kostari.demo.sandv1.points.solids;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.demo.sandv1.Point;
import de.kostari.demo.sandv1.PointType;

public class Wood extends Point {

    public Wood(Window window, int size, int x, int y, GameObject[][] grid) {
        super(window, PointType.WOOD, size, x, y, grid);
        setResistance(1);
    }

    @Override
    public int[] fall(int x, int y, GameObject[][] grid) {
        return new int[] { x, y };
    }

}
