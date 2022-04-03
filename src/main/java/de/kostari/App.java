package de.kostari;

import de.kostari.cloud.core.window.Window;
import de.kostari.game.scenes.MainMenu;

public final class App {
    public static void main(String[] args) {
        Window window = new Window(640, 480, "Cloud");

        window.setVSync(true);
        window.setScene(MainMenu.class);

        try {
            window.show();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
