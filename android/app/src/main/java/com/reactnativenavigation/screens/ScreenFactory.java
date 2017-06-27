package com.reactnativenavigation.screens;

import android.support.v7.app.AppCompatActivity;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.MenuButtonOnClickListener;

class ScreenFactory {
    static Screen create(AppCompatActivity activity,
                         ScreenParams screenParams,
                         LeftButtonOnClickListener leftButtonOnClickListener,
						 MenuButtonOnClickListener rightButtonsClickListener) {
        if (screenParams.isFragmentScreen()) {
            return new FragmentScreen(activity, screenParams, leftButtonOnClickListener, rightButtonsClickListener);
        } else if (screenParams.hasTopTabs()) {
            if (screenParams.hasCollapsingTopBar()) {
                return new CollapsingViewPagerScreen(activity, screenParams, leftButtonOnClickListener, rightButtonsClickListener);
            } else {
                return new ViewPagerScreen(activity, screenParams, leftButtonOnClickListener, rightButtonsClickListener);
            }
        } else if (screenParams.hasCollapsingTopBar()) {
            return new CollapsingSingleScreen(activity, screenParams, leftButtonOnClickListener, rightButtonsClickListener);
        } else {
            return new SingleScreen(activity, screenParams, leftButtonOnClickListener, rightButtonsClickListener);
        }
    }
}
