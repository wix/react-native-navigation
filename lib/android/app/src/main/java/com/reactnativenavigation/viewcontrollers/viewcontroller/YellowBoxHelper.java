package com.reactnativenavigation.viewcontrollers.viewcontroller;

import android.view.View;
import android.view.ViewGroup;

public class YellowBoxHelper {
    boolean isYellowBox(View parent, View child) {
        return parent instanceof ViewGroup &&
               child instanceof ViewGroup &&
               ((ViewGroup) parent).indexOfChild(child) >= 1;
    }
}
