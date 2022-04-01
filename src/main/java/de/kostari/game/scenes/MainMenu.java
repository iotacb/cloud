package de.kostari.game.scenes;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.input.Keys;
import de.kostari.game.components.player.HealthScript;
import de.kostari.game.enitites.Background;
import de.kostari.game.enitites.Player;
import de.kostari.game.ui.player.PlayerUI;

public class MainMenu extends Scene {

    private Background background;

    private Player player;

    private PlayerUI playerUI;

    public MainMenu(Window window) {
        super(window);
        player = new Player(window, "PLAYER");
        background = new Background(window);
        addObject(player, 1);
        addObject(background, 0);
        addObject(getCamera());

        playerUI = new PlayerUI(window, player);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        getCamera().followObject(player, delta);

        if (window.getInput().getKeyDown(Keys.KEY_R)) {
            addObject(player, 1);
        }

        if (window.getInput().getButtonDown(Keys.GP_A)) {
            player.getComponent(HealthScript.class).damage(10);
        }

        if (window.getInput().getButtonDown(Keys.GP_B)) {
            player.getComponent(HealthScript.class).heal(20);
        }

        playerUI.update(delta);
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
        playerUI.draw(delta);
    }

}
