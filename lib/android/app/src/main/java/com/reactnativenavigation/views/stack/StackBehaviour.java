package com.reactnativenavigation.views.stack;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.views.BehaviourAdapter;
import com.reactnativenavigation.views.BehaviourDelegate;
import com.reactnativenavigation.views.topbar.TopBar;

public class StackBehaviour<V extends ViewGroup> extends BehaviourDelegate<V> {
    public StackBehaviour(BehaviourAdapter<V> delegate) {
        super(delegate);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        return dependency instanceof TopBar;
    }
}
