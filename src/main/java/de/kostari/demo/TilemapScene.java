package de.kostari.demo;

import org.lwjgl.stb.STBImageResize;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.files.asset.assets.Image;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.tileset.TileManager;
import de.kostari.cloud.utilities.tileset.Tileset;

public class TilemapScene extends Scene {

    private Tileset tileset = new Tileset("tileset.png", 16, 16);
    private Image image = new Image("tt.png");

    private TileManager tileManager;

    private int currentTile;

    public TilemapScene(Window window) {
        super(window);

        tileset.resizeTiles(64, 64, STBImageResize.STBIR_FILTER_BOX);
        image.resize(128, 128);

        this.tileManager = new TileManager(window, 64, 64);
        tileManager.enableGrid();
    }

    @Override
    public void draw(float delta) {
        tileManager.draw(delta);

        int posX = Input.getMouseX();
        int posY = Input.getMouseY();
        for (int y = 0; y < tileManager.getTileStepY(); y++) {
            for (int x = 0; x < tileManager.getTileStepX(); x++) {
                if (posX > x * tileManager.getTileWidth() && posX < (x + 1) * tileManager.getTileWidth()
                        && posY > y * tileManager.getTileHeight()
                        && posY < (y + 1) * tileManager.getTileHeight()) {
                    posX = (int) (x * tileManager.getTileWidth());
                    posY = (int) (y * tileManager.getTileHeight());
                    // Render.rect(posX + tileManager.getTileWidth() / 2, posY +
                    // tileManager.getTileHeight() / 2,
                    // tileManager.getTileWidth(),
                    // tileManager.getTileHeight(), CColor.WHITE.setAlpha(100));
                    Render.image(tileset.getTiles()[currentTile], posX + tileManager.getTileWidth() / 2,
                            posY + tileManager.getTileHeight() / 2, CColor.WHITE.setAlpha(100));

                }
            }
        }
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        tileManager.update(delta);
        if (Input.mouseButtonPressed(0)) {
            tileManager.addTile(tileset.getTiles()[currentTile], Input.getMouseX(), Input.getMouseY());
        }

        if (Input.keyPressed(Keys.KEY_1)) {
            currentTile = 0;
        } else if (Input.keyPressed(Keys.KEY_2)) {
            currentTile = 1;
        } else if (Input.keyPressed(Keys.KEY_3)) {
            currentTile = 2;
        } else if (Input.keyPressed(Keys.KEY_4)) {
            currentTile = 3;
        }
        super.update(delta);
    }

}
