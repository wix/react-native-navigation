package com.reactnativenavigation.viewcontrollers;

import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.views.view.ReactViewBackgroundDrawable;
import com.reactnativenavigation.utils.ViewUtils;

import androidx.annotation.NonNull;

import static com.reactnativenavigation.utils.ViewUtils.findChildrenByClassRecursive;

public class YellowBoxHelper {
    private final static int YELLOW_BOX_COLOR = -218449360;

    boolean isYellowBox(View parent, View child) {
        return parent instanceof ViewGroup &&
               child instanceof ViewGroup &&
               ((ViewGroup) parent).getChildCount() > 1 &&
               !findChildrenByClassRecursive((ViewGroup) child, View.class, YellowBackgroundMather()).isEmpty();
    }

    @NonNull
    private static ViewUtils.Matcher<View> YellowBackgroundMather() {
        return child1 -> child1.getBackground() instanceof ReactViewBackgroundDrawable && ((ReactViewBackgroundDrawable) child1.getBackground()).getColor() == YELLOW_BOX_COLOR;
    }
}
