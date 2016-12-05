package com.reactnativenavigation.views.utils;

import static android.view.View.MeasureSpec;

public class ViewMeasurer {

    public int getMeasuredHeight(int heightMeasuerSpec) {
        return MeasureSpec.getSize(heightMeasuerSpec);
    }

    public int getMeasuredWidth(int widthMeasureSpec) {
        return MeasureSpec.getSize(widthMeasureSpec);
    }
}
