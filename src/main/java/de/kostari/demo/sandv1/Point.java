package de.kostari.demo.sandv1;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;
import lombok.Getter;
import lombok.Setter;

public class Point extends GameObject {

    public CColor color;
    public PointType type;
    public int size;

    public int x;
    public int y;

    public int lastX;
    public int lastY;

    private GameObject[][] grid;

    @Getter
    @Setter
    private float resistance;

    private int gridX;
    private int gridY;

    @Getter
    private int lifetime;

    public boolean updatePoint = true;

    public boolean canBeMoved = true;

    public Point(Window window, PointType type, int size, int x, int y, GameObject[][] grid) {
        super(window);
        this.color = type.getColor();
        this.type = type;
        this.size = size;
        this.x = x;
        this.y = y;

        this.grid = grid;
        this.gridX = grid.length;
        this.gridY = grid[0].length;
    }

    @Override
    public void draw(float delta) {
        // Render.rect(new Vec(x * size + size / 2, y * size + size / 2), new Vec(size),
        // color);
        lifetime++;
        super.draw(delta);
    }

    public int[] fall(int x, int y, GameObject[][] grid) {
        return new int[] { x, y };
    }

    public void updateLastFrame(int x, int y) {
        this.lastX = x;
        this.lastY = y;
    }

    public CColor getColor() {
        return color;
    }

    public PointType getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public void setColor(CColor color) {
        this.color = color;
    }

    public GameObject[][] getGrid() {
        return grid;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public boolean cellEmpty(int x, int y, GameObject[][] grid) {
        return x >= 0 && x < gridX && y > 0 && y < gridY && grid[x][y] == null;
    }

    public int getEmptyCellsDistanceX(int x, int y, int distance, GameObject[][] grid) {
        int emptyCells = 0;
        for (int i = 0; i < Math.abs(distance); i++) {
            if (cellEmpty(x + (distance < 0 ? -i : i), y, grid)) {
                emptyCells++;
            }
        }
        return emptyCells;
    }

    public int getEmptyCellsDistanceY(int x, int y, int distance, GameObject[][] grid) {
        int emptyCells = 0;
        for (int i = 0; i < distance; i++) {
            if (cellEmpty(x, y + i, grid)) {
                emptyCells++;
            }
        }
        return emptyCells;
    }

    public Point getCell(int x, int y) {
        if (x < 0 || x >= gridX || y < 0 || y >= gridY)
            return null;
        return (Point) grid[x][y];
    }

    public float getCellResistance(int x, int y) {
        if (x < 0 || x >= gridX || y < 0 || y >= gridY) {
            return 1;
        }
        Point p = getCell(x, y);
        if (p == null)
            return 0;
        return p.getResistance();
    }

    public PointType getTypeOfCell(int x, int y) {
        Point p = getCell(x, y);
        return p == null ? PointType.EMPTY : p.getType();
    }

    public boolean canBeMoved(int x, int y) {
        Point p = getCell(x, y);
        if (p == null)
            return false;
        return p.canBeMoved;
    }

}
