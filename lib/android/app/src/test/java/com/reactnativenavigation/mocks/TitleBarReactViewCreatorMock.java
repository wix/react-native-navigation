package com.reactnativenavigation.mocks;

import static org.mockito.Mockito.mock;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactView;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarReactViewCreator;

public class TitleBarReactViewCreatorMock extends TitleBarReactViewCreator {
    public TitleBarReactViewCreatorMock() {
        super(mock(ReactInstanceManager.class));
    }

    @Override
    public TitleBarReactView create(Activity activity, String componentId, String componentName) {
        return new TitleBarReactView(activity, instanceManager, componentId, componentName);
    }
}
