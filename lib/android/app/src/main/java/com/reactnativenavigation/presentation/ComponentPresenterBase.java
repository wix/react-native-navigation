package com.reactnativenavigation.presentation;

import android.view.View;
import android.view.ViewGroup;

public class ComponentPresenterBase {
    public boolean applyTopInsets(View view, int topInsets) {
        if (view != null) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (lp.topMargin != topInsets) {
                lp.topMargin = topInsets;
                view.requestLayout();
                return true;
            }
        }
        return false;
    }
}
