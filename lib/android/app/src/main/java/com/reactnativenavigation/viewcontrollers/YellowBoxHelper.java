package com.reactnativenavigation.viewcontrollers;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.views.view.ReactViewBackgroundDrawable;
import com.reactnativenavigation.utils.ViewUtils;

class YellowBoxHelper {
    private final static int YELLOW_BOX_COLOR = -218449360;

    boolean isYellowBox(View parent, View child) {
        return parent instanceof ViewGroup &&
               child instanceof ViewGroup &&
               ((ViewGroup) parent).getChildCount() > 1 &&
               !ViewUtils.findChildrenByClassRecursive((ViewGroup) child, View.class, YellowBackgroundMather((ViewGroup) child)).isEmpty();
    }

    @NonNull
    private static ViewUtils.Matcher<View> YellowBackgroundMather(ViewGroup vg) {
        return child1 -> child1.getBackground() instanceof ReactViewBackgroundDrawable && ((ReactViewBackgroundDrawable) child1.getBackground()).getColor() == YELLOW_BOX_COLOR;
    }
}
