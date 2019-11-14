package com.reactnativenavigation.presentation;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

public class ComponentPresenterBase {
    public void applyTopInsets(@NonNull View view, int topInsets) {
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        if (lp != null && lp.topMargin != topInsets) {
            lp.topMargin = topInsets;
            view.requestLayout();
        }
    }

    public void applyBottomInset(@NonNull View view, int bottomInset) {
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        if (lp != null && lp.bottomMargin!= bottomInset) {
            lp.bottomMargin = bottomInset;
            view.requestLayout();
        }
    }
}
