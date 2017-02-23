package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.TitleBar;
import com.reactnativenavigation.views.TitleBarBackground;
import com.reactnativenavigation.views.TranslucentTitleBarBackground;

public class CollapsingTitleBar extends TitleBar implements View.OnTouchListener {
    private CollapsingTextView title;
    private int collapsedHeight;
    private final ScrollListener scrollListener;
    private final CollapsingTopBarParams params;
    private TitleBarBackground titleBarBackground;

    public CollapsingTitleBar(Context context, int collapsedHeight, ScrollListener scrollListener, CollapsingTopBarParams params) {
        super(context);
        this.collapsedHeight = collapsedHeight;
        this.scrollListener = scrollListener;
        this.params = params;
        addCollapsingTitle();
        setOnTouchListener(this);
        hideTitle(params);
    }

    private void hideTitle(CollapsingTopBarParams params) {
        if (params.showTitleWhenCollapsed) {
            ViewUtils.runOnPreDraw(this, new Runnable() {
                @Override
                public void run() {
                    View titleView = getTitleView();
                    if (titleView != null) {
                        titleView.setAlpha(0);
                    }
                }
            });
        }
    }

    @Override
    public void hideTitle() {
        super.hideTitle();
        titleBarBackground.showTranslucentBackground();
    }

    @Override
    public void showTitle() {
        super.showTitle();
        titleBarBackground.showSolidBackground();
    }

    private void addCollapsingTitle() {
        if (params.hasBackgroundImage()) {
            title = new CollapsingTextView(getContext(), collapsedHeight);
            addView(title);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (params.hasBackgroundImage()) {
            this.title.setText((String) title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    protected void setTitleTextColor(StyleParams params) {
        if (this.params.hasBackgroundImage()) {
            title.setTextColor(params);
        } else {
            super.setTitleTextColor(params);
        }
    }

    @Override
    protected void setSubtitleTextColor(StyleParams params) {
        if (this.params.hasReactView()) {
            super.setSubtitleTextColor(params);
        }
    }

    @Override
    protected void setBackground(StyleParams params) {
        titleBarBackground = createBackground(params);
        setBackground(titleBarBackground);
    }

    private TitleBarBackground createBackground(StyleParams params) {
        return hasTranslucentAndSolidBackground(params) ?
            new TitleBarBackground(params.collapsingTopBarParams.scrimColor) :
            new TranslucentTitleBarBackground();
    }

    private boolean hasTranslucentAndSolidBackground(StyleParams params) {
        return params.topBarTranslucent && params.collapsingTopBarParams.scrimColor.hasColor();
    }

    public void collapse(CollapseAmount amount) {
        if (amount.hasExactAmount()) {
            collapse(amount.get());
        }
    }

    private void collapse(float collapse) {
        if (params.hasBackgroundImage()) {
            title.setTranslationY(0);
            title.collapseBy(collapse);
        }
        setTranslationY(-collapse);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return scrollListener.onTouch(event);
    }
}
