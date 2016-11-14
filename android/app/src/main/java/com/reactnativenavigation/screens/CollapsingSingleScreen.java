package com.reactnativenavigation.screens;

import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.CollapsingContentView;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.collapsingToolbar.CollapseAmount;
import com.reactnativenavigation.views.collapsingToolbar.CollapseCalculator;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingViewMeasurer;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBar;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingView;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseBehaviour;

public class CollapsingSingleScreen extends SingleScreen {

    public CollapsingSingleScreen(AppCompatActivity activity, ScreenParams screenParams, LeftButtonOnClickListener titleBarBarBackButtonListener) {
        super(activity, screenParams, titleBarBarBackButtonListener);
    }

    @Override
    public void destroy() {
        super.destroy();
        ((CollapsingContentView) contentView).destroy();
    }

    @Override
    protected void createTopBar() {
        final CollapsingTopBar topBar = new CollapsingTopBar(getContext(), styleParams.collapsingTopBarParams);
        topBar.setScrollListener(getScrollListener(topBar));
        this.topBar = topBar;
    }

    @Override
    protected void createContent() {
        contentView = new CollapsingContentView(getContext(), screenParams.screenId, screenParams.navigationParams);
        if (screenParams.styleParams.drawScreenBelowTopBar) {
            contentView.setViewMeasurer(new CollapsingViewMeasurer((CollapsingTopBar) topBar, this));
        }
        setupCollapseDetection((CollapsingTopBar) topBar);
        addView(contentView, createLayoutParams());
    }

    private void setupCollapseDetection(final CollapsingTopBar topBar) {
        ((CollapsingContentView) contentView).setupCollapseDetection(getScrollListener(topBar), new OnScrollViewAddedListener() {
            @Override
            public void onScrollViewAdded(ScrollView scrollView) {
                topBar.onScrollViewAdded(scrollView);
            }
        });
    }

    private ScrollListener getScrollListener(final CollapsingTopBar topBar) {
        return new ScrollListener(new CollapseCalculator(topBar, getCollapseBehaviour()),
                new OnScrollListener() {
                    @Override
                    public void onScroll(CollapseAmount amount) {
                        if (!screenParams.styleParams.titleBarHideOnScroll) {
                            ((CollapsingView) contentView).collapse(amount);
                        }
                        topBar.collapse(amount);
                    }

                    @Override
                    public void onFling(CollapseAmount amount) {
                        topBar.collapse(amount);
                    }
                },
                getCollapseBehaviour()
        );
    }

    private CollapseBehaviour getCollapseBehaviour() {
        return screenParams.styleParams.collapsingTopBarParams.collapseBehaviour;
    }
}
