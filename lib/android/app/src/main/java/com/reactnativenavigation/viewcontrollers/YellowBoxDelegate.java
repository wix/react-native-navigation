package com.reactnativenavigation.viewcontrollers;

import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.ViewGroup;

public class YellowBoxDelegate {
    private ViewGroup parent;
    private View yellowBox;
    private YellowBoxHelper yellowBoxHelper;
    private boolean isDestroyed;

    public YellowBoxDelegate() {
        this.yellowBoxHelper = new YellowBoxHelper();
    }

    YellowBoxDelegate(YellowBoxHelper yellowBoxHelper) {
        this.yellowBoxHelper = yellowBoxHelper;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public ViewGroup getParent() {
        return parent;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public View getYellowBox() {
        return yellowBox;
    }

    public void onChildViewAdded(View parent, View child) {
        if (yellowBoxHelper.isYellowBox(parent, child)) {
            onYellowBoxAdded(parent, child);
        }
    }

    protected void onYellowBoxAdded(View parent, View yellowBox) {
        if (isDestroyed) return;
        this.yellowBox = yellowBox;
        this.parent = (ViewGroup) parent;
        this.parent.removeView(yellowBox);
    }

    public void destroy() {
        isDestroyed = true;
        if (yellowBox != null) {
            parent.addView(yellowBox);
        }
    }
}
