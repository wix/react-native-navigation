package com.reactnativenavigation.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.react.ReactRootView;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.screens.SingleScreen;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollViewDelegate;
import com.reactnativenavigation.views.utils.ScrollViewDetector;
import com.reactnativenavigation.views.utils.ViewMeasurer;

public class ContentView extends ReactRootView {
    private final String screenId;
    private final NavigationParams navigationParams;

    boolean isContentVisible = false;
    private SingleScreen.OnDisplayListener onDisplayListener;
    @Nullable private ScrollViewDelegate scrollViewDelegate;
    private ViewMeasurer viewMeasurer;
    private ScrollViewDetector scrollViewDetector;

    public void setOnDisplayListener(SingleScreen.OnDisplayListener onDisplayListener) {
        this.onDisplayListener = onDisplayListener;
    }

    public ContentView(Context context, String screenId, NavigationParams navigationParams) {
        super(context);
        this.screenId = screenId;
        this.navigationParams = navigationParams;
        attachToJS();
        viewMeasurer = new ViewMeasurer();
    }

    public void setupCollapseDetection(ScrollListener scrollListener, OnScrollViewAddedListener onScrollViewAddedListener) {
        scrollViewDelegate = new ScrollViewDelegate(scrollListener);
        scrollViewDetector = new ScrollViewDetector(this, onScrollViewAddedListener, scrollViewDelegate);
    }

    public void setViewMeasurer(ViewMeasurer viewMeasurer) {
        this.viewMeasurer = viewMeasurer;
    }

    private void attachToJS() {
        startReactApplication(NavigationApplication.instance.getReactGateway().getReactInstanceManager(), screenId,
                navigationParams.toBundle());
    }

    public String getNavigatorEventId() {
        return navigationParams.navigatorEventId;
    }

    public void unmountReactView() {
        unmountReactApplication();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(viewMeasurer.getMeasuredWidth(widthMeasureSpec),
                viewMeasurer.getMeasuredHeight(heightMeasureSpec));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (scrollViewDelegate != null) {
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
        detectContentViewVisible(child);
        if (scrollViewDetector != null) {
            scrollViewDetector.detectScrollViewAdded(child);
        }
    }

    private void detectContentViewVisible(View child) {
        if (onDisplayListener != null) {
            ViewUtils.runOnPreDraw(child, new Runnable() {
                @Override
                public void run() {
                    if (!isContentVisible) {
                        isContentVisible = true;
                        onDisplayListener.onDisplay();
                        onDisplayListener = null;
                    }
                }
            });
        }
    }

    public void collapse(float collapse) {
        setTranslationY(collapse);
    }
}
