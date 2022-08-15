package de.kostari.demo.sandv1;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.kostari.cloud.core.scene.Camera;
import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.files.asset.assets.Image;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.render.text.Fonts;
import de.kostari.demo.sandv1.points.moveable.Oil;
import de.kostari.demo.sandv1.points.moveable.Sand;
import de.kostari.demo.sandv1.points.moveable.Water;
import de.kostari.demo.sandv1.points.solids.Wood;

public class SandScene extends Scene {

    private final int CELL_SIZE = 2;
    private final int SIMULATION_SPEED = 1;

    private int gridX;
    private int gridY;

    private Point[][] grid;

    private int brushSize = 1;
    private PointType brushType = PointType.SAND;

    private boolean isSpout = false;

    private Camera cam;

    public SandScene(Window window) {
        super(window);

        this.gridX = (window.getWidth() / CELL_SIZE) + 1;
        this.gridY = window.getHeight() / CELL_SIZE;

        this.grid = new Point[gridX][gridY];

        this.cam = new Camera();
    }

    @Override
    public void draw(float delta) {
        Render.beginCam(cam);
        for (int x = 0; x < gridX; x++) {
            for (int y = 0; y < gridY; y++) {
                if (grid[x][y] != null) {
                    grid[x][y].draw(delta);
                }
            }
        }
        Render.endCam();

        Render.rect(Input.getMousePosition(), new Vec(brushSize * CELL_SIZE),
                CColor.WHITE.setAlpha(200));
        super.draw(delta);
        Fonts.sans32.drawTextShadow("FPS: " + getWindow().getFPS(),
                8, 2);
        Fonts.sans32.drawTextShadow("Points: " + getPoints(),
                8, 30);
        Fonts.sans32.drawTextShadow("Type: " + brushType.name(),
                8, 60);
        Fonts.sans32.drawTextShadow("Is Spout: " + isSpout,
                8, 90);
        String isBatched = "Render type: " + getWindow().getRenderType().name();
        Fonts.sans32.drawTextShadow(isBatched, getWindow().getWidth() - Fonts.sans32.getWidth(isBatched) - 8, 2);
    }

    @Override
    public void update(float delta) {

        cam.zoom += Input.getScrollY() * 0.01f;
        cam.offset = new Vec(0, 0);

        if (cam.zoom > 3)
            cam.zoom = 3;
        if (cam.zoom < 0.25f)
            cam.zoom = 0.25f;

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

        if (Input.getScrollY() != 0) {
            brushSize += Input.getScrollY();
        }

        if (Input.keyPressed(Keys.KEY_R)) {
            // this.grid = new Point[gridX][gridY];

            // getWindow().setPixel(10, 10, CColor.random());

            // ByteBuffer pixels = getWindow().loadPixels();

            // int pX = 0;
            // int pY = 0;

            // int i = (int) (pX * getWindow().getWidth() * 4 + pX * 4);
            // int r = pixels.get(i + 0) & 0xFF;
            // int g = pixels.get(i + 1) & 0xFF;
            // int b = pixels.get(i + 2) & 0xFF;
            // int a = pixels.get(i + 3) & 0xFF;

            // System.out.println(r + " " + g + " " + b + " " + a);
        }

        for (int i = 0; i < SIMULATION_SPEED; i++) {
            stepSand(delta);
        }

        super.update(delta);
    }

    @Override
    public void onFileDrop(String[] files) {
        String file = files[0];
        if (!(file.endsWith(".png") || file.endsWith(".jpg"))) {
            return;
        }

        Image image = new Image(file);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                PointType type = getNearestMatchingPoint(image.getPixelColor(x, y));
                Point point = getPointByType(x, y, grid, type);
                addPoint(x, y, point);
            }
        }

        super.onFileDrop(files);
    }

    private void stepSand(float delta) {
        ByteBuffer pixels = getWindow().newPixels();
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
                getWindow().changePixel(pixels, x, y, point.color);
                movePoint(point, positions[0], positions[1]);
            }
        }
        getWindow().updatePixels(pixels);
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

    private PointType getNearestMatchingPoint(CColor color) {
        float heighestMatch = 0;
        PointType matchingType = PointType.EMPTY;
        for (PointType type : PointType.values()) {
            if (type == PointType.SPOUT) {
                continue;
            }

            float r = color.getRed01() - type.getColor().getRed01();
            float g = color.getGreen01() - type.getColor().getGreen01();
            float b = color.getBlue01() - type.getColor().getBlue01();
            float match = (float) Math.sqrt(r * r + g * g + b * b);
            if (match > heighestMatch) {
                heighestMatch = match;
                matchingType = type;
            }
        }
        return matchingType;
    }

}
