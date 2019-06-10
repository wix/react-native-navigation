package com.reactnativenavigation.views.component;

import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;

import com.reactnativenavigation.react.ReactView;

public class ComponentBehaviour extends CoordinatorLayout.Behavior<ReactView> {
    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, ReactView child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        int height = View.MeasureSpec.getSize(parentHeightMeasureSpec);
        Log.i("ComponentBehaviour", "onMeasureChild " +
                                    child.getComponentName() +
                                    " height: " + height
        );
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }
}
