package de.kostari.cloud.core.ui;

import de.kostari.cloud.core.ui.layout.UILayout;
import de.kostari.cloud.core.ui.layout.layouts.PositionedLayout;
import de.kostari.cloud.core.window.Window;

public class UIContainer extends UIComponent {

    private UILayout layout;

    public UIContainer(Window window) {
        super(window);

        init(0, 0);
    }

    public UIContainer(Window window, float width, float height) {
        super(window);

        init(width, height);
    }

    private void init(float width, float height) {
        setLayout(new PositionedLayout());
        setSize(width, height);
    }

    @Override
    public void draw(float delta) {
        if (isDisabled())
            return;
        super.draw(delta);
    }

    @Override
    public void update(float delta) {
        if (isDisabled())
            return;
        if (getChildren().size() > 0) {
            layout.alignChildren(getChildren());
        }
        super.update(delta);
    }

    /**
     * Change the layout of the container.
     * The layout defines how the container children are positioned.
     * 
     * @param layout
     */
    public void setLayout(UILayout layout) {
        this.layout = layout;
        this.layout.setParent(this);
    }

    public UILayout getLayout() {
        return layout;
    }

}
