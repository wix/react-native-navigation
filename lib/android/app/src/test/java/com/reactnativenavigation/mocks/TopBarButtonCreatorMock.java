package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.views.TopBarReactButtonView;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;

import static org.mockito.Mockito.mock;

public class TopBarButtonCreatorMock implements ReactViewCreator {

    @Override
    public TopBarReactButtonView create(Activity activity, String componentId, String componentName) {
        final ReactInstanceManager reactInstanceManager = mock(ReactInstanceManager.class);
        return new TopBarReactButtonView(activity, reactInstanceManager, componentId, componentName) {
            @Override
            public void sendComponentStart() {

            }

            @Override
            public void sendComponentStop() {

            }
        };
    }
}
