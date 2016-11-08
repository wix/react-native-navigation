package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.TitleBar;
import com.reactnativenavigation.views.TopBar;

public class CollapsingTopBar extends TopBar implements CollapsingView {
    private CollapsingTopBarBackground collapsingTopBarBackground;
    private ScrollListener scrollListener;
    private float finalCollapsedTranslation;
    private CollapsingTopBarParams params;

    public CollapsingTopBar(Context context, final CollapsingTopBarParams params) {
        super(context);
        this.params = params;
        createCollapsingTopBar(params);
        calculateFinalCollapsedTranslation(params);
    }

    private void calculateFinalCollapsedTranslation(final CollapsingTopBarParams params) {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                if (params.hasBackgroundImage()) {
                    finalCollapsedTranslation =
                            getCollapsingTopBarBackground().getCollapsedTopBarHeight() - getHeight();
                } else {
                    finalCollapsedTranslation = -titleBar.getHeight();
                }
            }
        });
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    private void createCollapsingTopBar(CollapsingTopBarParams params) {
        if (params.hasBackgroundImage()) {
            collapsingTopBarBackground = new CollapsingTopBarBackground(getContext(), params);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) CollapsingTopBarBackground.MAX_HEIGHT);
            titleBarAndContextualMenuContainer.addView(collapsingTopBarBackground, lp);
        }
    }

    @Override
    protected TitleBar createTitleBar() {
        if (params.hasBackgroundImage()) {
            return new CollapsingTitleBar(getContext(),
                    collapsingTopBarBackground.getCollapsedTopBarHeight(),
                    scrollListener);
        } else {
            return super.createTitleBar();
        }
    }

    public CollapsingTopBarBackground getCollapsingTopBarBackground() {
        return collapsingTopBarBackground;
    }

    public void collapse(float collapse) {
        setTranslationY(collapse);
        if (titleBar instanceof CollapsingTitleBar) {
            ((CollapsingTitleBar) titleBar).collapse(collapse);
        }
        if (collapsingTopBarBackground != null) {
            collapsingTopBarBackground.collapse(collapse);
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
        return params.hasBackgroundImage() ?
                collapsingTopBarBackground.getCollapsedTopBarHeight() :
                titleBar.getHeight();
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
