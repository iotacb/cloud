package de.kostari.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.render.text.Fonts;
import de.kostari.demo.points.moveable.Oil;
import de.kostari.demo.points.moveable.Sand;
import de.kostari.demo.points.moveable.Water;
import de.kostari.demo.points.solids.Wood;

public class SandScene extends Scene {

    private final int CELL_SIZE = 4;
    private final int CHUNK_SIZE = 64;
    private final int SIMULATION_SPEED = 1;

    private int gridX;
    private int gridY;

    private Point[][] grid;

    private int[][] chunks;

    private boolean showGrid = false;

    private int brushSize = 1;
    private PointType brushType = PointType.SAND;

    private boolean isSpout = false;

    public SandScene(Window window) {
        super(window);

        this.gridX = (window.getWidth() / CELL_SIZE) + 1;
        this.gridY = window.getHeight() / CELL_SIZE;

        this.grid = new Point[gridX][gridY];
        this.chunks = new int[window.getWidth() / CHUNK_SIZE][window.getHeight() / CHUNK_SIZE];
    }

    @Override
    public void draw(float delta) {
        for (int x = 0; x < gridX; x++) {
            for (int y = 0; y < gridY; y++) {
                if (grid[x][y] != null) {
                    grid[x][y].draw(delta);
                }
            }
        }
        if (showGrid) {
            // for (int x = 0; x < gridX; x++) {
            // for (int y = 0; y < gridY; y++) {
            // Render.line(0, y * CELL_SIZE, getWindow().getWidth(), y * CELL_SIZE, .5F,
            // CColor.WHITE);
            // }
            // Render.line(x * CELL_SIZE, 0, x * CELL_SIZE, getWindow().getHeight(), .5F,
            // CColor.WHITE);
            // }
            for (int x = 0; x < chunks.length; x++) {
                for (int y = 0; y < chunks[0].length; y++) {
                    Render.line(0, y * CHUNK_SIZE, getWindow().getWidth(), y * CHUNK_SIZE, .5F,
                            CColor.GREEN);
                }
                Render.line(x * CHUNK_SIZE, 0, x * CHUNK_SIZE, getWindow().getHeight(), .5F,
                        CColor.GREEN);
            }
        }

        super.draw(delta);
        Render.rect(Input.getMousePosition(), new Vec(brushSize * CELL_SIZE),
                CColor.WHITE.setAlpha(200));
        Fonts.sans32.drawText("FPS: " + getWindow().getFPS(),
                8, 2);
        Fonts.sans32.drawText("Points: " + getPoints(),
                8, 30);
        Fonts.sans32.drawText("Type: " + brushType.name(),
                8, 60);
        Fonts.sans32.drawText("Is Spout: " + isSpout,
                8, 90);
    }

    @Override
    public void update(float delta) {
        if (Input.mouseButtonStrength(0) == 1 || Input.mouseButtonPressed(2)) {
            if (isSpout && Input.mouseButtonPressed(2)) {
                int x = (Input.getMouseX() / CELL_SIZE);
                int y = (Input.getMouseY() / CELL_SIZE);
                if (cellEmpty(x, y)) {
                    addPoint(x, y, new Spout(getWindow(), brushType, brushSize, CELL_SIZE, x, y, grid));
                }
                return;
            }
            for (int j = -brushSize / 2; j <= brushSize / 2; j++) {
                for (int i = -brushSize / 2; i <= brushSize / 2; i++) {

                    int x = (Input.getMouseX() / CELL_SIZE) + i;
                    int y = (Input.getMouseY() / CELL_SIZE) + j;

                    if (cellEmpty(x, y)) {
                        addPoint(x, y, getPointByType(x, y, grid, brushType));
                    }
                }
            }
            // int[] pos = getPosInBrush();
            // if (cellEmpty(pos[0], pos[1])) {
            // if (!spawnWater) {
            // addPoint(pos[0], pos[1], new Sand(getWindow(), CELL_SIZE, pos[0], pos[1],
            // grid));
            // } else {
            // addPoint(pos[0], pos[1], new Water(getWindow(), CELL_SIZE, pos[0], pos[1],
            // grid));
            // }
            // }
        }

        if (Input.mouseButtonStrength(1) == 1) {
            for (int j = -brushSize / 2; j <= brushSize / 2; j++) {
                for (int i = -brushSize / 2; i <= brushSize / 2; i++) {

                    int x = (Input.getMouseX() / CELL_SIZE) + i;
                    int y = (Input.getMouseY() / CELL_SIZE) + j;

                    if (!cellEmpty(x, y)) {
                        removePoint(x, y);
                    }
                }
            }
        }

        if (Input.keyPressed(Keys.KEY_F1)) {
            brushType = PointType.SAND;
        } else if (Input.keyPressed(Keys.KEY_F2)) {
            brushType = PointType.WATER;
        } else if (Input.keyPressed(Keys.KEY_F3)) {
            brushType = PointType.WOOD;
        } else if (Input.keyPressed(Keys.KEY_F4)) {
            brushType = PointType.OIL;
        } else if (Input.keyPressed(Keys.KEY_F12)) {
            isSpout = !isSpout;
        }

        // if (Input.keyPressed(Keys.KEY_F1)) {
        // showGrid = !showGrid;
        // }

        if (Input.getScrollY() != 0) {
            brushSize += Input.getScrollY();
        }

        if (Input.keyPressed(Keys.KEY_R)) {
            this.grid = new Point[gridX][gridY];
        }

        for (int i = 0; i < SIMULATION_SPEED; i++) {
            stepSand(delta);
        }
        super.update(delta);
    }

