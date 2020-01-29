package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.views.titlebar.TitleBarReactButtonView;

import static org.mockito.Mockito.mock;

public class TopBarButtonCreatorMock implements ReactViewCreator {

    @Override
    public TitleBarReactButtonView create(Activity activity, String componentId, String componentName) {
        final ReactInstanceManager reactInstanceManager = mock(ReactInstanceManager.class);
        return new TitleBarReactButtonView(activity, reactInstanceManager, componentId, componentName) {
            @Override
            public void sendComponentStart(ComponentType type) {

            }

            @Override
            public void sendComponentStop(ComponentType type) {

            }
        };
    }
}
