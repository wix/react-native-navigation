package com.reactnativenavigation.views.utils;

import static android.view.View.MeasureSpec;

public class ViewMeasurer {

    public int getMeasuredHeight(int heightMeasureSpec) {
        return MeasureSpec.getSize(heightMeasureSpec);
    }

    public int getMeasuredWidth(int widthMeasureSpec) {
        return MeasureSpec.getSize(widthMeasureSpec);
    }
}
