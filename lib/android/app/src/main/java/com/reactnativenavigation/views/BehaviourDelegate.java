package com.reactnativenavigation.views;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;

public class BehaviourDelegate<V extends ViewGroup> extends CoordinatorLayout.Behavior<V> {

    private BehaviourAdapter delegate;

    public BehaviourDelegate(BehaviourAdapter<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        return delegate.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        return delegate.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }
}
