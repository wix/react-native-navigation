package com.reactnativenavigation.mocks;

import android.app.Activity;

import com.reactnativenavigation.viewcontrollers.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.ComponentViewController.ReactViewCreator;

import static org.mockito.Mockito.spy;

public class TestComponentViewCreator implements ReactViewCreator {
    @Override
    public ComponentViewController.IReactView create(final Activity activity, final String componentId, final String componentName) {
        ComponentViewController.IReactView reactView = spy(new TestReactView(activity));
        return new TestComponentLayout(activity, reactView);
    }
}
