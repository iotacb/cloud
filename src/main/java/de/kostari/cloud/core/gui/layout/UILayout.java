package de.kostari.cloud.core.gui.layout;

import java.util.ArrayList;
import java.util.List;

import de.kostari.cloud.core.components.Bounds;
import de.kostari.cloud.core.gui.UIComponent;
import de.kostari.cloud.core.gui.enums.LayoutDirections;

public abstract class UILayout {

    private UIComponent parent;

    private boolean reverseOrder = false;

    private float gap = 0;

    private LayoutDirections childrenDirection = LayoutDirections.HORIZONTAL;

    public abstract void alignChildren(List<UIComponent> components);

    public UIComponent getParent() {
        return parent;
    }

    public void setParent(UIComponent parent) {
        this.parent = parent;
    }

    public List<UIComponent> reveresedList(List<UIComponent> children) {
        List<UIComponent> list = new ArrayList<>();
        for (int i = children.size() - 1; i >= 0; i--) {
            list.add(children.get(i));
        }
        return list;
    }

    public float getChildrenWidth(List<UIComponent> children) {
        float width = 0;
        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            Bounds bounds = child.getComponent(Bounds.class);
            width += bounds.getWidth();
        }
        return width;
    }

    public float getChildrenHeight(List<UIComponent> children) {
        float height = 0;
        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            Bounds bounds = child.getComponent(Bounds.class);
            height += bounds.getHeight();
        }
        return height;
    }

    public boolean isOrderReversed() {
        return reverseOrder;
    }

    public LayoutDirections getChildrenDirection() {
        return childrenDirection;
    }

    public UILayout setOrderReveresed(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
        return this;
    }

    public UILayout setChildrenDirection(LayoutDirections childrenDirection) {
        this.childrenDirection = childrenDirection;
        return this;
    }

    public float getGap() {
        return gap;
    }

    public UILayout setGap(float gap) {
        this.gap = gap;
        return this;
    }

}
