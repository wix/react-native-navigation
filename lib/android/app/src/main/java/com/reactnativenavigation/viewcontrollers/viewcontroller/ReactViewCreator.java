package com.reactnativenavigation.viewcontrollers.viewcontroller;

import android.app.Activity;

public interface ReactViewCreator {

    IReactView create(Activity activity, String componentId, String componentName);
}
