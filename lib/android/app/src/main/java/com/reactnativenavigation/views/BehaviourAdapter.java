package com.reactnativenavigation.views;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;

public interface BehaviourAdapter<V extends ViewGroup> {
    /**
     * @see CoordinatorLayout.Behavior#onMeasureChild
     * @return true if the Behavior measured the child view, false if the CoordinatorLayout should perform its default measurement
     */
    boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed);

    /**
     * @see CoordinatorLayout.Behavior#onDependentViewChanged
     * @return true if the Behavior changed the child view's size or position, false otherwise
     */
    boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency);
}
