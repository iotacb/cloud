package de.kostari.cloud.utilities.tileset;

import java.nio.ByteBuffer;

import de.kostari.cloud.core.components.Transform;
import de.kostari.cloud.utilities.files.asset.assets.Image;

public class Tile extends Image {

    public Transform transform;

    public Tile(String path) {
        super(path);
        this.transform = new Transform();
    }

    public Tile(ByteBuffer buffer, float width, float height) {
        super(buffer, width, height);
        this.transform = new Transform();
    }

    public Tile clone() {
        return new Tile(getPixelBuffer(), getWidth(), getHeight());
    }

}
