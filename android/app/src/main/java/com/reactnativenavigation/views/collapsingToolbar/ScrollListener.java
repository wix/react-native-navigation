package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.params.CollapsingTopBarParams.CollapseBehaviour;

public class ScrollListener {
    private CollapseCalculator collapseCalculator;
    private OnScrollListener scrollListener;
    private CollapseBehaviour collapseBehaviour;

    public ScrollListener(CollapseCalculator collapseCalculator, OnScrollListener scrollListener,
                          CollapseBehaviour collapseBehaviour) {
        this.collapseCalculator = collapseCalculator;
        this.scrollListener = scrollListener;
        this.collapseBehaviour = collapseBehaviour;
    }

    void onScrollViewAdded(ScrollView scrollView) {
        collapseCalculator.setScrollView(scrollView);
    }

    boolean onTouch(MotionEvent event) {
        CollapseAmount amount = collapseCalculator.calculate(event);
        if (amount.canCollapse()) {
            scrollListener.onScroll(amount);
            return CollapseBehaviour.CollapseTopBarFirst.equals(collapseBehaviour);
        }
        return false;
    }
}
