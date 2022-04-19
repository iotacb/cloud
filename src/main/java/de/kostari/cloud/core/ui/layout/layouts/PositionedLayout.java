package de.kostari.cloud.core.ui.layout.layouts;

import java.util.List;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.ui.UIComponent;
import de.kostari.cloud.core.ui.enums.LayoutPositions;
import de.kostari.cloud.core.ui.layout.UILayout;

public class PositionedLayout extends UILayout {

    private LayoutPositions position = LayoutPositions.TOP_LEFT;

    @Override
    public void alignChildren(List<UIComponent> components) {
        List<UIComponent> children = isOrderReversed() ? reveresedList(components) : components;
        Bounds parentBounds = getParent().getComponent(Bounds.class);
        if (parentBounds == null)
            return;
        switch (position) {
            case TOP_LEFT:
                alignTopLeft(parentBounds, children);
                break;
            case TOP_CENTER:
                alignTopCenter(parentBounds, children);
                break;
            case TOP_RIGHT:
                alignTopRight(parentBounds, children);
                break;
            case CENTER_LEFT:
                alignCenterLeft(parentBounds, children);
                break;
            case CENTER_CENTER:
                alignCenterCenter(parentBounds, children);
                break;
            case CENTER_RIGHT:
                alignCenterRight(parentBounds, children);
                break;
            case BOTTOM_LEFT:
                alignBottomLeft(parentBounds, children);
                break;
            case BOTTOM_CENTER:
                alignBottomCenter(parentBounds, children);
                break;
            case BOTTOM_RIGHT:
                alignBottomRight(parentBounds, children);
                break;
            case BOTTOM:
                alignBottomCenter(parentBounds, children);
                break;
            case CENTER:
                alignCenterCenter(parentBounds, children);
                break;
            case TOP:
                alignTopCenter(parentBounds, children);
                break;
        }
    }

    private void alignTopLeft(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);
                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }
                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + childBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);
                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }
                    child.transform.position.x = getParent().transform.position.x + childBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY;
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    private void alignTopCenter(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x + parentBounds.getHalfWidth()
                        - getChildrenWidth(children) / 2;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }

                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + childBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }

                    child.transform.position.x = getParent().transform.position.x + parentBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY;
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    private void alignTopRight(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x + parentBounds.getWidth() - getChildrenWidth(children)
                        - (getGap() * (children.size() - 1));
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }

                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + childBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }

                    child.transform.position.x = getParent().transform.position.x + parentBounds.getWidth()
                            - childBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY;
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    private void alignCenterLeft(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }

                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + parentBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y + parentBounds.getHalfHeight()
                        - getChildrenHeight(children) / 2;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }

                    child.transform.position.x = getParent().transform.position.x + childBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY;
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    private void alignCenterCenter(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x + parentBounds.getHalfWidth()
                        - getChildrenWidth(children) / 2;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }

                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + parentBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y + parentBounds.getHalfHeight()
                        - getChildrenHeight(children) / 2;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }

                    child.transform.position.x = getParent().transform.position.x + parentBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY;
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    private void alignCenterRight(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x + parentBounds.getWidth() - getChildrenWidth(children)
                        - (getGap() * (children.size() - 1));
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }

                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + parentBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y + parentBounds.getHalfHeight()
                        - getChildrenHeight(children) / 2;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }

                    child.transform.position.x = getParent().transform.position.x + parentBounds.getWidth()
                            - childBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY;
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    private void alignBottomLeft(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }

                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + parentBounds.getHeight()
                            - childBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y + parentBounds.getHeight()
                        - getChildrenHeight(children);
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }

                    child.transform.position.x = getParent().transform.position.x + childBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY - getGap();
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    private void alignBottomCenter(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x + parentBounds.getHalfWidth()
                        - getChildrenWidth(children) / 2;
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }

                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + parentBounds.getHeight()
                            - childBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y + parentBounds.getHeight()
                        - getChildrenHeight(children);
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }

                    child.transform.position.x = getParent().transform.position.x + parentBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY - getGap();
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    private void alignBottomRight(Bounds parentBounds, List<UIComponent> children) {
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                float offsetX = 0;
                float latestX = getParent().transform.position.x + parentBounds.getWidth() - getChildrenWidth(children)
                        - (getGap() * (children.size() - 1));
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetX = childBounds.getHalfWidth();
                    }

                    child.transform.position.x = latestX + offsetX;
                    child.transform.position.y = getParent().transform.position.y + parentBounds.getHeight()
                            - childBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + getGap();

                    if (i == 0) {
                        latestX += childBounds.getHalfWidth();
                    }
                }
                break;
            case VERTICAL:
                float offsetY = 0;
                float latestY = getParent().transform.position.y + parentBounds.getHeight()
                        - getChildrenHeight(children);
                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (i == 0) {
                        offsetY = childBounds.getHalfHeight();
                    }

                    child.transform.position.x = getParent().transform.position.x + parentBounds.getWidth()
                            - childBounds.getHalfWidth();
                    child.transform.position.y = latestY + offsetY - getGap();
                    latestY += childBounds.getHeight() + getGap();
                }
                break;
        }
    }

    public void setPosition(LayoutPositions position) {
        this.position = position;
    }

}