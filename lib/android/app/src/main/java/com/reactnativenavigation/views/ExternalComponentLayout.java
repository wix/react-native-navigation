package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.ComponentOptionsPresenter;

import static android.widget.RelativeLayout.BELOW;

@SuppressLint("ViewConstructor")
public class ExternalComponentLayout extends FrameLayout implements Component {
    public ExternalComponentLayout(Context context) {
		super(context);
        setContentDescription("ExternalComponentLayout");
    }

    @Override
    public void applyOptions(Options options) {
        new ComponentOptionsPresenter(this).present(options);
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
}
