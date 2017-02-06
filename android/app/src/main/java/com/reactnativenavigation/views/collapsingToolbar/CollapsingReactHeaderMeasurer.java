package com.reactnativenavigation.views.collapsingToolbar;

import android.view.ViewGroup;

import com.reactnativenavigation.views.utils.ViewMeasurer;

class CollapsingReactHeaderMeasurer extends ViewMeasurer {
    private ViewGroup header;

    CollapsingReactHeaderMeasurer(ViewGroup header) {
        this.header = header;
    }

    @Override
    public int getMeasuredHeight(int heightMeasureSpec) {
        return hasChildren() ? header.getChildAt(0).getMeasuredHeight() : super.getMeasuredHeight(heightMeasureSpec);
    }

    private boolean hasChildren() {
        return header.getChildCount() > 0;
    }
}
