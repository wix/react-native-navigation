package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

@SuppressLint("ViewConstructor")
public class ExternalComponentLayout extends CoordinatorLayout implements Component {
    public ExternalComponentLayout(Context context) {
		super(context);
    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 1;
    }
}
