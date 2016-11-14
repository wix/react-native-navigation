package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.reactnativenavigation.views.utils.ViewMeasurer;

public class CollapsingViewPager extends ViewPager implements CollapsingView {
    ViewMeasurer viewMeasurer;
    ViewCollapser viewCollapser;

    public CollapsingViewPager(Context context) {
        super(context);
        viewCollapser = new ViewCollapser(this);
        setBackgroundColor(Color.GREEN);
    }

    public void setViewMeasurer(ViewMeasurer viewMeasurer) {
        this.viewMeasurer = viewMeasurer;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(viewMeasurer.getMeasuredWidth(widthMeasureSpec),
                viewMeasurer.getMeasuredHeight(heightMeasureSpec));
    }

    @Override
    public float getFinalCollapseValue() {
        // Unused
        return 0;
    }

    @Override
    public float getCurrentCollapseValue() {
        return getTranslationY();
    }

    @Override
    public View asView() {
        return this;
    }

    @Override
    public void collapse(CollapseAmount amount) {
        viewCollapser.collapse(amount);
    }
}
