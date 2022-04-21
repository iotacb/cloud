package de.kostari.demo;

import org.lwjgl.stb.STBImageResize;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.files.asset.assets.Image;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.tileset.TileManager;
import de.kostari.cloud.utilities.tileset.Tileset;

public class MainScene extends Scene {

    private Tileset tileset = new Tileset("tileset.png", 16, 16);
    private Image image = new Image("tt.png");

    private TileManager tileManager;

    public MainScene(Window window) {
        super(window);

        tileset.resizeTiles(64, 64, STBImageResize.STBIR_FILTER_BOX);
        image.resize(128, 128);

        this.tileManager = new TileManager(window, 64, 64);
        tileManager.enableGrid();
    }

    @Override
    public void draw(float delta) {
        tileManager.draw(delta);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        tileManager.update(delta);
        if (Input.getMouseButtonDown(0)) {
            tileManager.addTile(tileset.getTiles()[0], Input.getMouseX(), Input.getMouseY());
        }
        super.update(delta);
    }

}
