package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.reactnativenavigation.views.topbar.TopBar;

import static android.widget.RelativeLayout.BELOW;

@SuppressLint("ViewConstructor")
public class ExternalComponentLayout extends FrameLayout implements Component {
    public ExternalComponentLayout(Context context) {
		super(context);
        setContentDescription("ExternalComponentLayout");
    }

    @Override
    public void drawBehindTopBar() {
        if (getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.removeRule(BELOW);
            setLayoutParams(layoutParams);
        }
    }

    @Override
    public void drawBelowTopBar(TopBar topBar) {
        if (getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.addRule(BELOW, topBar.getId());
            setLayoutParams(layoutParams);
        }
    }

    @Override
    public boolean isRendered() {
        return getChildCount() >= 1;
    }
}
