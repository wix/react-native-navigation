package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.reactnativenavigation.viewcontrollers.IReactView;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.views.ComponentLayout;

import static org.mockito.Mockito.spy;

public class TopBarButtonCreatorMock implements ReactViewCreator {
    @Override
    public IReactView create(Activity activity, String componentId, String componentName) {
        IReactView reactView = spy(new TestReactView(activity));
        return new ComponentLayout(activity, reactView);
    }
}
