package com.reactnativenavigation.viewcontrollers;

import android.view.View;
import android.view.ViewGroup;

class YellowBoxHelper {
    boolean isYellowBox(View parent, View child) {
        return parent instanceof ViewGroup &&
               child instanceof ViewGroup &&
               ((ViewGroup) parent).getChildCount() > 1;
    }
}
