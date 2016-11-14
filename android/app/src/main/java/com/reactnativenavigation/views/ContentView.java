package com.reactnativenavigation.views;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.facebook.react.ReactRootView;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.screens.SingleScreen;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.ViewMeasurer;

public class ContentView extends ReactRootView {
    private final String screenId;
    private final NavigationParams navigationParams;

    boolean isContentVisible = false;
    private SingleScreen.OnDisplayListener onDisplayListener;
    protected ViewMeasurer viewMeasurer;

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
        int measuredHeight = viewMeasurer.getMeasuredHeight(heightMeasureSpec);
        Log.i("Guy", "measuredHeight: " + measuredHeight + " " + getClass().getSimpleName());
        setMeasuredDimension(viewMeasurer.getMeasuredWidth(widthMeasureSpec),
                measuredHeight);
    }

    @Override
    public void onViewAdded(final View child) {
        super.onViewAdded(child);
        detectContentViewVisible(child);
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
}
