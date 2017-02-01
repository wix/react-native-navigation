package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.views.TitleBar;

public class CollapsingTitleBar extends TitleBar implements View.OnTouchListener {
    private CollapsingTextView title;
    private int collapsedHeight;
    private final ScrollListener scrollListener;
    private final CollapsingTopBarParams params;

    public CollapsingTitleBar(Context context, int collapsedHeight, ScrollListener scrollListener, CollapsingTopBarParams params) {
        super(context);
        this.collapsedHeight = collapsedHeight;
        this.scrollListener = scrollListener;
        this.params = params;
        addCollapsingTitle();
        setOnTouchListener(this);
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
