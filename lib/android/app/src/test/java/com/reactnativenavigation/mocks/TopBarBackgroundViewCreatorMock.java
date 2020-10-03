package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.views.stack.topbar.TopBarBackgroundView;
import com.reactnativenavigation.views.stack.topbar.TopBarBackgroundViewCreator;

import static org.mockito.Mockito.mock;

public class TopBarBackgroundViewCreatorMock extends TopBarBackgroundViewCreator {
    public TopBarBackgroundViewCreatorMock() {
        super(mock(ReactInstanceManager.class));
    }

    @Override
    public TopBarBackgroundView create(Activity activity, String componentId, String componentName) {
        return new TopBarBackgroundView(activity, instanceManager, componentId, componentName);
    }
}
