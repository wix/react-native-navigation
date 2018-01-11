package com.reactnativenavigation.mocks;

import android.app.*;

import com.reactnativenavigation.viewcontrollers.*;
import com.reactnativenavigation.viewcontrollers.ComponentViewController.*;

public class TestComponentViewCreator implements ReactViewCreator {
    @Override
    public ComponentViewController.IReactView create(final Activity activity, final String componentId, final String componentName) {
        return new TestComponentLayout(activity);
    }
}
