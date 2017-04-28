package com.reactnativenavigation.layouts;

import android.support.v7.app.AppCompatActivity;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.LeftButtonOnClickListener;

public class ModalScreenLayout extends SingleScreenLayout {

    public ModalScreenLayout(AppCompatActivity activity,
                             ScreenParams screenParams,
                             LeftButtonOnClickListener leftButtonOnClickListener) {
        super(activity, null, null, screenParams);
        this.leftButtonOnClickListener = leftButtonOnClickListener;
    }

    @Override
    protected void pushInitialScreen(LayoutParams lp) {
        stack.pushInitialScreenWithAnimation(screenParams, lp);
    }
}
