package com.reactnativenavigation.screens;

import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.collapsingToolbar.CollapseCalculator;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingContentViewMeasurer;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBar;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;

public class CollapsingSingleScreen extends SingleScreen {

    public CollapsingSingleScreen(AppCompatActivity activity, ScreenParams screenParams, LeftButtonOnClickListener titleBarBarBackButtonListener) {
        super(activity, screenParams, titleBarBarBackButtonListener);
    }

    @Override
    protected void createTopBar() {
        final CollapsingTopBar topBar = new CollapsingTopBar(getContext(), styleParams.collapsingTopBarParams);
        topBar.setScrollListener(getScrollListener(topBar));
        this.topBar = topBar;
    }

    @Override
    protected void createContent() {
        contentView = new ContentView(getContext(), screenParams.screenId, screenParams.navigationParams);
        if (screenParams.styleParams.drawScreenBelowTopBar) {
            contentView.setViewMeasurer(new CollapsingContentViewMeasurer((CollapsingTopBar) topBar, this));
        }
        setupCollapseDetection((CollapsingTopBar) topBar);
        addView(contentView, createLayoutParams());
    }

    private void setupCollapseDetection(final CollapsingTopBar topBar) {
        contentView.setupCollapseDetection(getScrollListener(topBar));
        contentView.setOnScrollViewAddedListener(new OnScrollViewAddedListener() {
            @Override
            public void onScrollViewAdded(ScrollView scrollView) {
                topBar.onScrollViewAdded(scrollView);
            }
        });
    }

    private ScrollListener getScrollListener(final CollapsingTopBar topBar) {
        return new ScrollListener(new CollapseCalculator(topBar),
                new ScrollListener.OnScrollListener() {
                    @Override
                    public void onScroll(float amount) {
                        if (!screenParams.styleParams.titleBarHideOnScroll) {
                            contentView.collapse(amount);
                        }
                        topBar.collapse(amount);
                    }
                },
                screenParams.styleParams.collapsingTopBarParams.collapseBehaviour
        );
    }
}
