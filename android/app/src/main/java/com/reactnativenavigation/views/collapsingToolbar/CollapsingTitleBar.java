package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.TitleBar;
import com.reactnativenavigation.views.TitleBarBackground;
import com.reactnativenavigation.views.TranslucentDrawable;

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
        setInitialTitleViewVisibility();
    }

    private void setInitialTitleViewVisibility() {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                View titleView = getTitleView();
                if (titleView == null) {
                    return;
                }
                if (params.showTitleWhenExpended) {
                    titleView.setAlpha(1);
                } else if (params.showTitleWhenCollapsed) {
                    titleView.setAlpha(0);
                }
            }
        });

    }

    @Override
    public void hideTitle() {
        if (!params.showTitleWhenExpended) {
            super.hideTitle();
        }
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
        titleBarBackground = createBackground(params,
                params.collapsingTopBarParams.expendedTitleBarColor,
                params.collapsingTopBarParams.scrimColor);
        setBackground(titleBarBackground);
    }

    private TitleBarBackground createBackground(StyleParams styleParams, StyleParams.Color expendedColor, StyleParams.Color collapsedColor) {
        final Drawable expendedDrawable = styleParams.topBarTranslucent ? new TranslucentDrawable() : new ColorDrawable(expendedColor.getColor(Color.TRANSPARENT));
        final Drawable collapsedDrawable = new ColorDrawable(collapsedColor.getColor(Color.TRANSPARENT));
        return new TitleBarBackground(expendedDrawable, collapsedDrawable);
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
