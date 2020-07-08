package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactButtonView;

import static org.mockito.Mockito.mock;

public class TitleBarButtonCreatorMock extends TitleBarButtonCreator {

    public TitleBarButtonCreatorMock() {
        super(null);
    }

    @Override
    public TitleBarReactButtonView create(Activity activity, ComponentOptions component) {
        final ReactInstanceManager reactInstanceManager = mock(ReactInstanceManager.class);
        return new TitleBarReactButtonView(activity, reactInstanceManager, component) {
            @Override
            public void sendComponentStart(ComponentType type) {

            }

            @Override
            public void sendComponentStop(ComponentType type) {

            }
        };
    }
}
