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

    public void applyBottomInset(View view, int bottomInset) {
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        if (lp.bottomMargin!= bottomInset) {
            lp.bottomMargin = bottomInset;
            view.requestLayout();
        }
    }
}
