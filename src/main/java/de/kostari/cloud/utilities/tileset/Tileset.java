package de.kostari.cloud.utilities.tileset;

import java.nio.ByteBuffer;

import de.kostari.cloud.utilities.files.asset.assets.Image;

public class Tileset {

    private Image tilesetImage;

    private Tile[] tiles;

    public Tileset(String path, float tileWidth, float tileHeight) {
        this.tilesetImage = new Image(path);

        float tilesetWidth = tilesetImage.getWidth();
        // float tilesetHeight = tilesetImage.getHeight();

        // Calculate the amount of tiles
        int tilesX = (int) (tilesetWidth / tileWidth);
        // int tilesY = (int) (tilesetHeight / tileHeight);

        // Allocate an array with the amount of tiles
        this.tiles = new Tile[tilesX];
        sliceTileset(tiles, tileWidth, tileHeight);
    }

    public Image getTilesetImage() {
        return tilesetImage;
    }

    private void sliceTileset(Tile[] tiles, float tileWidth, float tileHeight) {
        // The pixelbuffer of the entire tileset image (Loaded via STBImage.stbi_load)
        ByteBuffer tilesetPixelBuffer = tilesetImage.getPixelBuffer().duplicate();

        float width = tilesetImage.getWidth();
        float height = tilesetImage.getHeight();

        // Loop over all available tile slots ((tileSetWidth / tileWidth) +
        // (tileSetHeight / tileHeight))
        for (int i = 0; i < tiles.length; i++) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(tilesetPixelBuffer.capacity());
            int stride = (int) width * 4;

            // calculate the x offset of the current tile
            int offsetX = (int) (tileWidth * i);
            // calculate the width offset of the current tile
            int offsetWidth = (int) (width - (tileWidth * (i + 1)));

            for (int y = 0; y < height; y++) {
                for (int x = offsetX; x < width - offsetWidth; x++) {
                    int s = y * stride + x * 4;

                    // get the current pixels rgba values
                    int r = tilesetPixelBuffer.get(s);
                    int g = tilesetPixelBuffer.get(s + 1);
                    int b = tilesetPixelBuffer.get(s + 2);
                    int a = tilesetPixelBuffer.get(s + 3);

                    // put the pixel colors into the new buffer
                    buffer.put((byte) r);
                    buffer.put((byte) g);
                    buffer.put((byte) b);
                    buffer.put((byte) a);
                }
            }
            // Create a new image with the tile image buffer
            tiles[i] = new Tile(buffer, tileWidth, tileHeight);
            buffer.flip();
        }
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void resizeTiles(float tileWidth, float tileHeight) {
        for (Tile tile : getTiles()) {
            tile.resize(tileWidth, tileHeight);
        }
    }

    public void resizeTiles(float tileWidth, float tileHeight, int filter) {
        for (Tile tile : getTiles()) {
            tile.resize(tileWidth, tileHeight, filter);
        }
    }

}
