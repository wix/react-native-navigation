package com.reactnativenavigation.views;

import android.content.Context;
import android.os.Bundle;
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
    private Bundle initialProps;

    boolean isContentVisible = false;
    private SingleScreen.OnDisplayListener onDisplayListener;
    protected ViewMeasurer viewMeasurer;

    public void setOnDisplayListener(SingleScreen.OnDisplayListener onDisplayListener) {
        this.onDisplayListener = onDisplayListener;
    }

    public ContentView(Context context, String screenId, NavigationParams navigationParams) {
        this(context, screenId, navigationParams, Bundle.EMPTY);
    }

    public ContentView(Context context, String screenId, NavigationParams navigationParams, Bundle initialProps) {
        super(context);
        this.screenId = screenId;
        this.navigationParams = navigationParams;
        this.initialProps = initialProps;
        attachToJS();
        viewMeasurer = new ViewMeasurer();
    }

    public void setViewMeasurer(ViewMeasurer viewMeasurer) {
        this.viewMeasurer = viewMeasurer;
    }

    private void attachToJS() {
        navigationParams.toBundle().putAll(initialProps);
        startReactApplication(NavigationApplication.instance.getReactGateway().getReactInstanceManager(),
                screenId,
                createInitialParams()
        );
    }

    private Bundle createInitialParams() {
        final Bundle params = new Bundle();
        params.putAll(navigationParams.toBundle());
        params.putAll(initialProps);
        return params;
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
        setMeasuredDimension(viewMeasurer.getMeasuredWidth(widthMeasureSpec), measuredHeight);
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
