package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.params.CollapsingTopBarParams.CollapseBehaviour;
import com.reactnativenavigation.views.collapsingToolbar.ScrollDirection.Direction;

public class ScrollListener implements OnFlingListener {
    private CollapseCalculator collapseCalculator;
    private OnScrollListener scrollListener;
    private CollapseBehaviour collapseBehaviour;

    public ScrollListener(CollapseCalculator collapseCalculator, OnScrollListener scrollListener,
                          CollapseBehaviour collapseBehaviour) {
        this.collapseCalculator = collapseCalculator;
        this.scrollListener = scrollListener;
        this.collapseBehaviour = collapseBehaviour;
        collapseCalculator.setFlingListener(this);
    }

    void onScrollViewAdded(ScrollView scrollView) {
        collapseCalculator.setScrollView(scrollView);
    }

    boolean onTouch(MotionEvent event) {
        CollapseAmount amount = collapseCalculator.calculate(event);
        if (amount.canCollapse()) {
            scrollListener.onScroll(amount.get());
            return CollapseBehaviour.CollapseTopBarFirst.equals(collapseBehaviour);
        }
        return false;
    }

    @Override
    public void onFling(Direction direction) {
        scrollListener.onFling(direction);
    }
}
