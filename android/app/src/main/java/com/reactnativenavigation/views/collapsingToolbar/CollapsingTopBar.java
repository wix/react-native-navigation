package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.TitleBar;
import com.reactnativenavigation.views.TopBar;

public class CollapsingTopBar extends TopBar implements CollapsingView {
    private CollapsingTopBarBackground collapsingTopBarBackground;
    private CollapsingTopBarReactView headerView;
    private ScrollListener scrollListener;
    private float finalCollapsedTranslation;
    private CollapsingTopBarParams params;
    private final ViewCollapser viewCollapser;
    private final int topBarHeight;

    public CollapsingTopBar(Context context, final CollapsingTopBarParams params) {
        super(context);
        this.params = params;
        topBarHeight = calculateTopBarHeight();
        createBackgroundImage(params);
        createReactView(params);
        calculateFinalCollapsedTranslation(params);
        viewCollapser = new ViewCollapser(this);
    }

    private void calculateFinalCollapsedTranslation(final CollapsingTopBarParams params) {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                if (params.hasBackgroundImage()) {
                    finalCollapsedTranslation = getCollapsedHeight() - getHeight();
                } else {
                    finalCollapsedTranslation = -titleBar.getHeight();
                }
            }
        });
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    private void createBackgroundImage(CollapsingTopBarParams params) {
        if (params.hasBackgroundImage()) {
            collapsingTopBarBackground = new CollapsingTopBarBackground(getContext(), params);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) CollapsingTopBarBackground.MAX_HEIGHT);
            titleBarAndContextualMenuContainer.addView(collapsingTopBarBackground, lp);
        }
    }

    private void createReactView(CollapsingTopBarParams params) {
        if (params.hasReactView()) {
            headerView = new CollapsingTopBarReactView(getContext(),
                    params.reactViewId,
                    new NavigationParams(Bundle.EMPTY));
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) ViewUtils.convertDpToPixel(400));
            titleBarAndContextualMenuContainer.addView(headerView, lp);
        }
    }

    @Override
    protected TitleBar createTitleBar() {
        if (params.hasBackgroundImage()) {
            return new CollapsingTitleBar(getContext(),
                    getCollapsedHeight(),
                    scrollListener);
        } else {
            return super.createTitleBar();
        }
    }

    @Override
    public void collapse(CollapseAmount amount) {
        viewCollapser.collapse(amount);
        if (titleBar instanceof CollapsingTitleBar) {
            ((CollapsingTitleBar) titleBar).collapse(amount.get());
        }
        if (collapsingTopBarBackground != null) {
            collapsingTopBarBackground.collapse(amount.get());
        }
    }

    public void onScrollViewAdded(ScrollView scrollView) {
        scrollListener.onScrollViewAdded(scrollView);
    }

    @Override
    public float getFinalCollapseValue() {
        return finalCollapsedTranslation;
    }

    public int getCollapsedHeight() {
        if (params.hasBackgroundImage()) {
            return topBarHeight;
        } else if (params.hasReactView()) {
            return topBarHeight;
        } else if (topTabs != null) {
            return topTabs.getHeight();
        } else {
            return titleBar.getHeight();
        }
    }

    private int calculateTopBarHeight() {
        int[] attrs = new int[] {android.R.attr.actionBarSize};
        TypedArray ta = getContext().obtainStyledAttributes(attrs);
        final int result = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return result;
    }

    @Override
    public float getCurrentCollapseValue() {
        return getTranslationY();
    }

    @Override
    public View asView() {
        return this;
    }
}
