package com.reactnativenavigation.viewcontrollers.viewcontroller;

import android.content.Context;

public interface ReactViewCreator {

    IReactView create(Context context, String componentId, String componentName);
}
