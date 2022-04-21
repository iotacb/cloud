package de.kostari.cloud.utilities.tileset;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.cloud.utilities.render.Render;

public class TileManager {

    private Window window;

    private List<Tile> tiles;

    private float tileWidth;
    private float tileHeight;

    private float tileStepX;
    private float tileStepY;

    private boolean gridEnabled;
    private boolean showGrid;

    public TileManager(Window window, float tileWidth, float tileHeight) {
        this.window = window;
        this.tiles = new ArrayList<>();

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.tileStepX = window.getWidth() / tileWidth;
        this.tileStepY = window.getHeight() / tileHeight;
    }

    public void addTile(Tile tile, float posX, float posY) {
        for (int y = 0; y < tileStepY; y++) {
            for (int x = 0; x < tileStepX; x++) {
                if (posX > x * tileWidth && posX < (x + 1) * tileWidth && posY > y * tileHeight
                        && posY < (y + 1) * tileHeight) {
                    Tile newTile = tile.clone();
                    posX = x * tileWidth;
                    posY = y * tileHeight;
                    newTile.transform.position.set(posX + newTile.getWidth() / 2, posY + newTile.getHeight() / 2);
                    tiles.add(newTile);
                }
            }
        }
        // newTile.transform.position.set(posX, posY);
        // tiles.add(newTile);
    }

    public void draw(float delta) {
        for (Tile tile : tiles) {
            Render.image(tile, tile.transform.position);
        }

        int posX = Input.getMouseX();
        int posY = Input.getMouseY();
        for (int y = 0; y < tileStepY; y++) {
            for (int x = 0; x < tileStepX; x++) {
                if (posX > x * tileWidth && posX < (x + 1) * tileWidth && posY > y * tileHeight
                        && posY < (y + 1) * tileHeight) {
                    if (tiles.size() > 0) {
                        posX = (int) (x * tileWidth);
                        posY = (int) (y * tileHeight);
                        Render.rect(posX + tileWidth / 2, posY + tileHeight / 2, tileWidth,
                                tileHeight, CColor.WHITE.setAlpha(100));
                    }
                }
            }
        }

        if (!gridEnabled)
            return;

        if (!showGrid)
            return;

        for (int x = 0; x < tileStepX; x++) {
            int pX = (int) (x * tileWidth);
            Render.line(pX, 0, pX, window.getHeight(), 1, CColor.BLACK);
        }
        for (int y = 0; y < tileStepY; y++) {
            int pY = (int) (y * tileHeight);
            Render.line(0, pY, window.getWidth(), pY, 1, CColor.BLACK);
        }
    }

    public void update(float delta) {
        if (Input.getKeyDown(Keys.KEY_F7)) {
            showGrid = !showGrid;
        }
    }

    public void enableGrid() {
        this.gridEnabled = true;
    }

}
