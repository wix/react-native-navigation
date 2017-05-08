package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CollapsingViewPager extends ViewPager implements CollapsingView {
    CollapsingViewMeasurer viewMeasurer;
    ViewCollapser viewCollapser;

    public CollapsingViewPager(Context context) {
        super(context);
        viewCollapser = new ViewCollapser(this);
    }

    public void setViewMeasurer(CollapsingViewMeasurer viewMeasurer) {
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
        return viewMeasurer.getFinalCollapseValue();
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

    @Override
    public void fling(CollapseAmount amount) {
        viewCollapser.fling(amount);
    }
}
