package com.reactnativenavigation.presentation;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

public class ComponentPresenterBase {
    public void applyTopInsets(View view, int topInsets) {
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        if (lp.topMargin != topInsets) {
            lp.topMargin = topInsets;
            view.requestLayout();
        }
    }
}
