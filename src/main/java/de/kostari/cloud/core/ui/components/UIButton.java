package de.kostari.cloud.core.ui.components;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.ui.UIComponent;
import de.kostari.cloud.core.ui.listener.ClickListener;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.input.Input;
import de.kostari.cloud.utilities.render.Render;

public class UIButton extends UIComponent {

    private Bounds bounds;

    private final CColor NORMAL_COLOR = new CColor(255, 255, 255, 125);
    private final CColor HOVER_COLOR = new CColor(255, 255, 255, 175);

    private ClickListener clickListener;

    private String title;

    public UIButton(Window window, String title) {
        super(window);

        this.bounds = addComponent(new Bounds(200, 60));
        this.title = title;
    }

    public UIButton(Window window, String title, ClickListener clickListener) {
        super(window);

        this.bounds = getComponent(Bounds.class);
        this.clickListener = clickListener;
    }

    @Override
    public void draw(float delta) {
        super.draw(delta);
        Render.rect(transform.position, bounds.getSize(), isHovered() ? HOVER_COLOR : NORMAL_COLOR);
        // Fonts.sans32.drawCenteredText(title, transform.position.x,
        // transform.position.y);
    }

    @Override
    public void update(float delta) {
        if (isHovered()) {
            if (Input.getMouseButtonDown(0)) {
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

    public String getTitle() {
        return title;
    }

    public CColor getNORMAL_COLOR() {
        return NORMAL_COLOR;
    }

    public CColor getHOVER_COLOR() {
        return HOVER_COLOR;
    }

}
