package com.reactnativenavigation.views;

import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public interface BehaviourAdapter {
    /**
     * @see CoordinatorLayout.Behavior#onMeasureChild
     * @return true if the Behavior measured the child view, false if the CoordinatorLayout should perform its default measurement
     */
    boolean onMeasureChild(CoordinatorLayout parent, ViewGroup child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed);

    /**
     * @see CoordinatorLayout.Behavior#onDependentViewChanged
     * @return true if the Behavior changed the child view's size or position, false otherwise
     */
    boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency);
}
