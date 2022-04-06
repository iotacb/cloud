package de.kostari.cloud.core.gui;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.gui.layout.UILayout;
import de.kostari.cloud.core.gui.layout.layouts.PositionedLayout;
import de.kostari.cloud.core.window.Window;
import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.render.Render;

public class UIContainer extends UIComponent {

    private CColor color = CColor.WHITE;

    private UILayout layout;

    public UIContainer(Window window) {
        super(window);

        init(0, 0);
    }

    public UIContainer(Window window, float width, float height) {
        super(window);

        init(width, height);
    }

    public UIContainer(Window window, int t) {
        super(window);

        if (t == 0) {
            this.color = CColor.RED;
        } else if (t == 1) {
            this.color = CColor.BLUE;
        } else if (t == 2) {
            this.color = CColor.GREEN;
        } else if (t == 3) {
            this.color = CColor.ORANGE;
        } else if (t == -1) {
            this.color = CColor.TRANSPARENT;
        } else if (t == 4) {
            this.color = CColor.MAROON;
        }

        init(0, 0);
    }

    private void init(float width, float height) {
        setLayout(new PositionedLayout());
        setSize(width, height);
    }

    @Override
    public void draw(float delta) {
        Render.rect(transform.position, getComponent(Bounds.class).getSize(), color);
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        if (getChildren().size() > 0) {
            layout.alignChildren(getChildren());
        }
        super.update(delta);
    }

    public void setLayout(UILayout layout) {
        this.layout = layout;
        this.layout.setParent(this);
    }

    public UILayout getLayout() {
        return layout;
    }

}
