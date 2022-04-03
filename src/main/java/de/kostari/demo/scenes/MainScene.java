package de.kostari.demo.scenes;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.demo.objects.Ball;

public class MainScene extends Scene {

    public MainScene(Window window) {
        super(window);

        for (int i = 0; i < 100; i++) {
            // Create a ball
            Ball ball = new Ball(window);

            // (Call the start method -> will be called automatically in later versions)
            ball.start();

            // Add the ball to the scene
            addObject(ball);
        }
    }

    @Override
    public void update(float delta) {
        // Objects added to the scene will automatically be updated
        super.update(delta);
    }

    @Override
    public void draw(float delta) {
        // Objects added to the scene will automatically be drawn
        super.draw(delta);
    }

}
