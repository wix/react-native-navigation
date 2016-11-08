package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.params.CollapsingTopBarParams.CollapseBehaviour;

public class ScrollListener implements ScrollViewDelegate.OnScrollListener {
    private CollapseCalculator collapseCalculator;
    private OnScrollListener scrollListener;
    private CollapseBehaviour collapseBehaviour;

    public interface  OnScrollListener {
        void onScroll(float amount);
    }

    public ScrollListener(CollapseCalculator collapseCalculator, OnScrollListener scrollListener,
                          CollapseBehaviour collapseBehaviour) {
        this.collapseCalculator = collapseCalculator;
        this.scrollListener = scrollListener;
        this.collapseBehaviour = collapseBehaviour;
    }

    @Override
    public void onScrollViewAdded(ScrollView scrollView) {
        collapseCalculator.setScrollView(scrollView);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        CollapseAmount amount = collapseCalculator.calculate(event);
        if (amount.canCollapse()) {
            scrollListener.onScroll(amount.get());
            return CollapseBehaviour.CollapseTopBarFirst.equals(collapseBehaviour);
        }
        return false;
    }
}
