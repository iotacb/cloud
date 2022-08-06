package de.kostari.cloud.core.components;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.files.asset.assets.Image;
import de.kostari.cloud.utilities.render.Render;

public class Sprite extends Component {

    private Image image;
    private CColor color;

    public Sprite(String path, CColor color) {
        this.image = new Image(path);
        this.color = color;
    }

    @Override
    public void draw(float delta) {
        Render.image(image, gameObject.transform.position, color);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public Image getImage() {
        return image;
    }

    public CColor getColor() {
        return color;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setColor(CColor color) {
        this.color = color;
    }

}
