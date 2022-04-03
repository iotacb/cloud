package de.kostari.game.scenes;

import org.lwjgl.opengl.GL11;

import de.kostari.cloud.core.audio.Audio;
import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.cloud.utilities.render.Text;
import de.kostari.cloud.utilities.time.Timer;
import de.kostari.game.enitites.Player;

public class MainMenu extends Scene {

    private Player player;
    private Timer timer;

    private Audio test;

    public MainMenu(Window window) {
        super(window);
        this.player = new Player(window);
        this.timer = new Timer();

        this.test = new Audio("test.ogg", false);

        addObject(player);
    }

    @Override
    public void update(float delta) {

        if (window.getInput().getKeyDown(Keys.KEY_SPACE)) {
            test.play();
        }

        super.update(delta);
    }

    private String text = "Ich liebe dich Schatz <3";
    private String currText = "";

    private float rot = 0;

    @Override
    public void draw(float delta) {
        Vec[] test = {
                new Vec(0, 0), new Vec(200, 0),
                new Vec(200, 200), new Vec(0, 200)
        };

        rot += 60 * delta;

        Render.start();
        Render.color(CColor.ORANGE);
        Render.begin(GL11.GL_QUADS);
        for (Vec v : test) {
            v.rotate(new Vec(100, 100), rot);
            Render.vertex(v);
        }
        Render.end();
        Render.stop();

        if (timer.havePassed(300) && !currText.equals(text)) {
            currText += text.charAt(currText.length());
        }
        Text.drawCenteredText(currText, window.getWidth() / 2,
                window.getHeight() / 2);
        super.draw(delta);
    }

}
