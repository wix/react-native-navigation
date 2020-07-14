package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;
import com.reactnativenavigation.views.component.ComponentLayout;
import com.reactnativenavigation.views.component.ReactComponent;

import static org.mockito.Mockito.spy;

public class TestComponentViewCreator implements ReactViewCreator {
    @Override
    public ReactComponent create(final Activity activity, final String componentId, final String componentName) {
        ReactView reactView = spy(new TestReactView(activity));
        return new ComponentLayout(activity, reactView);
    }
}
