package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.params.StyleParams;
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
    private final StyleParams styleParams;
    private final CollapsingTopBarParams params;
    private final ViewCollapser viewCollapser;
    private final int topBarHeight;

    @Override
    public void destroy() {
        if (params.hasReactView()) {
            header.unmountReactView();
        }
    }

    public CollapsingTopBar(Context context, final StyleParams params) {
        super(context);
        styleParams = params;
        this.params = params.collapsingTopBarParams;
        topBarHeight = calculateTopBarHeight();
        createBackgroundImage();
        calculateFinalCollapsedTranslation();
        viewCollapser = new ViewCollapser(this);
    }

    private void calculateFinalCollapsedTranslation() {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                if (params.hasBackgroundImage() || params.hasReactView()) {
                    finalCollapsedTranslation = getCollapsedHeight() - getHeight();
                    if (styleParams.topBarCollapseOnScroll) {
                        finalCollapsedTranslation += titleBar.getHeight();
                    }
                } else {
                    finalCollapsedTranslation = -titleBar.getHeight();
                }
            }
        });
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    private void createBackgroundImage() {
        if (params.hasBackgroundImage()) {
            collapsingTopBarBackground = new CollapsingTopBarBackground(getContext(), params);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) CollapsingTopBarBackground.MAX_HEIGHT);
            titleBarAndContextualMenuContainer.addView(collapsingTopBarBackground, lp);
        }
    }

    private void createReactHeader(CollapsingTopBarParams params) {
        if (params.hasReactView()) {
            header = new CollapsingTopBarReactHeader(getContext(),
                    params,
                    new NavigationParams(Bundle.EMPTY),
                    scrollListener);
            titleBarAndContextualMenuContainer.addView(header, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            header.setOnHiddenListener(new CollapsingTopBarReactHeaderAnimator.OnHiddenListener() {
                @Override
                public void onHidden() {
                    titleBar.showTitle();
                }
            });
            header.setOnVisibleListener(new CollapsingTopBarReactHeaderAnimator.OnVisibleListener() {
                @Override
                public void onVisible() {
                    titleBar.hideTitle();
                }
            });
        }
    }

    @Override
    protected TitleBar createTitleBar() {
        if (params.hasBackgroundImage() || params.hasReactView()) {
            createReactHeader(params);
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
        if (header != null) {
            header.collapse(amount.get());
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
