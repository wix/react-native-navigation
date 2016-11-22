package com.reactnativenavigation.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.views.collapsingToolbar.CollapseAmount;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingView;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollViewDelegate;
import com.reactnativenavigation.views.collapsingToolbar.ViewCollapser;
import com.reactnativenavigation.views.utils.ScrollViewDetector;

public class CollapsingContentView extends ContentView implements CollapsingView {

    private @Nullable ScrollViewDelegate scrollViewDelegate;
    private @Nullable ScrollViewDetector scrollViewDetector;
    private final ViewCollapser viewCollapser;

    public CollapsingContentView(Context context, String screenId, NavigationParams navigationParams) {
        super(context, screenId, navigationParams);
        viewCollapser = new ViewCollapser(this);
    }

    public void setupCollapseDetection(ScrollListener scrollListener, OnScrollViewAddedListener onScrollViewAddedListener) {
        scrollViewDelegate = new ScrollViewDelegate(scrollListener);
        scrollViewDetector = new ScrollViewDetector(this, onScrollViewAddedListener, scrollViewDelegate);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (scrollViewDelegate != null && scrollViewDelegate.hasScrollView()) {
            boolean consumed = scrollViewDelegate.didInterceptTouchEvent(ev);
            if (consumed) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onViewAdded(final View child) {
        super.onViewAdded(child);
        if (scrollViewDetector != null) {
            scrollViewDetector.detectScrollViewAdded(child);
        }
    }

    @Override
    public void collapse(CollapseAmount amount) {
        viewCollapser.collapse(amount);
    }

    public void destroy() {
        if (scrollViewDetector != null) {
            scrollViewDetector.destroy();
        }
        if (scrollViewDelegate != null) {
            scrollViewDelegate.destroy();
        }
    }

    @Override
    public float getFinalCollapseValue() {
        return 0;
    }

    @Override
    public float getCurrentCollapseValue() {
        return 0;
    }

    @Override
    public View asView() {
        return this;
    }
}
