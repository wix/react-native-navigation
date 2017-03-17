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
        return hasChildren() ? getFirstChildHeightAndTopMargin() : super.getMeasuredHeight(heightMeasureSpec);
    }

    private int getFirstChildHeightAndTopMargin() {
        return header.getChildAt(0).getHeight() + header.getChildAt(0).getTop();
    }

    private boolean hasChildren() {
        return header.getChildCount() > 0;
    }
}
