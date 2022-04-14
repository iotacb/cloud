package de.kostari.cloud.core.ui.layout.layouts;

import java.util.List;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.ui.UIComponent;
import de.kostari.cloud.core.ui.enums.LayoutDistributions;
import de.kostari.cloud.core.ui.enums.LayoutPositions;
import de.kostari.cloud.core.ui.layout.UILayout;

public class DistributedLayout extends UILayout {

    private LayoutPositions position = LayoutPositions.TOP_LEFT;
    private LayoutDistributions distribution = LayoutDistributions.EVEN;

    @Override
    public void alignChildren(List<UIComponent> components) {
        List<UIComponent> children = isOrderReversed() ? reveresedList(components) : components;
        Bounds parentBounds = getParent().getComponent(Bounds.class);
        switch (position) {
            case TOP:
                alignTop(parentBounds, children);
                break;
            case CENTER:
                alignCenter(parentBounds, children);
                break;
            case BOTTOM:
                alignBottom(parentBounds, children);
                break;
            default:
                alignTop(parentBounds, children);
                break;
        }
    }

    private void alignTop(Bounds parentBounds, List<UIComponent> children) {
        float spacing = 0;
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                spacing = (parentBounds.getWidth() - getChildrenWidth(children))
                        / (children.size() + (distribution == LayoutDistributions.AROUND ? 0 : 1));
                float latestX = getParent().transform.position.x;

                switch (distribution) {
                    case EVEN:
                        latestX += spacing;
                        break;
                    case AROUND:
                        latestX += spacing / 2;
                        break;
                    case BETWEEN:
                        latestX += spacing;
                        break;
                }

                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);
                    if (distribution == LayoutDistributions.BETWEEN) {
                        if (i == 0) {
                            child.transform.position.x = getParent().transform.position.x;
                        } else if (i == children.size() - 1) {
                            child.transform.position.x = getParent().transform.position.x + parentBounds.getWidth()
                                    - childBounds.getWidth();
                        } else {
                            child.transform.position.x = latestX;
                        }
                    } else {
                        child.transform.position.x = latestX;
                    }
                    child.transform.position.y = getParent().transform.position.y;
                    latestX += childBounds.getWidth() + spacing;
                }
                break;
            case VERTICAL:
                spacing = (parentBounds.getHeight() - getChildrenHeight(children))
                        / (children.size() + (distribution == LayoutDistributions.AROUND ? 0 : 1));

                float latestY = getParent().transform.position.y;
                switch (distribution) {
                    case EVEN:
                        latestY += spacing;
                        break;
                    case AROUND:
                        latestY += spacing / 2;
                        break;
                    case BETWEEN:
                        break;
                }

                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);
                    child.transform.position.x = getParent().transform.position.x;

                    if (distribution == LayoutDistributions.BETWEEN) {
                        if (i == 0) {
                            child.transform.position.y = getParent().transform.position.y;
                        } else if (i == children.size() - 1) {
                            child.transform.position.y = getParent().transform.position.y + parentBounds.getHeight()
                                    - childBounds.getHeight();
                        } else {
                            child.transform.position.y = latestY;
                        }
                    } else {
                        child.transform.position.y = latestY;
                    }
                    latestY += childBounds.getHeight() + spacing;
                }
                break;
        }
    }

    private void alignCenter(Bounds parentBounds, List<UIComponent> children) {
        float spacing = 0;
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                spacing = (parentBounds.getWidth() - getChildrenWidth(children))
                        / (children.size() + (distribution == LayoutDistributions.AROUND ? 0 : 1));
                float latestX = getParent().transform.position.x;

                switch (distribution) {
                    case EVEN:
                        latestX += spacing;
                        break;
                    case AROUND:
                        latestX += spacing / 2;
                        break;
                    case BETWEEN:
                        break;
                }

                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (distribution == LayoutDistributions.BETWEEN) {
                        if (i == 0) {
                            child.transform.position.x = getParent().transform.position.x;
                        } else if (i == children.size() - 1) {
                            child.transform.position.x = getParent().transform.position.x + parentBounds.getWidth()
                                    - childBounds.getWidth();
                        } else {
                            child.transform.position.x = latestX;
                        }
                    } else {
                        child.transform.position.x = latestX;
                    }
                    child.transform.position.y = getParent().transform.position.y + parentBounds.getHalfHeight()
                            - childBounds.getHalfHeight();
                    latestX += childBounds.getWidth() + spacing;
                }
                break;
            case VERTICAL:
                spacing = (parentBounds.getHeight() - getChildrenHeight(children))
                        / (children.size() + (distribution == LayoutDistributions.AROUND ? 0 : 1));
                float latestY = getParent().transform.position.y;

                switch (distribution) {
                    case EVEN:
                        latestY += spacing;
                        break;
                    case AROUND:
                        latestY += spacing / 2;
                        break;
                    case BETWEEN:
                        break;
                }

                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);
                    child.transform.position.x = getParent().transform.position.x + parentBounds.getHalfWidth()
                            - childBounds.getWidth() / 2;

                    if (distribution == LayoutDistributions.BETWEEN) {
                        if (i == 0) {
                            child.transform.position.y = getParent().transform.position.y;
                        } else if (i == children.size() - 1) {
                            child.transform.position.y = getParent().transform.position.y + parentBounds.getHeight()
                                    - childBounds.getHeight();
                        } else {
                            child.transform.position.y = latestY;
                        }
                    } else {
                        child.transform.position.y = latestY;
                    }

                    latestY += childBounds.getHeight() + spacing;
                }
                break;
        }
    }

    private void alignBottom(Bounds parentBounds, List<UIComponent> children) {
        float spacing = 0;
        switch (getChildrenDirection()) {
            case HORIZONTAL:
                spacing = (parentBounds.getWidth() - getChildrenWidth(children))
                        / (children.size() + (distribution == LayoutDistributions.AROUND ? 0 : 1));
                float latestX = getParent().transform.position.x;

                switch (distribution) {
                    case EVEN:
                        latestX += spacing;
                        break;
                    case AROUND:
                        latestX += spacing / 2;
                        break;
                    case BETWEEN:
                        break;
                }

                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);

                    if (distribution == LayoutDistributions.BETWEEN) {
                        if (i == 0) {
                            child.transform.position.x = getParent().transform.position.x;
                        } else if (i == children.size() - 1) {
                            child.transform.position.x = getParent().transform.position.x + parentBounds.getWidth()
                                    - childBounds.getWidth();
                        } else {
                            child.transform.position.x = latestX;
                        }
                    } else {
                        child.transform.position.x = latestX;
                    }

                    child.transform.position.y = getParent().transform.position.y + parentBounds.getHeight()
                            - childBounds.getHeight();
                    latestX += childBounds.getWidth() + spacing;
                }
                break;
            case VERTICAL:
                spacing = (parentBounds.getHeight() - getChildrenHeight(children))
                        / (children.size() + (distribution == LayoutDistributions.AROUND ? 0 : 1));
                float latestY = getParent().transform.position.y;

                switch (distribution) {
                    case EVEN:
                        latestY += spacing;
                        break;
                    case AROUND:
                        latestY += spacing / 2;
                        break;
                    case BETWEEN:
                        break;
                }

                for (int i = 0; i < children.size(); i++) {
                    UIComponent child = children.get(i);
                    Bounds childBounds = child.getComponent(Bounds.class);
                    child.transform.position.x = getParent().transform.position.x + parentBounds.getWidth()
                            - childBounds.getWidth();

                    if (distribution == LayoutDistributions.BETWEEN) {
                        if (i == 0) {
                            child.transform.position.y = getParent().transform.position.y;
                        } else if (i == children.size() - 1) {
                            child.transform.position.y = getParent().transform.position.y + parentBounds.getHeight()
                                    - childBounds.getHeight();
                        } else {
                            child.transform.position.y = latestY;
                        }
                    } else {
                        child.transform.position.y = latestY;
                    }

                    latestY += childBounds.getHeight() + spacing;
                }
                break;
        }
    }

    public void setDistribution(LayoutDistributions distribution) {
        this.distribution = distribution;
    }

    public void setPosition(LayoutPositions position) {
        this.position = position;
    }

}