    private void stepSand(float delta) {
        for (int y = gridY - 1; y >= 0; y--) {
            List<Integer> shuffeledX = new ArrayList<>(gridX);
            for (int i = 0; i < gridX; i++) {
                shuffeledX.add(i);
            }
            Collections.shuffle(shuffeledX);

            for (int x : shuffeledX) {
                Point point = grid[x][y];
                if (point == null || !point.updatePoint)
                    continue;

                if (point.type == PointType.SPOUT) {
                    Spout spout = (Spout) point;
                    for (int j = -spout.size / 2; j <= spout.size / 2; j++) {
                        for (int i = -spout.size / 2; i <= spout.size / 2; i++) {
                            movePoint(
                                    getPointByType(x + i, y + j + (spout.size > 1 ? spout.size / 2 : 1), grid,
                                            spout.typeToSpawn),
                                    point.x,
                                    point.y);
                        }
                    }
                }

                int[] positions = point.fall(x, y, grid);
                movePoint(point, positions[0], positions[1]);
            }
        }
    }

    private void addPoint(int x, int y, Point point) {
        if (grid[x][y] == null) {
            grid[x][y] = point;
        }
    }

    private boolean cellEmpty(int x, int y) {
        return x >= 0 && x < gridX && y >= 0 && y < gridY && grid[x][y] == null;
    }

    private void movePoint(Point point, int x, int y) {
        if (x < 0 || x > gridX || y < 0 || y >= gridY) {
            grid[point.x][point.y] = null;
            return;
        }

        Point oldPoint = null;
        if (grid[x][y] != null) {
            if (grid[x][y].canBeMoved) {
                oldPoint = grid[x][y];
                oldPoint.x = point.x;
                oldPoint.y = point.y;
            }
        }
        grid[point.x][point.y] = null;
        point.x = x;
        point.y = y;
        grid[x][y] = point;
        if (oldPoint != null) {
            grid[oldPoint.x][oldPoint.y] = oldPoint;
        }
    }

    private void removePoint(int x, int y) {
        if (x < 0 || x >= gridX || y < 0 || y >= gridY)
            return;
        grid[x][y] = null;
    }

    private int getPoints() {
        int points = 0;
        for (int x = 0; x < gridX; x++) {
            for (int y = 0; y < gridY; y++) {
                if (grid[x][y] != null) {
                    points++;
                }
            }
        }
        return points;
    }

    public PointType getTypeOfCell(int x, int y) {
        if (x < 0 || x >= gridX || y < 0 || y >= gridY)
            return null;
        if (grid[x][y] == null)
            return null;
        return grid[x][y].type;
    }

    private Point getPointByType(int x, int y, Point[][] grid, PointType type) {
        switch (type) {
            case SAND:
                return new Sand(getWindow(), CELL_SIZE, x, y, grid);
            case WATER:
                return new Water(getWindow(), CELL_SIZE, x, y, grid);
            case WOOD:
                return new Wood(getWindow(), CELL_SIZE, x, y, grid);
            case OIL:
                return new Oil(getWindow(), CELL_SIZE, x, y, grid);
            case SPOUT:
                return new Spout(getWindow(), brushType, brushSize, CELL_SIZE, x, y, grid);
            default:
                return new Sand(getWindow(), CELL_SIZE, x, y, grid);
        }
    }

}
