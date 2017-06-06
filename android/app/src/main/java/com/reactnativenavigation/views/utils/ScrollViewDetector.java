package com.reactnativenavigation.views.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollViewDelegate;

public class ScrollViewDetector {
    private static final boolean SCROLLVIEW_FOUND = true;
    private static final boolean SCROLLVIEW_NOT_FOUND = false;
    private OnScrollViewAddedListener scrollViewAddedListener;
    private ScrollViewDelegate scrollViewDelegate;
    private View.OnAttachStateChangeListener scrollViewStateChangeListener;
    private ViewTreeObserver.OnGlobalLayoutListener scrollViewDetectorListener;

    public ScrollViewDetector(ContentView contentView, OnScrollViewAddedListener onScrollViewAddedListener,
                              ScrollViewDelegate scrollViewDelegate) {
        this.scrollViewAddedListener = onScrollViewAddedListener;
        this.scrollViewDelegate = scrollViewDelegate;
        scrollViewStateChangeListener = createScrollViewStateChangeListener(contentView, scrollViewDelegate);
    }

    private StateChangeListenerAdapter createScrollViewStateChangeListener(final ContentView contentView, final ScrollViewDelegate scrollViewDelegate) {
        return new StateChangeListenerAdapter() {
            @Override
            public void onViewDetachedFromWindow(View scrollView) {
                scrollViewDelegate.getScrollView().removeOnAttachStateChangeListener(this);
                scrollViewDelegate.onScrollViewRemoved();
                detectScrollView(scrollViewDelegate, contentView);
            }
        };
    }

    private void detectScrollView(final ScrollViewDelegate scrollViewDelegate, final ContentView contentView) {
        scrollViewDetectorListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!scrollViewDelegate.hasScrollView()) {
                    if (findScrollView(contentView) && contentView.getViewTreeObserver().isAlive()) {
                        contentView.getViewTreeObserver().removeOnGlobalLayoutListener(scrollViewDetectorListener);
                    }
                }
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(scrollViewDetectorListener);
    }

    public boolean findScrollView(View child) {
        if (child instanceof ScrollView) {
            onScrollViewFound((ScrollView) child);
            return SCROLLVIEW_FOUND;
        } else if (child instanceof ViewGroup) {
            Object maybeScrollView = ViewUtils.findChildByClass((ViewGroup) child, ScrollView.class);
            if (maybeScrollView instanceof ScrollView) {
                onScrollViewFound((ScrollView) maybeScrollView);
                return SCROLLVIEW_FOUND;
            }
        }
        return SCROLLVIEW_NOT_FOUND;
    }

    private void onScrollViewFound(final ScrollView scrollView) {
        if (scrollViewDelegate != null && !scrollViewDelegate.hasScrollView()) {
            scrollViewDelegate.onScrollViewAdded(scrollView);
            scrollViewAddedListener.onScrollViewAdded(scrollView);
            scrollView.addOnAttachStateChangeListener(scrollViewStateChangeListener);
        }
    }

    public void destroy() {
        if (scrollViewDelegate.getScrollView() != null) {
            scrollViewDelegate.getScrollView().removeOnAttachStateChangeListener(scrollViewStateChangeListener);
        }
    }
}
