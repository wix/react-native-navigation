package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;

public interface  OnScrollListener extends OnFlingListener {
    void onScroll(MotionEvent event, CollapseAmount amount);
}
