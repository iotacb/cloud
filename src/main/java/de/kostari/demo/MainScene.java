package de.kostari.demo;

import org.jbox2d.dynamics.BodyType;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.cloud.utilities.math.Randoms;
import de.kostari.cloud.utilities.render.Image;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.render.text.Fonts;
import de.kostari.demo.objects.Box;
import de.kostari.demo.objects.Circle;

public class MainScene extends Scene {

    private Box ground;
    private Box box, box2;

    private Image logo;

    public MainScene(Window window) {
        super(window);

        // Ground
        this.ground = new Box(window, window.getWidth() * .75F, 32, BodyType.STATIC, false, false);
        ground.transform.position.set(window.getWidth() / 2, window.getHeight() - 32);

        // Spinning box
        this.box = new Box(window, 128, 32, BodyType.KINEMATIC, true, true);
        box.transform.position.set(300, 400);
        box.transform.rotation = 35;

        // Stationary box
        this.box2 = new Box(window, 128, 128, BodyType.STATIC, false, true);
        box2.transform.position.set(window.getWidth() - 500, 200);
        box2.transform.rotation = 5;

        // Spawn objects
        for (int i = 0; i < 10; i++) {
            spawnObject(Randoms.randomInteger(0, (int) window.getWidth()),
                    Randoms.randomInteger(0, 100));
        }

        // Add the objects to the scene
        addObjects(ground, box, box2);

        // Adjust the updates of the physics engine
        // higher physics updates = faster physics
        // getPhysicsEngine().setPhysicsUpdates(1);
        getPhysicsEngine().setPhysicsTimeStep(0.02F);
        getPhysicsEngine().setPhysicsUpdates(4);

        this.logo = new Image("logo.png");
    }

    @Override
    public void draw(float delta) {
        // Draw info to the scene
        Fonts.sans32.drawText("FPS: " + window.getFPS(), 6, 0);
        Fonts.sans32.drawText("Entities: " + getObjects().size(), 6, 32);

        // Render.startSmooth();
        logo.drawImage(window.getWidth() - 100, -5, 100, 100);
        // Render.stopSmooth();

        Render.circleOutlined(Input.getMousePosition(), 100, 2, CColor.RED);
        Render.circleOutlined(window.getHalfSize(), 100, 2, CColor.BLUE);

        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        // Spawn objects when space is pressed
        if (Input.getKeyDown(Keys.KEY_SPACE)) {
            for (int i = 0; i < 10; i++) {
                spawnObject(Randoms.randomInteger(0, (int) window.getWidth()),
                        Randoms.randomInteger(0, 100));
            }
        }

        super.update(delta);
    }

    // Spawn a object at the given position
    private void spawnObject(float x, float y) {

        // Randomize the type of object
        if (Randoms.randomInteger(0, 10) == 5) {
            Circle circle = new Circle(window, Randoms.randomInteger(16, 80),
                    BodyType.DYNAMIC);
            circle.transform.position.set(x, y);
            addObject(circle);
        } else {
            Box box = new Box(window, Randoms.randomInteger(16, 80), Randoms.randomInteger(16, 80), BodyType.DYNAMIC,
                    false, false);
            box.transform.position.set(x, y);
            addObject(box);
        }
    }

}
