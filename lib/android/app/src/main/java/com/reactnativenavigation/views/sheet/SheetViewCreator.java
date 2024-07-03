package com.reactnativenavigation.views.sheet;

import android.app.Activity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ReactViewCreator;
import com.reactnativenavigation.react.ReactComponentViewCreator;
import com.reactnativenavigation.react.ReactView;

public class SheetViewCreator implements ReactViewCreator {

    private ReactInstanceManager instanceManager;

    public SheetViewCreator(ReactInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
    }

    @Override
    public IReactView create(Activity activity, String componentId, String componentName) {
        ReactView reactView = new ReactComponentViewCreator(instanceManager).create(activity, componentId, componentName);
        return new SheetLayout(activity, reactView);
    }
}
