package com.reactnativenavigation.views.bottomtabs;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.views.BehaviourAdapter;
import com.reactnativenavigation.views.BehaviourDelegate;
import com.reactnativenavigation.views.BottomTabs;

public class BottomTabsBehaviour<V extends ViewGroup> extends BehaviourDelegate<V> {
    public BottomTabsBehaviour(BehaviourAdapter<V> delegate) {
        super(delegate);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
        return dependency instanceof BottomTabs;
    }
}
