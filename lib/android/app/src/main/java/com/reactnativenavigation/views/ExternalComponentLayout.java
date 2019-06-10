package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;

@SuppressLint("ViewConstructor")
public class ExternalComponentLayout extends FrameLayout implements Component {
    public ExternalComponentLayout(Context context) {
		super(context);
    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 1;
    }
}
