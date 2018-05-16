package com.reactnativenavigation.layouts;

import android.support.v7.app.AppCompatActivity;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.LeftButtonOnClickListener;

import java.util.List;

public class ModalScreenLayout extends SingleScreenLayout {

    public ModalScreenLayout(AppCompatActivity activity,
                             ScreenParams screenParams,
                             LeftButtonOnClickListener leftButtonOnClickListener) {
        super(activity, null, null, screenParams);
        this.leftButtonOnClickListener = leftButtonOnClickListener;
    }

    @Override
    protected void pushInitialScreen(LayoutParams lp) {
        if (screenParams.screens.isEmpty()) {
            stack.pushInitialModalScreenWithAnimation(screenParams, lp);
        } else {
            stack.pushInitialScreen(screenParams, lp);
        }
    }

    @Override
    protected void pushAdditionalScreens(LayoutParams lp) {
        List<ScreenParams> screens = screenParams.screens;
        for (int i = 0, screensSize = screens.size(); i < screensSize; i++) {
            ScreenParams screen = screens.get(i);
            if (i == screens.size() - 1) {
                stack.pushInitialModalScreenWithAnimation(screen, lp);
            } else {
                stack.pushInitialScreen(screen, lp);
            }
        }
    }
}
