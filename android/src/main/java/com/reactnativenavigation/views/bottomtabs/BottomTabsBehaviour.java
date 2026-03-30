package com.reactnativenavigation.views.bottomtabs;

import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.views.BehaviourAdapter;
import com.reactnativenavigation.views.BehaviourDelegate;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class BottomTabsBehaviour extends BehaviourDelegate {
    public BottomTabsBehaviour(BehaviourAdapter delegate) {
        super(delegate);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull ViewGroup child, @NonNull View dependency) {
        return dependency instanceof BottomTabsContainer;
    }
}
