import de.kostari.cloud.core.particles.emitters.BoxEmitter;
import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.text.Fonts;

public class RenderingScene extends Scene {

    public RenderingScene(Window window) {
        super(window);
    }

    @Override
    public void draw(float delta) {
        Fonts.sans32.drawCenteredText("Rectangle", 200, 200, CColor.WHITE);
        Render.rect(200, window.getHeight() / 2, 200, 200, CColor.RED);
        Fonts.sans32.drawCenteredText("Circle", window.getHalfSize().x, 200, CColor.WHITE);
        Render.circle(window.getHalfSize(), 200, CColor.GREEN);
        Fonts.sans32.drawCenteredText("Polygon", window.getWidth() - 200, 200, CColor.WHITE);
        Render.polygon(window.getWidth() - 200, window.getHeight() / 2, 200, 6, CColor.BLUE);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

}