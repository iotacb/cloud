import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;

public class App {

    public static void main(String[] args) {
        // Create a new window
        Window window = new Window(1280, 720, "Particle System Demo");

        // Enable vsync
        window.useVsync(true);

        // Set the scene
        window.setScene(ParticleScene.class);

        // Make the clear color transparent to fake a trail effect for the particles
        window.setClearColor(CColor.BLACK.setAlpha(20));

        // Show the window
        try {
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
