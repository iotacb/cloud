package de.kostari.game.ui.player;

import java.awt.Color;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.gui.GUI;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.math.Maths;
import de.kostari.cloud.utilities.render.Render;
import de.kostari.game.components.player.HealthScript;

public class HealthBar extends GUI {

    private static float BAR_WIDTH = 300;
    private static float BAR_HEIGHT = 40;
    private static float BAR_X = 5;
    private static float BAR_Y = 5;
    private static float BAR_PADDING = 5;
    private static float DAMPING = 10;

    private static Color BAR_COLOR = new Color(60, 60, 60);
    private static Color HEALTH_COLOR = new Color(250, 60, 60);

    private float currentBarWidth = 0;
    private GameObject player;

    public HealthBar(Window window, GameObject player) {
        super(window);
        this.player = player;
        transform.position.set(BAR_X, BAR_Y);
        addComponent(new Bounds(BAR_WIDTH, BAR_HEIGHT));
    }

    @Override
    public void draw(float delta) {
        float wantedBarWidth = (getComponent(Bounds.class).width - (BAR_PADDING * 2))
                * player.getComponent(HealthScript.class).getHealthPercentage();
        currentBarWidth = Maths.lerp(currentBarWidth, wantedBarWidth, DAMPING * delta);

        Render.rect(transform.position.x, transform.position.y, getComponent(Bounds.class).width,
                getComponent(Bounds.class).height, BAR_COLOR);
        Render.rect(transform.position.x + BAR_PADDING, transform.position.y + BAR_PADDING, currentBarWidth,
                getComponent(Bounds.class).height - (BAR_PADDING * 2),
                HEALTH_COLOR);
        super.draw(delta);
    }

}
