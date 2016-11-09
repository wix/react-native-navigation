package com.reactnativenavigation.views.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollViewDelegate;

public class ScrollViewDetector {
    private ContentView contentView;
    private OnScrollViewAddedListener scrollViewAddedListener;
    private ScrollViewDelegate scrollViewDelegate;

    public ScrollViewDetector(ContentView contentView,
                              OnScrollViewAddedListener onScrollViewAddedListener,
                              final ScrollViewDelegate scrollViewDelegate) {
        this.contentView = contentView;
        this.scrollViewAddedListener = onScrollViewAddedListener;
        this.scrollViewDelegate = scrollViewDelegate;
    }

    public void detectScrollViewAdded(View child) {
        if (child instanceof ScrollView) {
            onScrollViewAdded((ScrollView) child);
        } else if (child instanceof ViewGroup) {
            Object maybeScrollView = ViewUtils.findChildByClass((ViewGroup) child, ScrollView.class);
            if (maybeScrollView instanceof ScrollView) {
                onScrollViewAdded((ScrollView) maybeScrollView);
            }
        }
    }

    private void onScrollViewAdded(final ScrollView scrollView) {
        if (scrollViewDelegate != null && !scrollViewDelegate.hasScrollView()) {
            scrollViewDelegate.onScrollViewAdded(scrollView);
            scrollViewAddedListener.onScrollViewAdded(scrollView);
            attachStateChangeListener(scrollView);
        }
    }

    private void attachStateChangeListener(final ScrollView scrollView) {
        scrollView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                scrollView.removeOnAttachStateChangeListener(this);
                scrollViewDelegate.onScrollViewRemoved();
                contentView.post(new Runnable() {
                    @Override
                    public void run() {
                        detectScrollViewAdded(contentView);
                    }
                });
            }
        });
    }

}
