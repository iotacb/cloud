package de.kostari.demo;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;

public class Spout extends Point {

    public PointType typeToSpawn;
    public int size;

    public Spout(Window window, PointType type, int brushSize, int size, int x, int y, GameObject[][] grid) {
        super(window, PointType.SPOUT, size, x, y, grid);
        this.typeToSpawn = type;
        this.size = brushSize;
    }

}
