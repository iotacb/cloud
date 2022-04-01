package de.kostari.game.ui.player;

import de.kostari.cloud.core.gui.GUI;
import de.kostari.cloud.core.objects.GameObject;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.render.Text;
import de.kostari.game.components.player.PlayerScoreScript;

public class PlayerUI extends GUI {

    private HealthBar healthBar;

    private PlayerScoreScript playerScoreScript;

    public PlayerUI(Window window, GameObject player) {
        super(window);

        this.healthBar = new HealthBar(window, player);
        this.playerScoreScript = player.getComponent(PlayerScoreScript.class);
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
        healthBar.draw(delta);
        String scoreText = String.format("Score: %s", playerScoreScript.getCurrentScore());
        Text.drawText(scoreText, window.getWidth() - Text.getWidth(scoreText) - 4, 2);
        String highscoreText = String.format("Highscore: %s", playerScoreScript.getCurrentScore());
        Text.drawText(highscoreText, window.getWidth() - Text.getWidth(highscoreText) - 4, Text.getHeight() + 4);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        healthBar.update(delta);
    }

}
