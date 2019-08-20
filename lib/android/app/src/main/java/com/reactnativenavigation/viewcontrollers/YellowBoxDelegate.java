package com.reactnativenavigation.viewcontrollers;

import androidx.annotation.RestrictTo;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class YellowBoxDelegate {
    private ViewGroup parent;
    private YellowBoxHelper yellowBoxHelper;
    private boolean isDestroyed;
    private ArrayList<View> yellowBoxViews = new ArrayList<>();

    public YellowBoxDelegate() {
        this.yellowBoxHelper = new YellowBoxHelper();
    }

    YellowBoxDelegate(YellowBoxHelper yellowBoxHelper) {
        this.yellowBoxHelper = yellowBoxHelper;
    }

    public void onChildViewAdded(View parent, View child) {
        UiUtils.runOnPreDrawOnce(child, () -> {
            if (yellowBoxHelper.isYellowBox(parent, child)) {
                onYellowBoxAdded(parent);
            }
        });
    }

    void onYellowBoxAdded(View parent) {
        if (isDestroyed) return;
        this.parent = (ViewGroup) parent;

        for (int i = 1; i < this.parent.getChildCount(); i++) {
            yellowBoxViews.add(this.parent.getChildAt(i));
            this.parent.removeView(this.parent.getChildAt(i));
            this.parent.addView(new View(parent.getContext()), i);
        }
    }

    public void destroy() {
        isDestroyed = true;
        if (!yellowBoxViews.isEmpty()) {
            for (View view : yellowBoxViews) {
                parent.addView(view);
            }
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public ViewGroup getParent() {
        return parent;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public List<View> getYellowBoxes() {
        return yellowBoxViews;
    }
}
