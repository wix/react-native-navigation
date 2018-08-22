package com.reactnativenavigation.viewcontrollers;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.views.view.ReactViewBackgroundDrawable;
import com.reactnativenavigation.utils.ViewUtils;

public class YellowBoxHelper {
    private final static int YELLOW_BOX_COLOR = -218449360;

    public static boolean isYellowBox(ViewGroup parent, ViewGroup child) {
        return parent.getChildCount() > 1 &&
               !ViewUtils.findChildrenByClassRecursive(child, View.class, YellowBackgroundMather()).isEmpty();
    }

    @NonNull
    private static ViewUtils.Matcher<View> YellowBackgroundMather() {
        return child1 -> child1.getBackground() instanceof ReactViewBackgroundDrawable && ((ReactViewBackgroundDrawable) child1.getBackground()).getColor() == YELLOW_BOX_COLOR;
    }
}
