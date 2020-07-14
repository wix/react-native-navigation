package com.reactnativenavigation.views.stack;

import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.views.BehaviourAdapter;
import com.reactnativenavigation.views.BehaviourDelegate;
import com.reactnativenavigation.views.stack.fab.Fab;
import com.reactnativenavigation.views.stack.fab.FabMenu;
import com.reactnativenavigation.views.stack.topbar.TopBar;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class StackBehaviour extends BehaviourDelegate {
    public StackBehaviour(BehaviourAdapter delegate) {
        super(delegate);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull ViewGroup child, @NonNull View dependency) {
        return dependency instanceof TopBar ||
               dependency instanceof Fab ||
               dependency instanceof FabMenu;
    }
}
