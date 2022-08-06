package de.kostari.cloud.core.ui.components;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.ui.UIComponent;
import de.kostari.cloud.core.ui.listener.ClickListener;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.files.asset.assets.Image;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.render.Render;

public class UIImageButton extends UIComponent {

    private Bounds bounds;

    private final CColor NORMAL_COLOR = new CColor(255, 255, 255, 125);
    private final CColor HOVER_COLOR = new CColor(255, 255, 255, 175);

    private ClickListener clickListener;

    private Image image;

    public UIImageButton(Window window, Image image) {
        super(window);

        this.bounds = addComponent(new Bounds(200, 60));
        this.image = image;
    }

    public UIImageButton(Window window, Image image, ClickListener clickListener) {
        super(window);

        this.bounds = addComponent(new Bounds(200, 60));
        this.image = image;
        this.clickListener = clickListener;
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
        Render.rect(transform.position, bounds.getSize(), isHovered() ? HOVER_COLOR : NORMAL_COLOR);
        Render.image(image, transform.position, bounds.getSize());
        // Fonts.sans32.drawCenteredText(title, transform.position.x,
        // transform.position.y);
    }

    @Override
    public void update(float delta) {
        if (isHovered()) {
            if (Input.mouseButtonPressed(0)) {
                if (clickListener != null) {
                    clickListener.clicked(0, Input.getMouseX(), Input.getMouseY());
                }
            }
            // window.setCursor(GLFW.GLFW_HAND_CURSOR);
        } else {
            // window.setCursor(GLFW.GLFW_ARROW_CURSOR);
        }
        super.update(delta);
    }

    public boolean isHovered() {
        boolean x = Input.getMouseX() > transform.position.x - bounds.getHalfWidth()
                && Input.getMouseX() < transform.position.x + bounds.getHalfWidth();
        boolean y = Input.getMouseY() > transform.position.y - bounds.getHalfHeight()
                && Input.getMouseY() < transform.position.y + bounds.getHalfHeight();
        return x && y;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public Image getImage() {
        return image;
    }

    public CColor getNORMAL_COLOR() {
        return NORMAL_COLOR;
    }

    public CColor getHOVER_COLOR() {
        return HOVER_COLOR;
    }

}
