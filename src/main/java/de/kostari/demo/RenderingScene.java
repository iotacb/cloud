package de.kostari.demo;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.render.text.Fonts;

public class RenderingScene extends Scene {

    public RenderingScene(Window window) {
        super(window);
    }

    @Override
    public void draw(float delta) {
        Fonts.sans32.drawCenteredText("Rectangle", 200, 200, CColor.WHITE);
        Render.rect(200, getWindow().getHeight() / 2, 200, 200, CColor.RED);
        Fonts.sans32.drawCenteredText("Circle", getWindow().getHalfSize().x, 200, CColor.WHITE);
        Render.circle(getWindow().getHalfSize(), 200, CColor.GREEN);
        Fonts.sans32.drawCenteredText("Polygon", getWindow().getWidth() - 200, 200, CColor.WHITE);
        Render.polygon(getWindow().getWidth() - 200, getWindow().getHeight() / 2, 200, 6, CColor.BLUE);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

}
