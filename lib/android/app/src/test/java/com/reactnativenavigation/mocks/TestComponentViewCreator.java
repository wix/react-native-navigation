package com.reactnativenavigation.mocks;

import static org.mockito.Mockito.spy;

import android.content.Context;

import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;
import com.reactnativenavigation.views.component.ComponentLayout;
import com.reactnativenavigation.views.component.ReactComponent;

public class TestComponentViewCreator implements ReactViewCreator {
    @Override
    public ReactComponent create(final Context context, final String componentId, final String componentName) {
        ReactView reactView = spy(new TestReactView(context));
        return new ComponentLayout(context, reactView);
    }
}
