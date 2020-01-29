package com.reactnativenavigation.viewcontrollers;

import java.util.ArrayDeque;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class ChildControllersRegistry {
    private ArrayDeque<ChildController> children = new ArrayDeque<>();

    public void onViewAppeared(ChildController child) {
        children.push(child);
    }

    public void onViewDisappear(ChildController child) {
        if (isTopChild(child)) {
            children.pop();
            if (!children.isEmpty()) children.peek().onViewBroughtToFront();
        } else {
            children.remove(child);
        }
    }

    private boolean isTopChild(ChildController child) {
        return perform(children.peek(), false, c -> c.equals(child));
    }

    public int size() {
        return children.size();
    }

    public void onChildDestroyed(ChildController child) {
        children.remove(child);
    }
}
