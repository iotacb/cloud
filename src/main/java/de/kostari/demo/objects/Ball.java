package de.kostari.demo.objects;

import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;

public class Ball extends GameObject {

    private static float SPEED = 300;
    private static float SIZE = 20;

    private int randomColor;

    private Vec direction = new Vec();

    public Ball(Window window) {
        super(window);
    }

    @Override
    public void start() {
        // Set the position of the ball to a random position
        // on the screen
        float randomX = random(SIZE * 2, window.getWidth() - SIZE * 2);
        float randomY = random(SIZE * 2, window.getHeight() - SIZE * 2);
        transform.position.set(randomX, randomY);

        // Set the color of the ball to a random hex color
        randomColor = (int) (Math.random() * 0xFFFFFF);

        // Set the direction of the ball to a random direction
        float randomAngle = (float) (Math.random() * 360);
        direction = Vec.fromAngle(randomAngle);

        super.start();
    }

    @Override
    public void draw(float delta) {
        // Draw a circle at the ball location
        Render.circleCentered(transform.position, SIZE, new CColor(randomColor));
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        // Check if the ball collides with a will
        // if so reverse the direction
        checkBounds();

        // Move to ball into the current direction
        transform.position.add(new Vec(direction).mul(SPEED * delta));
        super.update(delta);
    }

    private void checkBounds() {
        // Check the position and bounds
        if (transform.position.x - (SIZE / 2) < 0 || transform.position.x + (SIZE / 2) > window.getWidth()) {
            direction.x *= -1;
        }
        if (transform.position.y - (SIZE / 2) < 0 || transform.position.y + (SIZE / 2) > window.getHeight()) {
            direction.y *= -1;
        }
    }

    // random min max
    public static float random(float min, float max) {
        return min + (float) (Math.random() * (max - min));
    }

}