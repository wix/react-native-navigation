package com.reactnativenavigation.views.collapsingToolbar;

public class CollapseAmount {
    final static CollapseAmount None = new CollapseAmount();

    private CollapseAmount() {}

    private Float amount;
    private CollapseCalculator.Direction direction;

    CollapseAmount(float amount) {
        this.amount = amount;
    }

    public CollapseAmount(CollapseCalculator.Direction direction) {
        this.direction = direction;
    }

    boolean canCollapse() {
        return amount != null || this != None;
    }

    boolean hasExactAmount() {
        return amount != null;
    }

    boolean collapseToTop() {
        return direction == CollapseCalculator.Direction.Up;
    }

    boolean collapseToBottom() {
        return direction == CollapseCalculator.Direction.Down;
    }

    public float get() {
        return amount;
    }
}
