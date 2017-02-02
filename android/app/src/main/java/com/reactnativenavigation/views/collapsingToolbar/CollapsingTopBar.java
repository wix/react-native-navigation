package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.TitleBar;
import com.reactnativenavigation.views.TopBar;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CollapsingTopBar extends TopBar implements CollapsingView {
    private CollapsingTopBarBackground collapsingTopBarBackground;
    private CollapsingTopBarReactHeader header;
    private ScrollListener scrollListener;
    private float finalCollapsedTranslation;
    private CollapsingTopBarParams params;
    private final ViewCollapser viewCollapser;
    private final int topBarHeight;
    private String title;

    public CollapsingTopBar(Context context, final CollapsingTopBarParams params) {
        super(context);
        this.params = params;
        topBarHeight = calculateTopBarHeight();
        createBackgroundImage(params);
        calculateFinalCollapsedTranslation(params);
        viewCollapser = new ViewCollapser(this);
    }

    private void calculateFinalCollapsedTranslation(final CollapsingTopBarParams params) {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                if (params.hasBackgroundImage() || params.hasReactView()) {
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

    @Override
    public void setTitle(String title) {
        if (params.hasReactView()) {
            this.title = title;
        } else {
            super.setTitle(title);
        }
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
            header = new CollapsingTopBarReactHeader(getContext(),
                    params.reactViewId,
                    new NavigationParams(Bundle.EMPTY),
                    scrollListener);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) ViewUtils.convertDpToPixel(params.reactViewHeight));
            titleBarAndContextualMenuContainer.addView(header, lp);
        }
    }

    @Override
    protected TitleBar createTitleBar() {
        if (params.hasBackgroundImage() || params.hasReactView()) {
            createReactView(params);
            return new CollapsingTitleBar(getContext(),
                    getCollapsedHeight(),
                    scrollListener,
                    params);
        } else {
            return super.createTitleBar();
        }
    }

    @Override
    protected void addTitleBar() {
        if (params.hasReactView()) {
            titleBarAndContextualMenuContainer.addView(titleBar, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        } else {
            super.addTitleBar();
        }
    }

    @Override
    public void collapse(CollapseAmount amount) {
        viewCollapser.collapse(amount);
        if (titleBar instanceof CollapsingTitleBar) {
            ((CollapsingTitleBar) titleBar).collapse(amount);
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
        if (topTabs != null) {
            return topTabs.getHeight();
        } else if (params.hasBackgroundImage()) {
            return topBarHeight;
        } else if (params.hasReactView()) {
            return topBarHeight;
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
