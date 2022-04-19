package de.kostari.cloud.core.ui.layout.layouts;

import java.util.List;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.ui.UIComponent;
import de.kostari.cloud.core.ui.layout.UILayout;

public class AbsoluteLayout extends UILayout {

    private float x;
    private float y;

    @Override
    public void alignChildren(List<UIComponent> components) {
        List<UIComponent> children = isOrderReversed() ? reveresedList(components) : components;
        Bounds parentBounds = getParent().getComponent(Bounds.class);

        if (parentBounds == null)
            return;
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x + x;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);
                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }
                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + y + childBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y + y;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);
                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }
                    child.transform.position.x = getParent().transform.position.x + x + childBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY;
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

}