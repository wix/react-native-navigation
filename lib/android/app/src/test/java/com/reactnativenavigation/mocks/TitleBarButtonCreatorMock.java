package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactButtonView;

public class TitleBarButtonCreatorMock extends TitleBarButtonCreator {

    public TitleBarButtonCreatorMock() {

    }

    @Override
    public TitleBarReactButtonView create(Activity activity, ComponentOptions component) {
        return new TitleBarReactButtonView(activity, component) {
            @Override
            public void sendComponentStart(ComponentType type) {

            }

            @Override
            public void sendComponentStop(ComponentType type) {

            }
        };
    }
}
